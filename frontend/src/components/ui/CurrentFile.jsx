import React from 'react';
import styles from './CurrentFile.module.css';

// name: 알바
// taskId: task 번호
// taskName: 작업 내용(지시문, 입출력 etc..)
export default function CurrentFile({ name, taskId, taskName }) {
    return (
        <div className={styles.currentFile}>
            <p className={styles.p}>ID : </p>
            <p>{`${name} _ ${taskId} _ ${taskName}`}</p>
            <p> .txt</p>
        </div>
    );
}

