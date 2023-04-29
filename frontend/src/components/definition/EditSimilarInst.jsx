import React, { useContext, useEffect, useRef, useState } from 'react';
import { SET_INST_TASKID, SET_SUB_IDX, SET_TASKNAME, userContext } from '../../context/UserContext';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditSimilarInst.module.css';
// import { TbArrowNarrowLeft, TbArrowNarrowRight } from 'react-icons/tb';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

import axios from 'axios';
import InputNumber from '../ui/input/InputNumber';

export default function EditSimilarInst() {
    const context = useContext(userContext);
    const [input1, setInput1] = useState('');

    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const subIdx = context.state.data.sub_idx;

    // 내부 관리용 taskId state
    const handleTaskId = (value) => {
        context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(value) })
    }
    const handleSubIdx = (value) => {
        context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(value) });
    }
    const handleChange1 = (value) => {
        setInput1(value);
    };

    const handleLoad = (e) => {
        axios.get(`/api/tasks/${taskId}/assignment/${subIdx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setInput1(data.similarInstruct1);
            context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskId)});
            context.actions.contextDispatch({ type: SET_TASKNAME, data: data.taskTitle});
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }
    const handleSaveAndLoad = (e) => {
        e.preventDefault();
        if(!taskId || taskId == 0 || taskId > last_taskId || taskId < first_taskId){
            alert('task id 확인 후 다시 제출해주세요.');
            return;
        }

        axios.patch(`/api/tasks/${taskId}/assignment/${subIdx}`, {
            similarInstruct1: `${input1}`,
            taskSubIdx: subIdx,
        })
        .then(function(res) {
            if(subIdx < 10){
                // input 창의 내용을 새로 받기 위해서 비워줌
                setInput1('');
                // 다음 subIdx로 state 초기화
                context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(subIdx) + 1) });
            }
            else if(subIdx >= 10){
                if(taskId < last_taskId) {
                    // input 창의 내용을 새로 받기 위해서 비워줌
                    setInput1('');
                    // state 초기화
                    context.actions.contextDispatch({ type: SET_INST_TASKID, data: (parseInt(parseInt(taskId)+1)) });
                    context.actions.contextDispatch({ type: SET_SUB_IDX, data: (parseInt(1)) });
                }
                else {
                    alert('마지막입니다.');
                }
            }
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('task id 혹은 sub index를 확인해주세요.');
            }
        })
    }
    const handleSave = async (e) => {
        e.preventDefault();
        if(!taskId || taskId == 0 || taskId > last_taskId || taskId < first_taskId){
            alert('task id 확인 후 다시 제출해주세요.');
            return;
        }

        axios.patch(`/api/tasks/${taskId}/assignment/${subIdx}`, {
            similarInstruct1: `${input1}`,
            taskSubIdx: subIdx,
        })
        .catch(function(err) {
            alert('저장되지 않았습니다.');
        })
    }

    useEffect(() => {
        handleLoad();
    }, [taskId, subIdx]);

    
    return (
        <>
            <div className={styles.wrapper}>
                <div className={styles.header}>
                    <p className={styles.title}>* 유사 지시문(1~10)을 작성하시오.</p>
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
                            maxNum={10}
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

            <div className={styles.edit}>
                <form>
                    <TextArea input={input1} setInput={handleChange1} placeholder={`유사 지시문 ${subIdx}을 입력하세요.`}/>
                </form>
            </div>

            <div className={styles.buttons}>
                <div className={styles.btnWrapper}>
                    <button onClick={handleSave} className={`${styles.button} noDrag`}>저장</button>
                    <button onClick={handleSaveAndLoad} className={`${styles.button} noDrag`}>저장하고 다음으로 이동</button>    
                </div>
            </div>
        </>
    );
}

