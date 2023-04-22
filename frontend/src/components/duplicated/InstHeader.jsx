import React, { useContext, useEffect, useRef, useState } from 'react';
import styles from './InstHeader.module.css';
import { SET_INST_TASKID, SET_SUB_IDX, userContext } from '../../context/UserContext';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

export default function InstHeader() {
    const context = useContext(userContext);

    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const subIdx = context.state.data.sub_idx;

    // state
    const [taskNum, setTaskNum] = useState(taskId);
    const [subNum, setSubNum] = useState(subIdx);

    // ref
    const taskNumRef = useRef();
    const subNumRef = useRef();

    // dom events
    const handlePressEnter = (e) => {
        if(e.key === "Enter") {
            e.preventDefault();
            const id = e.target.id;
            
            if(id === "task") {
                const value= e.target.value;
                value >= first_taskId && value <=last_taskId && setTaskNum(parseInt(value));
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum) });
                // context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum) });
            }
            else if(id === "subIdx") {
                const value= e.target.value;
                value >= 1 && value <= 10 && setSubNum(parseInt(value));
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum) });
                context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum) });
            }
            // handleLoad(e);
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value = e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(value));
        }
        else if(id === "subIdx") {
            const value = e.target.value;
            value >= 1 && value <= 10 && setSubNum(parseInt(value));
        }
    }

    useEffect(() => {
        setTaskNum(taskId);
    }, [taskId]);

    useEffect(() => {
        setSubNum(subIdx);
    }, [subIdx]);

    return (
        <div className={styles.header}>
            <form 
                className={styles.form}
                onSubmit={(e) => e.preventDefault}    
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
                    onClick={ () => {
                        taskNumRef.current.select();
                    }}
                />
                {/* definition index */}
                <label className='noDrag'>sub index: </label>
                <input 
                    ref={subNumRef}
                    type="number"
                    id="subIdx"
                    onChange={(e) => {
                        const value = parseInt(e.target.value);
                        value >= 1 && value <= 10 && setSubNum(parseInt(e.target.value))
                    }}
                    max="10"
                    min="1"
                    value={subNum}
                    onKeyDown={handlePressEnter}
                    onBlur={handleOnBlur}
                    onClick={() => {
                        subNumRef.current.select();
                    }}
                />
            </form>

            <div className={styles.buttons}>
                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(subNum > 1){ //이동 가능한 상태
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum)-1});
                            setSubNum(prev => parseInt(prev)-1);
                        }
                        else {
                            if(taskNum > first_taskId){
                                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum)-1 });
                                setTaskNum(prev => parseInt(prev) - 1);
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(10)) });
                                setSubNum(10);
                            }
                            else{
                                alert('첫 지시문입니다.');
                            }
                        }
                    }}    
                ><AiOutlineLeft/></button>

                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(subNum < 10){
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum)+1});
                            setSubNum(prev => parseInt(prev)+1);
                        }
                        else {
                            if(taskNum < last_taskId) {
                                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum)+1 });
                                setTaskNum(prev => parseInt(prev) + 1);
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(1)) });
                                setSubNum(1);
                            }
                            else{
                                alert('마지막입니다.');
                            }
                        }
                    }}
                ><AiOutlineRight/></button>
            </div>
        </div>
    );
}

