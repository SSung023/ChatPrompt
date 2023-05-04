import React from 'react';
import { TbQuestionMark } from 'react-icons/tb';
import { VscEyeClosed } from 'react-icons/vsc';
import styles from './InfoBox.module.css';

export default function InfoBox({ children }) {
    return (
        <div className={`${styles.inspectInfo} noDrag`}>
            <div className={styles.button}><TbQuestionMark /></div>
            <div className={styles.infoBox}>
                { children }
            </div>
        </div>
    );
}

