import React from 'react';
import { TbPrompt, TbEdit } from 'react-icons/tb';
import { NavLink } from 'react-router-dom';
import SelectBox from '../ui/selectbox/SelectBox';
import styles from './Navigation.module.css';

export default function Navigation() {
    // const writer = ["박소영", "김다은", "성희연", "홍길동", "권경란"];

    return (
        <div className={styles.gnb}>
            <ul className={styles.colFlex}>
                <NavLink 
                    to={`/`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                    >
                    <TbPrompt />
                    지시문 편집
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/input`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbEdit />
                    입력 편집
                    <p className={styles.bar}></p>
                </NavLink>
            </ul>

            <div className={styles.divider}/>

            {/* <SelectBox defaultValue="구축자 선택" options={writer}/>     */}
        </div>
    );
}

