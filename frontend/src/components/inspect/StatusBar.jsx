import axios from 'axios';
import React, { useContext, useEffect, useRef, useState } from 'react';
import { SET_IO_PROGRESS, userContext } from '../../context/UserContext';
import styles from './StatusBar.module.css';

export default function StatusBar() {
    const context = useContext(userContext);
    const taskId = context.state.data.io_taskId;
    const io_progress = context.state.data.io_progress;

    const [progress, setProgress] = useState(io_progress);

    const barRef = useRef();

    useEffect(() => {
        setProgress(io_progress);
        // console.log(Math.floor( 3 / 60 * 100 ));
        barRef.current.style.width = `${Math.floor( 3 / 60 * 100 )}%`;
        console.log(progress);
    }, [io_progress]);

    useEffect(() => {
        axios.get(`/api/verifications/tasks/${taskId}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            context.actions.contextDispatch({ type: SET_IO_PROGRESS, data: data.validatedCnt });
        })
    
    }, [taskId]);

    return (
        <div className={styles.barWrapper}>
            <div className={styles.header}>
                <p style={{ fontWeight: `var(--medium)` }}>{`task ${taskId} 진행률`}</p>
                <div className={styles.wrapper}>
                    <p>{`${progress}`}</p>
                    <p>/ 60</p>
                </div>
            </div>
            <div className={styles.outerBar}>
                <div className={styles.innerBar} ref={barRef}></div>
            </div>
        </div>
    );
}

