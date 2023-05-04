import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import { userContext } from '../context/UserContext';
import styles from './InquireIo.module.css';

import { BsCaretRightFill } from 'react-icons/bs';
import ShowIo from '../components/inquire-inst/ShowIo';
import InputNumber from '../components/ui/input/InputNumber';

export default function InquireIo() {
    // taskNum 내부 state를 사용하여 definition 작업에 영향을 미치지 않도록
    const context = useContext(userContext);

    const taskId = context.state.data.io_taskId; // 작업하던 taskId
    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;

    // 조회가 지시문과 입출력에 영향을 주지 않도록 taskId context를 사용하지 않음
    const [taskNum, setTaskNum] = useState(taskId); // 데이터 로드용
    const handleTaskNum = (value) => {
        setTaskNum(parseInt(value));
    }

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

    return (
        <div className='body'>
            <form
                className={styles.form}
                onSubmit={(e) => e.preventDefault()}>
                <label className={styles.label}>task: </label>
                <InputNumber 
                    context={taskNum}
                    setContext={handleTaskNum}
                    maxNum={last_taskId}
                    minNum={first_taskId}
                />
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
            <ShowIo taskNum={taskNum} setTaskNum={handleTaskNum}/>
        </div>
    );
}

