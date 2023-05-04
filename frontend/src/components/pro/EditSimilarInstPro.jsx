import React, { useContext, useEffect, useRef, useState } from 'react';
import { SET_INST_TASKID, SET_TASKNAME, userContext } from '../../context/UserContext';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditSimilarInstPro.module.css';
// import { TbArrowNarrowLeft, TbArrowNarrowRight } from 'react-icons/tb';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

import axios from 'axios';

export default function EditSimilarInstPro() {
    const context = useContext(userContext);
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    // const userId = GetUserId(context.state.data.name);
    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const subIdx = context.state.data.sub_idx;

    // 내부 관리용 taskId state
    const [taskNum, setTaskNum] = useState(() => taskId);
    // const [subNum, setSubNum] = useState(1);

    // ref
    const taskNumRef = useRef();

    const handleChange1 = (value) => {
        setInput1(value);
    };
    const handleChange2 = (value) => {
        setInput2(value);
    };

    const handleLoad = (e) => {
        axios.get(`/api/admin/tasks/${taskNum}/assignment`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setInput1(data.similarInstruct1);
            setInput2(data.similarInstruct2);
            context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum)});
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
        axios.patch(`/api/admin/tasks/${taskNum}/assignment`, {
            similarInstruct1: `${input1}`,
            similarInstruct2: `${input2}`,
        })
        .then(function(res) {
            if(taskNum < 120){
                // input 창의 내용을 새로 받기 위해서 비워줌
                setInput1('');
                setInput2('');
                // 다음 subIdx로 state 초기화
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: (parseInt(taskNum) +1) });
                // taskNum 상승
                setTaskNum(prev => parseInt(prev) + 1);
            }
            else if(taskNum >= 120){
                alert('마지막 지시문입니다.');
            }
        })
        .catch(function(err) {
            console.log(err);
        })
    }
    const handleSave = async (e) => {
        e.preventDefault();
        axios.patch(`/api/admin/tasks/${taskNum}/assignment`, {
            similarInstruct1: `${input1}`,
            similarInstruct2: `${input2}`,
        })
        .then(function() {
            window.location.reload();
        })
        .catch(function(err) {
            alert('저장되지 않았습니다.');
        })
    }

    const handlePressEnter = (e) => {
        if(e.key === "Enter") {
            e.preventDefault();
            const id = e.target.id;
            
            if(id === "task") {
                const value= e.target.value;
                value >= first_taskId && value <=last_taskId && setTaskNum(value);
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum) });
            }
            handleLoad(e);
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "task") {
            const value = e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(value);
        }
    }

    useEffect(() => {
        handleLoad();
    }, [taskId]);
    
    return (
        <>
            <div className={styles.wrapper}>
                <div className={styles.header}>
                    <p className={styles.title}>* 다음 유사 지시문 2개를 작성하시오.</p>
                    <form>
                        {/* task index(assigned task) */}
                        <label className='noDrag'>task: </label>
                        <input 
                            ref={taskNumRef}
                            type="number"
                            id="task"
                            onChange={(e) => {
                                const value = e.target.value;
                                value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                            }}
                            max="120"
                            min="1"
                            value={taskNum}
                            onKeyDown={handlePressEnter}
                            onBlur={handleOnBlur}
                            onClick={() => {
                                taskNumRef.current.select();
                            }}
                        />
                        <span style={{ 
                            color: `var(--red-color)`, 
                            fontSize: `12px`, 
                            marginLeft: `1em`,
                            lineHeight: `1.5em`,
                        }}>
                            ⚠ 엔터를 누르면 저장되지 않고 이동합니다.
                        </span>
                    </form>
                </div>
            </div>

            <div className={styles.edit}>
                <form>
                    <TextArea input={input1} setInput={handleChange1} placeholder={`유사 지시문 ${subIdx}을 입력하세요.`}/>
                    <div className={styles.divider}/>
                    <TextArea input={input2} setInput={handleChange2} placeholder={`유사 지시문 2를 입력하세요.`}/>
                </form>
            </div>

            <div className={styles.buttons}>
                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(taskNum > 1){
                            context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum)-1});
                            setTaskNum(prev => parseInt(prev)-1);
                        }
                        else {
                            alert('첫 지시문입니다.');
                        }
                    }}    
                ><AiOutlineLeft/>이전</button>

                <div className={styles.btnWrapper}>
                    <button onClick={handleSave} className={`${styles.button} noDrag`}>저장</button>
                    <button onClick={handleSaveAndLoad} className={`${styles.button} noDrag`}>저장하고 다음 페이지로 이동</button>    
                </div>
                
                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(taskNum < 120){
                            context.actions.contextDispatch({ type: SET_INST_TASKID, data: parseInt(taskNum)+1});
                            setTaskNum(prev => parseInt(prev)+1);
                        }
                        else {
                            alert('마지막 지시문입니다.');
                        }
                    }}
                >다음<AiOutlineRight/></button>
            </div>
        </>
    );
}

