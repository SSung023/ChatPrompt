import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { SET_IO_IDX, SET_IO_TASKID, userContext } from '../../context/UserContext';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditIo.module.css';

export default function EditIo() {
    const context = useContext(userContext);
    const [input, setInput] = useState('');
    const [output, setOutput]= useState('');

    // store 정보
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const idx = context.state.data.io_idx;

    // 내부 관리용
    const [taskNum, setTaskNum] = useState(taskId);
    const [taskIdx, setIdx] = useState(1);

    // state 관리
    const handleChangeInput = (value) => {
        setInput(value);
    }
    const handleChangeOutput = (value) => {
        setOutput(value);
    }

    // 입출력 입력칸에 불러오기
    const load = async (e) => {
        axios.get(`/api/tasks/${taskNum}/assignment/${taskIdx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            console.log("load:");
            console.log(data);
            setInput(data.input);
            setOutput(data.output);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }
    // 제출
    const saveIo = async (e) => {
        e.preventDefault();
        axios.patch(`/api/tasks/${taskNum}/assignment/${taskIdx}`, {
            input: `${input}`,
            output: `${output}`,
        })
        .then(function(res) {
            console.log('result:');
            console.log(res);
            if(taskIdx < 100){
                setInput('');
                setOutput('');
                // 다음 index로 state 초기화
                context.actions.contextDispatch({ type: SET_IO_IDX, data: (parseInt(taskIdx)+1)});
                // taskIdx 상승
                setIdx(prev => parseInt(prev) + 1);
            }
            else if(taskIdx >= 100){
                alert('마지막 인덱스입니다!');
            }
        })
        .catch(function(err) {
            console.log(err);
        })
    }

    // task, index 관리
    const handlePressEnter = (e) => {
        const id = e.target.id;
        if(e.key === "Enter"){
            if(id === "task") {
                const value= e.target.value;
                value >= first_taskId && value <= last_taskId && setTaskNum(value);
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: taskNum});
                context.actions.contextDispatch({ type: SET_IO_IDX, data: taskIdx});
                load(e);
            }
            else if(id === "idx") {
                const value= e.target.value;
                value >=1 && value <=100 && setIdx(value);
                context.actions.contextDispatch({ type: SET_IO_IDX, data: taskIdx});
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: taskNum});
                load(e);
            }
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value= e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(value);
        }
        else if(id === "idx") {
            const value= e.target.value;
            value >= 1 && value <= 100 && setIdx(value);
        }
    }

    useEffect(() => {
        // 입출력 작성 폼에 불러오기
        load();
    }, [taskId]);

    return (
        <div className={styles.ioEdit}>
            <div className={styles.header}>
                <p className={styles.title}>* 위 지시문에 대하여 적절한 입력과 출력을 작성하시오.</p>
                {/* taskId, idx input form */}
                <form
                    className={styles.ioForm}
                >
                    <label>task: </label>
                    <input 
                        type="number"
                        id="task"
                        onChange={(e) => {
                            const value = e.target.value;
                            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                        }}
                        max="120"
                        min="1"
                        value={taskNum}
                        onKeyDown={handlePressEnter}
                        onBlur={handleOnBlur}
                    />

                    <label>index: </label>
                    <input 
                        type="number"
                        id="idx"
                        onChange={(e) => {
                            const value = e.target.value;
                            value >=1 && value <=100 && setIdx(parseInt(e.target.value));
                        }}
                        max="100"
                        min="1"
                        value={taskIdx}
                        onKeyDown={handlePressEnter}
                        onBlur={handleOnBlur}
                    />
                </form>
                <p style={{ 
                    color: `#e02b2b`, 
                    fontSize: `12px`, 
                    marginLeft: `1em`,
                    lineHeight: `1.5em`,    
                }}>
                    ⚠ 엔터를 누르면 저장되지 않고 이동합니다.
                </p>
            </div>

            {/* io input form */}
            <form>
                <Table>
                    <TableBody>
                        <TableRow>
                            <TableHead>{`입력${idx}`}</TableHead>
                            <TableCell>
                                <TextArea input={input} setInput={handleChangeInput} placeholder={`입력을 작성하세요.`}/>
                            </TableCell>
                        </TableRow>
                        <TableRow>
                            <TableHead>{`출력${idx}`}</TableHead>
                            <TableCell>
                                <TextArea input={output} setInput={handleChangeOutput} placeholder={`출력을 작성하세요.`}/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                <div className={styles.buttons}>
                    <button onClick={saveIo} className={styles.button}>저장하고 다음으로 이동</button>
                </div>
            </form>
        </div>
    );
}