import React, { useContext, useEffect, useState } from 'react';
import { SET_TASKID, userContext } from '../../context/UserContext';
import { FormattedTaskID, UnformattedTaskId } from '../../utility/FormattedTaskId';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditDirective.module.css';

export default function EditDirective() {
    const context = useContext(userContext);
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    const [taskNum, setTaskNum] = useState(context.state.data.taskId);

    const handleChange1 = (value) => {
        setInput1(value);
    };
    const handleChange2 = (value) => {
        setInput2(value);
    };

    const handleLoad = (e) => {
        context.actions.contextDispatch({ type: SET_TASKID, data: taskNum});
        console.log(taskNum);
    }
    const handleSaveAndLoad = (e) => {
        if(taskNum <= 120){
            setTaskNum(prev => prev + 1);
            context.actions.contextDispatch({ type: SET_TASKID, data: taskNum});
        }
        console.log(taskNum);
    }
    
    return (
        <>
            <div className={styles.wrapper}>
                <div className={styles.left}>
                    <p className={styles.title}>* 다음 유사 지시문 2개를 작성하시오.</p>
                    <input
                        onChange={(e) => setTaskNum(UnformattedTaskId(e.target.value))}
                        type="text" 
                        id="taskId"
                        value={FormattedTaskID(taskNum)}
                    />
                </div>
                <div className={styles.buttons}>
                    <button onClick={handleLoad}>저장 없이 왼쪽 지정 페이지로 이동</button>
                    <button onClick={handleSaveAndLoad}>저장하고 다음 페이지로 이동</button>
                </div>
            </div>
            <div className={styles.edit}>
                <form>
                    <TextArea input={input1} setInput={handleChange1}/>
                    <div className={styles.divider}/>
                    <TextArea input={input2} setInput={handleChange2}/>
                </form>
            </div>
        </>
    );
}

