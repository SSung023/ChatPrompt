import axios from 'axios';
import React, { useContext, useEffect, useRef, useState } from 'react';
import { SET_IO_IDX, SET_IO_TASKID, userContext } from '../../context/UserContext';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditIo.module.css';

// import { TbCircleArrowRightFilled, TbCircleArrowLeftFilled } from 'react-icons/tb';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import InputNumber from '../ui/input/InputNumber';

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
    const handleTaskId = (value) => {
        context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(value) });
    }
    const handleIdx = (value) => {
        context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(value) });
    }

    // state 관리
    const handleChangeInput = (value) => {
        setInput(value);
    }
    const handleChangeOutput = (value) => {
        setOutput(value);
    }

    // 입출력 입력칸에 불러오기
    const handleLoad = async (e) => {
        axios.get(`/api/tasks/${taskId}/assignment-io/${idx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setInput(data.input);
            setOutput(data.output);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }
    // 제출하고 다음으로 이동
    const handleSaveAndLoad = async (e) => {
        e.preventDefault();
        if(!taskId || taskId == 0 || taskId > last_taskId || taskId < first_taskId){
            alert('task id 확인 후 다시 제출해주세요.');
            return;
        }
        
        axios.patch(`/api/tasks/${taskId}/assignment-io/${idx}`, {
            input: `${input}`,
            output: `${output}`,
        })
        .then(function(res) {
            if(idx < 60){
                setInput('');
                setOutput('');
                // 다음 index로 state 초기화
                context.actions.contextDispatch({ type: SET_IO_IDX, data: (parseInt(idx)+1)});
            }
            else if(idx >= 60){
                if(taskId < last_taskId){
                    setInput('');
                    setOutput('');
                    // state 초기화
                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: (parseInt(taskId)+1) });
                    context.actions.contextDispatch({ type: SET_IO_IDX, data: (parseInt(1))});
                }
                else {
                    alert('마지막입니다!');
                }
            }
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('task id 혹은 index를 확인해주세요.');
            }
        })
    }
    // 제출
    const handleSave = async (e) => {
        e.preventDefault();
        if(!taskId || taskId == 0 || taskId > last_taskId || taskId < first_taskId){
            alert('task id 확인 후 다시 제출해주세요.');
            return;
        }

        axios.patch(`/api/tasks/${taskId}/assignment-io/${idx}`, {
            input: `${input}`,
            output: `${output}`,
        })
        .catch(function(err) {
            console.log(err);
        })
    }

    useEffect(() => {
        // 입출력 작성 폼에 불러오기
        handleLoad();
    }, [taskId, idx]);

    return (
        <div className={styles.ioEdit}>
            <div className={styles.wrapper}>
                <div className={styles.header}>
                    <p className={styles.title}>* 위 지시문에 대하여 적절한 입력과 출력을 작성하시오.</p>
                    {/* taskId, idx input form */}
                    <form
                        className={styles.ioForm}
                    >
                        <label className='noDrag'>task: </label>
                        <InputNumber
                            context={taskId}
                            setContext={handleTaskId}
                            maxNum={last_taskId}
                            minNum={first_taskId}
                        />

                        <label className='noDrag'>index: </label>
                        <InputNumber 
                            context={idx}
                            setContext={handleIdx}
                            maxNum={60}
                            minNum={1}
                        />
                    </form>
                </div>
                <div className={styles.moveBtns}>
                    <button
                        className={`${styles.moveBtn} noDrag`}
                        onClick={(e) => {
                            e.preventDefault();
                            if(idx > 1){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(idx)-1});
                            }
                            else {
                                if(taskId > first_taskId) {
                                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskId)-1 });
                                    context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(60)});
                                }
                                else {
                                    alert('첫 인덱스입니다.');
                                }
                            }
                        }}
                    ><AiOutlineLeft/></button>

                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={(e) => {
                            e.preventDefault();
                            if(idx < 60){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(idx)+1});
                            }
                            else {
                                if(taskId < last_taskId) {
                                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskId)+1 });
                                    context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(1)});
                                }
                                else{
                                    alert('마지막입니다.');
                                }
                            }
                        }}
                    ><AiOutlineRight/></button>
                </div>
            </div>
            

            {/* io input form */}
            <form>
                <Table>
                    <TableBody>
                        <TableRow>
                            <TableHead>{`입력 ${idx}`}</TableHead>
                            <TableCell style={{border: `1px solid var(--line-color)`, padding: `0.5em 1em`}}>
                                <TextArea input={input} setInput={handleChangeInput} placeholder={`입력을 작성하세요.`} style={{paddingRight: `0`, paddingLeft: `0`,}}/>
                            </TableCell>
                        </TableRow>
                        <TableRow>
                            <TableHead>{`출력 ${idx}`}</TableHead>
                            <TableCell style={{border: `1px solid var(--line-color)`, padding: `0.5em 1em`}}>
                                <TextArea input={output} setInput={handleChangeOutput} placeholder={`출력을 작성하세요.`} style={{paddingRight: `0`, paddingLeft: `0`,}}/>
                            </TableCell>
                        </TableRow>
                    </TableBody>
                </Table>

                <div className={styles.buttons}>
                    <div className={styles.btnWrapper}>
                        <button onClick={handleSave} className={`${styles.button} noDrag`}>저장</button>
                        <button onClick={handleSaveAndLoad} className={`${styles.button} noDrag`}>저장하고 다음으로 이동</button>
                    </div>
                </div>
            </form>
        </div>
    );
}