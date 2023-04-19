import React, { useContext, useEffect, useRef, useState } from 'react';
import styles from './InspectInstruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_IO_IDX, SET_IO_TASKID, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

export default function InspectInstruction() {
    const context = useContext(userContext);

    // const subIdx = context.state.data.sub_idx;
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const ioIdx = context.state.data.io_idx;

    // state
    const [data, setData] = useState();
    const [taskNum, setTaskNum] = useState(taskId);
    const [subNum, setSubNum] = useState((ioIdx % 10) == 0 ? 10 : ioIdx % 10);
    const [taskIdx, setIdx] = useState(1);

    // ref
    const taskNumRef = useRef();
    const taskIdxRef = useRef();
    
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

    const handlePressEnter = (e) => {
        const id = e.target.id;
        if(e.key === "Enter"){
            if(id === "task") {
                const value= e.target.value;
                value >=first_taskId && value <=last_taskId && setTaskNum(value);
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskNum)});
                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
                handleLoad(e);
            }
            if(id === "idx") {
                const value= e.target.value;
                value >=1 && value <=60 && setIdx(value);
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskNum)});
                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
                handleLoad(e);
            }
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value= e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(value));
        }
        if(id === "idx") {
            const value= e.target.value;
            value >= 1 && value <= 60 && setIdx(parseInt(value));
        }
    }
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
        setTaskNum(taskId);
        setSubNum((ioIdx % 10) == 0 ? 10 : ioIdx % 10);
        setIdx(ioIdx);
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
                        <input 
                            className={styles.input}
                            ref={taskNumRef}
                            type="number"
                            id="task"
                            onChange={(e) => {
                                const value = parseInt(e.target.value);
                                value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                            }}
                            max={`${last_taskId}`}
                            min={`${first_taskId}`}
                            value={taskNum}
                            onKeyDown={handlePressEnter}
                            onBlur={handleOnBlur}
                            onClick={() => {
                                taskNumRef.current.select();
                            }}
                        />

                        <label className={`noDrag ${styles.label}`}>index: </label>
                        <input 
                            className={styles.input}
                            ref={taskIdxRef}
                            type="number"
                            id="idx"
                            onChange={(e) => {
                                const value = parseInt(e.target.value);
                                value >=1 && value <=60 && setIdx(parseInt(e.target.value));
                            }}
                            max="60"
                            min="1"
                            value={taskIdx}
                            onKeyDown={handlePressEnter}
                            onBlur={handleOnBlur}
                            onClick={() => {
                                taskIdxRef.current.select();
                            }}
                        />
                        {/* <p 
                            className='noDrag'
                            style={{
                                color: "var(--light-main-color)", 
                                fontSize: "14px",
                                marginLeft: "1em",
                        }}>✓ 엔터를 누르면 조회됩니다.</p> */}
                    </form>
                </div>

                <div className={styles.moveBtns}>
                    <button
                        className={`${styles.moveBtn} noDrag`}
                        onClick={(e) => {
                            e.preventDefault();
                            if(taskIdx > 1){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)-1});
                                setIdx(prev => parseInt(prev)-1);
                            }
                            else {
                                if(taskId > first_taskId) {
                                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskId)-1 });
                                    context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(60)});
                                    setIdx(60);
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
                            if(taskIdx < 60){
                                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)+1});
                                setIdx(prev => parseInt(prev)+1);
                            }
                            else {
                                if(taskId < last_taskId) {
                                    context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskId)+1 });
                                    context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(1)});
                                    setIdx(1);
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
        </div>
    );
}