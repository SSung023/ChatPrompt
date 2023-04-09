import axios from 'axios';
import React, { useContext, useEffect, useMemo, useRef, useState } from 'react';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { SET_IO_IDX, SET_IO_TASKID, userContext } from '../../context/UserContext';
import Table, { TableBody, TableRow } from '../ui/table/Table';
import IoInspect from './IoInspect';
import styles from './IoPairs.module.css';

export default function IoPairs() {
    const context = useContext(userContext);

    // store 정보
    const taskId = context.state.data.io_taskId;
    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;
    const idx = context.state.data.io_idx;

    // 내부 관리용
    const [taskIdx, setIdx] = useState(1);
    const [io, setIo] = useState({});

    // ref
    const taskIdxRef = useRef();

    const handleLoad = (e) => {
        taskId && axios.get(`/api/verifications/tasks/${taskId}/io/${idx}`)
        .then(function(res) {
            return res.data.data;
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
            if(id === "idx") {
                const value= e.target.value;
                value >=1 && value <=60 && setIdx(value);
                context.actions.contextDispatch({ type: SET_IO_IDX, data: parseInt(taskIdx)});
                handleLoad(e);
            }
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "idx") {
            const value= e.target.value;
            value >= 1 && value <= 60 && setIdx(parseInt(value));
        }
    }

    useEffect(() => {
        // 입출력 작성 폼에 불러오기
        handleLoad();
        setIdx(idx);
    }, [taskId, idx]);

    return (
        <div>
            <div className={styles.header}>
                <p className={styles.title}>* 위 지시문에 대한 입력과 출력이 적절한지 검사하시오.</p>
                <form
                    className={styles.ioForm}
                    onSubmit={(e) => {e.preventDefault()}}
                >
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
                </form>
            </div>
            <Table>
                <TableBody>
                    <IoInspect data={io} idx={idx}/>
                </TableBody>
            </Table>

            <div className={styles.buttons}>
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
                ><AiOutlineLeft/>이전</button>
                
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
                >다음<AiOutlineRight/></button>
            </div>
        </div>
    );
}
