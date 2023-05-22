import React, { useContext } from 'react';
import styles from './InstHeader.module.css';
import { SET_INST_TASKID, SET_SUB_IDX, userContext } from '../../context/UserContext';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import InputNumber from '../ui/input/InputNumber';

export default function InstHeader() {
    const context = useContext(userContext);

    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const subIdx = context.state.data.sub_idx;

    const handleTaskId = (value) => {
        context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(value) })
    }
    const handleSubIdx = (value) => {
        context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(value) });
    }

    return (
        <div className={styles.wrapper}>
            <div className={styles.header}>
                <form>
                    {/* task index(assigned task) */}
                    <label className='noDrag'>task: </label>
                    <InputNumber 
                        context={taskId}
                        setContext={handleTaskId}
                        maxNum={last_taskId}
                        minNum={first_taskId}
                    />
                    {/* definition index */}
                    <label className='noDrag'>sub index: </label>
                    <InputNumber 
                        context={subIdx}
                        setContext={handleSubIdx}
                        maxNum={12}
                        minNum={1}
                    />
                </form>
            </div>
            <div className={styles.moveBtns}>
                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(subIdx > 1){ //이동 가능한 상태
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subIdx)-1});
                        }
                        else {
                            if(taskId > first_taskId){
                                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskId)-1 });
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(10)) });
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
                        if(subIdx < 10){
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subIdx)+1});
                        }
                        else {
                            if(taskId < last_taskId) {
                                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskId)+1 });
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(1)) });
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

