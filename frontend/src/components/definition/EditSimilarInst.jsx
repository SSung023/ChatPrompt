import React, { useContext, useEffect, useState } from 'react';
import { SET_INST_TASKID, userContext } from '../../context/UserContext';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditDirective.module.css';

import axios from 'axios';

export default function EditSimilarInst() {
    const context = useContext(userContext);
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    // const userId = GetUserId(context.state.data.name);
    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    // 내부 관리용 taskId state
    const [taskNum, setTaskNum] = useState(taskId);

    const handleChange1 = (value) => {
        setInput1(value);
    };
    const handleChange2 = (value) => {
        setInput2(value);
    };

    const handleLoad = (e) => {
        axios.get(`/api/tasks/${taskNum}/assignment`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setInput1(data.similarInstruct1);
            setInput2(data.similarInstruct2);
            context.actions.contextDispatch({ type: SET_INST_TASKID, data: taskNum});
        })
        .catch(function(err) {
            if(err.response.status === 400){
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }
    const handleSaveAndLoad = (e) => {
        // console.log(`taskId: ${taskId}, taskNum: ${taskNum}`);
        // save
        axios.patch(`/api/tasks/${taskNum}/assignment`, {
            similarInstruct1: `${input1}`,
            similarInstruct2: `${input2}`
        })
        .then(function(res) {
            if(taskNum < last_taskId){
                // input 창의 내용을 새로 받기 위해서 비워줌
                setInput1('');
                setInput2('');
                // 다음 task로 state 초기화
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: (parseInt(taskNum)+1)});
                // taskNum 상승
                setTaskNum(prev => parseInt(prev) + 1);
            }
            else if(taskNum >=last_taskId){
                alert('마지막 태스크입니다!');
            }
        })
        .catch(function(err) {
            console.log(err);
        })
    }
    const handlePressEnter = (e) => {
        if(e.key === "Enter"){
            const value= e.target.value;
            value >= first_taskId && value <=last_taskId && setTaskNum(value);
            handleLoad(e);
        }
    }
    const handleOnBlur = (e) => {
        const value = e.target.value;
        value >= first_taskId && value <= last_taskId && setTaskNum(value);
    }

    useEffect(() => {
        // console.log(taskId);
        handleLoad();
    }, [taskId]);
    
    return (
        <>
            <div className={styles.wrapper}>
                <div className={styles.left}>
                    <p className={styles.title}>* 다음 유사 지시문 2개를 작성하시오.</p>
                    <label>task: </label>
                    <input 
                        type="number"
                        onChange={(e) => {
                            const value = e.target.value;
                            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                        }}
                        max="120"
                        min="1"
                        value={taskNum}
                        onKeyDown={handlePressEnter}
                        onBlur={handleOnBlur}
                    />
                    <p style={{ 
                        color: `var(--light-txt-color)`, 
                        fontSize: `12px`, 
                        marginLeft: `1em`,
                        lineHeight: `1.5em`,
                    }}>
                        ⚠ 엔터를 누르면 저장되지 않고 이동합니다.
                    </p>
                </div>
            </div>
            <div className={styles.edit}>
                <form>
                    <TextArea input={input1} setInput={handleChange1} placeholder={`유사 지시문 1을 입력하세요.`}/>
                    <div className={styles.divider}/>
                    <TextArea input={input2} setInput={handleChange2} placeholder={`유사 지시문 2를 입력하세요.`}/>
                </form>
            </div>
            <div className={styles.buttons}>
                <button onClick={handleSaveAndLoad}>저장하고 다음 페이지로 이동</button>
            </div>
        </>
    );
}

