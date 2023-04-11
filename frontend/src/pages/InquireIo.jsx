import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import { userContext } from '../context/UserContext';
import styles from './InquireIo.module.css';

import { BsCaretRightFill } from 'react-icons/bs';
import ShowIo from '../components/inquire-inst/ShowIo';

export default function InquireIo() {
    // taskNum 내부 state를 사용하여 definition 작업에 영향을 미치지 않도록
    const context = useContext(userContext);

    const taskId = context.state.data.io_taskId; // 작업하던 taskId
    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;

    // 조회가 지시문과 입출력에 영향을 주지 않도록 taskId context를 사용하지 않음
    const [taskNum, setTaskNum] = useState(taskId); // 데이터 로드용
    const [inputNum, setInput] = useState(taskId); // input 관리용

    const [defData, setDef] = useState();
    const [originalDefData, setOriginal] = useState();

    // toggle
    const [isOpen, setOpen] = useState(true);

    useEffect(() => {
        axios.get(`/api/tasks/${taskNum}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setOriginal(data);
        })
    }, [taskNum]);

    useEffect(() => {
        axios.get(`/api/tasks/${taskNum}/definitions`)
        .then(function(res) {
            setDef(res.data.data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }, [taskNum]);

    const handlePressEnter = (e) => {
        if(e.key === "Enter") {
            const value = e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(value));
            // context.actions.contextDispatch({ type: SET_INST_TASKID, data: taskNum});
        }
    }

    return (
        <div className='body'>
            <form
                className={styles.form}
                onSubmit={(e) => e.preventDefault()}>
                <label className={styles.label}>task: </label>
                <input 
                    className={styles.input}
                    type="number"
                    id="task"
                    onChange={(e) => {
                        const value = e.target.value;
                        value >= first_taskId && value <= last_taskId && setInput(parseInt(e.target.value))
                    }}
                    max={`${last_taskId}`}
                    min={`${first_taskId}`}
                    // defaultValue={taskId}
                    value={inputNum}
                    onKeyDown={handlePressEnter}
                    // onBlur={handleOnBlur}
                />
                <p style={{
                    color: "var(--light-main-color)", 
                    fontSize: "14px",
                    marginLeft: "1em",
                }}>✓ 엔터를 누르면 조회됩니다.</p>
            </form>
            <div className={styles.toggle}>
                <div className={styles.toggleTitle}>
                    <BsCaretRightFill 
                        className={isOpen ? styles.open : ''}
                        onClick={() => setOpen(prev => !Boolean(prev))}    
                    />
                    <h3 style={isOpen ? {color: "var(--txt-color)"} : {}}>지시문</h3>
                </div>
                <div className={styles.toggleBody}>
                    {isOpen ? <Directive defData={defData} originalDefData={originalDefData}/> : ''}
                </div>
            </div>
            <ShowIo taskNum={taskNum}/>
        </div>
    );
}

