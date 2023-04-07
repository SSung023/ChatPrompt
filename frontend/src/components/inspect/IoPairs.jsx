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
    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;

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
        taskId && axios.get(`/api/verifications/tasks/${taskId}/io/lists`)
        .then(function(res) {
            return res.data.dataList;
        })
        .then(function(data) {
            setIo(data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
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
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value= e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(value));
        }
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
                    <label className={`${styles.label} noDrag`}>task: </label>
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
            <Table>
                <TableBody>
                    {makeIoInspect}
                </TableBody>
            </Table>
        </div>
    );
}

