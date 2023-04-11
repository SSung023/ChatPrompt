import React, { useContext, useEffect, useRef, useState } from 'react';
import styles from './Instruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_IO_TASKID, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';

export default function Instruction() {
    const context = useContext(userContext);

    // const subIdx = context.state.data.sub_idx;
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;
    const ioIdx = context.state.data.io_idx;

    // state
    const [data, setData] = useState();
    const [taskNum, setTaskNum] = useState(taskId);
    const [subNum, setSubNum] = useState((ioIdx % 10) == 0 ? 10 : ioIdx % 10);

    // ref
    const taskNumRef = useRef();
    
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
                // context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskNum)});
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
    }
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
        setTaskNum(taskId);
        setSubNum((ioIdx % 10) == 0 ? 10 : ioIdx % 10);
    }, [taskId, ioIdx]);

    return (
        data &&
        <div className={styles.instruction}>
            <div className={styles.header}>
                <p className={styles.title}>* 내가 쓴 지시문</p>

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
                    <p 
                        className='noDrag'
                        style={{
                            color: "var(--light-main-color)", 
                            fontSize: "14px",
                            marginLeft: "1em",
                    }}>✓ 엔터를 누르면 조회됩니다.</p>
                </form>
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