import React from 'react';
import { TbQuestionMark } from 'react-icons/tb';
import styles from './InspectInfo.module.css';

export default function InspectInfo() {
    return (
        <div className={`${styles.inspectInfo} noDrag`}>
            <div className={styles.button}><TbQuestionMark /></div>
            <div className={styles.infoBox}>
                <p style={{ fontWeight: `var(--bold)`, color: `var(--main-color)` }}>✅  도움말</p>
                <p>표시 버튼을 누르면 달성률이 올라갑니다.</p>
            </div>
        </div>
    );
}

