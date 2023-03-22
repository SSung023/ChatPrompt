import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { SET_IO_TASKID, userContext } from '../../context/UserContext';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditIo.module.css';

export default function EditIo() {
    const context = useContext(userContext);
    const [input, setInput] = useState('');
    const [output, setOutput]= useState('');

    const taskId = context.state.data.io_taskId;
    const [taskNum, setTaskNum] = useState(taskId);
    const idx = context.state.data.io_idx;
    const [taskIdx, setIdx] = useState(1);

    // state 관리
    const handleChangeInput = (value) => {
        setInput(value);
    }
    const handleChangeOutput = (value) => {
        setOutput(value);
    }

    // 제출 관련
    const load = async (e) => {
        axios.get(`/api/tasks/${taskNum}/assignment`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setInput(data.input);
            setOutput(data.output);
            context.actions.contextDispatch({ type: SET_IO_TASKID, data: taskNum});
        })
    }
    const save = async (e) => {
        e.preventDefault();
        axios.patch(`/api/tasks/${taskNum}/assignment`, {
            input: `${input}`,
            output: `${output}`,
        })
        .then(function(res) {
            if(taskNum < 120){
                setInput('');
                setOutput('');
                // 다음 task로 state 초기화
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: (parseInt(taskNum)+1)});
                // taskNum 상승
                setTaskNum(prev => parseInt(prev) + 1);
            }
            else if(taskNum >=120){
                alert('마지막 태스크입니다!');
            }
        })
        .catch(function(err) {
            console.log(err);
        })
    }
    const handlePressEnter = (e) => {
        const id = e.target.id;
        if(e.key === "Enter"){
            if(id === "task") {
                const value= e.target.value;
                value >=1 && value <=120 && setTaskNum(value);
            }
            else if(id === "idx") {
                const value= e.target.value;
                value >=1 && value <=120 && setIdx(value);
            }
            load(e);
        }
    } 

    useEffect(() => {
        load();
    }, [taskId, idx]);

    return (
        <div className={styles.ioEdit}>
            <div className={styles.header}>
                <p className={styles.title}>* 위 지시문에 대하여 적절한 입력과 출력을 작성하시오.</p>
                
                <label>task: </label>
                <input 
                    type="number"
                    id="task"
                    onChange={(e) => {
                        const value = e.target.value;
                        value >=1 && value <=120 && setTaskNum(parseInt(e.target.value))
                    }}
                    max="120"
                    min="1"
                    value={taskNum}
                    onKeyDown={handlePressEnter}
                />

                <label>index: </label>
                <input 
                    type="number"
                    id="idx"
                    onChange={(e) => {
                        const value = e.target.value;
                        value >=1 && value <=100 && setIdx(parseInt(e.target.value))
                    }}
                    max="100"
                    min="1"
                    value={taskIdx}
                    onKeyDown={handlePressEnter}
                />
            </div>
            <form>
                <Table>
                    <TableBody>
                        <TableRow>
                            <TableHead>입력</TableHead>
                            <TableCell>
                                <TextArea input={input} setInput={handleChangeInput} placeholder={`입력`}/>
                            </TableCell>
                        </TableRow>
                        <TableRow>
                            <TableHead>출력</TableHead>
                            <TableCell>
                                <TextArea input={output} setInput={handleChangeOutput} placeholder={`출력`}/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                <div className={styles.buttons}>
                    <button onClick={save} className={styles.button}>저장하고 다음 페이지로 이동</button>
                </div>
            </form>
        </div>
    );
}