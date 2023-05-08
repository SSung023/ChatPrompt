import React, { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import styles from './InspectInstruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_INST_TASKID, SET_IO_IDX, SET_IO_TASKID, SET_SUB_IDX, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import InputNumber from '../ui/input/InputNumber';

export default function InspectInstruction() {
    const navigate = useNavigate();

    const context = useContext(userContext);

    // const subIdx = context.state.data.sub_idx;
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const ioIdx = context.state.data.io_idx;

    // state
    const [data, setData] = useState();
    const [subNum, setSubNum] = useState((ioIdx % 10) == 0 ? 10 : ioIdx % 10);

    const handleTaskId = (value) => {
        context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(value) });
    }
    const handleIdx = (value) => {
        context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(value) });
    }
    
    const handleLoad = () => {
        // console.log('load');
        axios.get(`/api/tasks/${taskId}/assignment/${(ioIdx % 10) == 0 ? 10 : ioIdx % 10}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setData(data);
            context.actions.contextDispatch({ type: SET_TASKNAME, data: data.taskTitle});
        })
    }
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
        setSubNum((ioIdx % 10) == 0 ? 10 : ioIdx % 10);
    }, [taskId, ioIdx]);

    return (
        data &&
        <div className={styles.instruction}>
            <div className={styles.wrapper}>
                <div className={styles.header}>
                    <p className={styles.title}>* 지시문에 대한 입력과 출력이 적절한지 검사하시오.</p>

                    <form
                        className={styles.form}
                        onSubmit={(e) => {e.preventDefault()}}
                    >
                        <label className={`noDrag ${styles.label}`}>task: </label>
                        <InputNumber 
                            context={taskId}
                            setContext={handleTaskId}
                            maxNum={last_taskId}
                            minNum={first_taskId}
                        />

                        <label className={`noDrag ${styles.label}`}>index: </label>
                        <InputNumber 
                            context={ioIdx}
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
                            if(ioIdx > 1){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(ioIdx)-1});
                            }
                            else {
                                if(taskId > first_taskId) {
                                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskId)-1 });
                                    context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(60)});
                                }
                                else {
                                    alert('첫 입출력입니다.');
                                }
                            }
                        }}
                    ><AiOutlineLeft/></button>
                    
                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={(e) => {
                            e.preventDefault();
                            if(ioIdx < 60){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(ioIdx)+1});
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
            
            {/* show similar instruct */}
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>{`유사\n지시문 ${subNum}`}</TableHead>
                        {/* <TableHead>{`${subIdx}번`}</TableHead> */}
                        <TableCell>{data.similarInstruct1 ? data.similarInstruct1 : <span style={{color: "var(--placeholder-txt-color)"}} className="noDrag">작성한 지시문이 없습니다.</span>}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>

            {/* hot key */}
            <p 
                className={styles.modifyBtn}
                onClick={() => {
                    context.actions.contextDispatch({ type: SET_INST_TASKID, data: taskId });
                    context.actions.contextDispatch({ type: SET_SUB_IDX, data: subNum });
                    navigate('../');
                }}    
            >{`지시문 수정하러 가기 >`}</p>
        </div>
    );
}