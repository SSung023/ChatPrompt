import axios from 'axios';
import React, { useContext, useEffect, useState } from 'react';
import Directive from '../components/definition/Directive';
import ShowInst from '../components/inquire-inst/ShowInst';
import { SET_INST_TASKID, userContext } from '../context/UserContext';
import styles from './InquireInst.module.css';

import { BsCaretRightFill } from 'react-icons/bs';

export default function InquireInst() {
    // taskNum 내부 state를 사용하여 definition 작업에 영향을 미치지 않도록
    const context = useContext(userContext);

    const taskId = context.state.data.inst_taskId;
    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;
    const [taskNum, setTaskNum] = useState(taskId);

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
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }, [taskNum]);

    const handlePressEnter = (e) => {
        if(e.key === "Enter") {
            const value = e.target.value;
            value >= first_taskId && value <= last_taskId && setTaskNum(value);
            // context.actions.contextDispatch({ type: SET_INST_TASKID, data: taskNum});
        }
    }
    // const handleOnBlur = (e) => {
    //     const value = e.target.value;
    //     value >= first_taskId && value <= last_taskId && setTaskNum(value);
    // }

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
                    // onChange={(e) => {
                    //     const value = e.target.value;
                    //     value >= first_taskId && value <= last_taskId && setTaskNum(parseInt(e.target.value))
                    // }}
                    max="120"
                    min="1"
                    defaultValue={taskId}
                    // value={taskNum}
                    onKeyDown={handlePressEnter}
                    // onBlur={handleOnBlur}
                />
            </form>
            <div className={styles.toggle}>
                <div className={styles.toggleTitle}>
                    <BsCaretRightFill 
                        className={isOpen ? styles.open : ''}
                        onClick={() => setOpen(prev => !prev)}    
                    />
                    <h3 style={isOpen ? {color: "var(--txt-color)"} : {}}>지시문</h3>
                </div>
                <div className={styles.toggleBody}>
                    {isOpen ? <Directive defData={defData} originalDefData={originalDefData}/> : ''}
                </div>
            </div>
            <ShowInst taskNum={taskNum}/>
        </div>
    );
}