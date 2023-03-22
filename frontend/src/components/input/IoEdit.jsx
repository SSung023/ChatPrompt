import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import { SET_IO_TASKID, userContext } from '../../context/UserContext';
import { UnformattedTaskId } from '../../utility/FormattedTaskId';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import TextArea from '../ui/textarea/TextArea';
import styles from './IoEdit.module.css';

export default function IoEdit() {
    const context = useContext(userContext);
    const [input, setInput] = useState('');
    const [output, setOutput]= useState('');

    const taskId = context.state.data.io_taskId;
    const [taskNum, setTaskNum] = useState(taskId);
    const [idx, setIdx] = useState(1);

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
    const save = async () => {
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
        if(e.key === "Enter"){
            const value= e.target.value;
            value >=1 && value <=120 && setTaskNum(value);
            load(e);
        }
    } 

    useEffect(() => {
        load();
    }, [taskId]);

    return (
        <div className={styles.ioEdit}>
            <div className={styles.header}>
                <p className={styles.title}>* 위 지시문에 대하여 적절한 입력과 출력을 작성하시오.</p>
                
                <label>task: </label>
                <input 
                    type="number"
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
                    onChange={(e) => {
                        const value = e.target.value;
                        value >=1 && value <=120 && setTaskNum(parseInt(e.target.value))
                    }}
                    max="120"
                    min="1"
                    value={taskNum}
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
                            <TableHead>입력</TableHead>
                            <TableCell>
                                <TextArea input={output} setInput={handleChangeOutput} placeholder={`출력`}/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>
                <button onClick={save} className={styles.button}>저장하고 다음으로 넘어가기</button>
            </form>
        </div>
    );
}