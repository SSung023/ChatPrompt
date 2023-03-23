import React, { useContext, useEffect } from 'react';
import { SET_TASKNAME, userContext } from '../../context/UserContext';
import styles from './CurrentFile.module.css';

// name: 알바
// taskId: task 번호
// taskName: 작업 내용(지시문, 입출력 etc..)
export default function CurrentFile({ ptaskName }) {
    const context = useContext(userContext);
    const taskName = context.state.data.taskName;

    useEffect(() => {
        context.actions.contextDispatch({ type: SET_TASKNAME, data: `${ptaskName}` })
    }, [ptaskName]);
    
    return (
        <div className={styles.wrapper}>
            <p className={styles.p}>태스크:</p>
            <div className={styles.currentFile}>
                {/* 전체를 taskname으로 바꾸기 */}
                <p>{`${taskName}`}</p>
            </div>
        </div>
    );
}

