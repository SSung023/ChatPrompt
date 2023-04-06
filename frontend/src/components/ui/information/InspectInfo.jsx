import React from 'react';
import { TbQuestionMark } from 'react-icons/tb';
import { VscEyeClosed } from 'react-icons/vsc';
import { Navigate } from 'react-router-dom';
import styles from './InspectInfo.module.css';

export default function InspectInfo() {
    return (
        <div className={`${styles.inspectInfo} noDrag`}>
            <div className={styles.button}><TbQuestionMark /></div>
            <div className={styles.infoBox}>
                <p style={{ fontWeight: `var(--bold)`, color: `var(--main-color)`, marginBottom: `6px`, }}>✅  도움말</p>
                <p><VscEyeClosed/> 버튼을 활성화하면 달성률이 올라갑니다.</p>
                <p style={{ color: `var(--main-color)`, fontSize: `12px`, }}>버튼이 안 보인다면 입출력을 작성해 주세요!</p>
            </div>
        </div>
    );
}

