import axios from 'axios';
import React, { useContext, useEffect, useMemo, useRef, useState } from 'react';
import { SET_IO_TASKID, userContext } from '../../context/UserContext';
import Table, { TableBody, TableRow } from '../ui/table/Table';
import IoInspect from './IoInspect';
import styles from './IoPairs.module.css';

export default function IoPairs() {
    const context = useContext(userContext);

    // store 정보
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;

    // 내부 관리용
    const [taskNum, setTaskNum] = useState(taskId);
    const [io, setIo] = useState({});

    // ref
    const taskNumRef = useRef();
    // const taskIdxRef = useRef();

    const makeIoInspect = useMemo(() => {
        return (
            io && Object.values(io).map((data, idx) => {
                return (
                    <IoInspect data={data} idx={idx} key={idx}/>
                )
            })
        )
    }, [io]);

    const handleLoad = (e) => {
        console.log(taskId);
        taskId && axios.get(`/api/tasks/${taskId}/assignment-io/lists`)
        .then(function(res) {
            return res.data.dataList;
        })
        .then(function(data) {
            // console.log(data);
            setIo(data);
        });
    }

    // task, index 관리
    const handlePressEnter = (e) => {
        const id = e.target.id;
        if(e.key === "Enter"){
            if(id === "task") {
                const value= e.target.value;
                value >= first_taskId && value <= last_taskId && setTaskNum(value);
                context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskNum)});
                // context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
                handleLoad(e);
            }
            // else if(id === "idx") {
            //     const value= e.target.value;
            //     value >=1 && value <=60 && setIdx(value);
            //     context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
            //     context.actions.contextDispatch({ type: SET_IO_TASKID, data: parseInt(taskNum)});
            //     handleLoad(e);
            // }
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value= e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(value));
        }
        // else if(id === "idx") {
        //     const value= e.target.value;
        //     value >= 1 && value <= 60 && setIdx(parseInt(value));
        //     // e.target.style.opacity="0"
        // }
    }

    useEffect(() => {
        // 입출력 작성 폼에 불러오기
        handleLoad();
        setTaskNum(taskId);
    }, [taskId]);

    return (
        <div>
            <div className={styles.header}>
                <p className={styles.title}>* 위 지시문에 대한 입력과 출력이 적절한지 검사하시오.</p>
                <form
                    className={styles.ioForm}
                    onSubmit={(e) => {e.preventDefault()}}
                >
                    <label className='noDrag'>task: </label>
                    <input 
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

                    {/* <label className='noDrag'>index: </label>
                    <input 
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
                    /> */}
                    <span 
                        style={{
                            color: `#e02b2b`,
                            fontSize: `12px`,
                            marginLeft: `1em`,
                            lineHeight: `1.5em`,
                        }}
                        className='noDrag'
                    >
                        ⚠ 엔터를 누르면 저장되지 않고 이동합니다.
                    </span>
                </form>
                
                <Table>
                    <TableBody>
                        {makeIoInspect}
                    </TableBody>
                </Table>
            </div>
        </div>
    );
}

