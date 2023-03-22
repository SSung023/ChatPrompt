import React, { useContext, useEffect, useState } from 'react';
import { SET_INST_TASKID, userContext } from '../../context/UserContext';
import { GetUserId } from '../../utility/GetUserId';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditDirective.module.css';

import axios from 'axios';

export default function EditDirective() {
    const context = useContext(userContext);
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    // const userId = GetUserId(context.state.data.name);
    const taskId = context.state.data.inst_taskId;
    const [taskNum, setTaskNum] = useState(taskId);

    const handleChange1 = (value) => {
        setInput1(value);
    };
    const handleChange2 = (value) => {
        setInput2(value);
    };

    // task 별 인풋 로드해서 초기화하기
    // const saveInstruction = async () => {
    //     axios.patch(`/api/tasks/${taskNum}/instruction`, {
    //         newDefinition: `${input1}`
    //     })
    //     .then(function(res) {
    //         if(taskNum < 120){
    //             // 다음 task로 state 초기화
    //             context.actions.contextDispatch({ type: SET_INST_TASKID, data: (parseInt(taskNum)+1)});
    //             setTaskNum(prev => parseInt(prev) + 1);
    //         }
    //         else if(taskNum >=120){
    //             alert('마지막 태스크입니다!');
    //         }
    //     })
    //     .catch(function(err) {
    //         console.log(err);
    //     })
    // }
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
    }
    const handleSaveAndLoad = (e) => {
        // console.log(`taskId: ${taskId}, taskNum: ${taskNum}`);
        // save
        axios.patch(`/api/tasks/${taskNum}/assignment`, {
            similarInstruct1: `${input1}`,
            similarInstruct2: `${input2}`
        })
        .then(function(res) {
            if(taskNum < 120){
                // input 창의 내용을 새로 받기 위해서 비워줌
                setInput1('');
                setInput2('');
                // 다음 task로 state 초기화
                context.actions.contextDispatch({ type: SET_INST_TASKID, data: (parseInt(taskNum)+1)});
                // taskNum 상승
                setTaskNum(prev => parseInt(prev) + 1);
            }
            else if(taskNum >=120){
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
            value >=1 && value <=120 && setTaskNum(value);
            handleLoad(e);
        }
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
                            value >=1 && value <=120 && setTaskNum(parseInt(e.target.value))
                        }}
                        max="120"
                        min="1"
                        value={taskNum}
                        onKeyDown={handlePressEnter}
                    />
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
                {/* {(userId === 1 || userId === 2 || userId === 3) 
                && <button onClick={saveInstruction}>교수님 전용 윤문 수정 버튼</button>} */}
                {/* <button onClick={handleLoad}>저장 없이 왼쪽 지정 페이지로 이동</button> */}
                <button onClick={handleSaveAndLoad}>저장하고 다음 페이지로 이동</button>
            </div>
        </>
    );
}

