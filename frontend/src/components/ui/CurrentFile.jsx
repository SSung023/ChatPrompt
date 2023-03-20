import React, { useContext, useEffect } from 'react';
import { SET_TASKNAME, userContext } from '../../context/UserContext';
import styles from './CurrentFile.module.css';

// name: 알바
// taskId: task 번호
// taskName: 작업 내용(지시문, 입출력 etc..)
export default function CurrentFile({ ptaskName }) {
    const context = useContext(userContext);
    const name = context.state.data.name;
    const taskId = context.state.data.taskId;
    const taskName = context.state.data.taskName;

    useEffect(() => {
        context.actions.contextDispatch({ type: SET_TASKNAME, data: `${ptaskName}` })
    }, [ptaskName]);
    
    return (
        <div className={styles.wrapper}>
            <p className={styles.p}>Current File is: </p>
            <div className={styles.currentFile}>
                <p>{`${name} _ ${taskId} _ ${taskName}`}</p>
                <p> .txt</p>
            </div>
        </div>
    );
}

