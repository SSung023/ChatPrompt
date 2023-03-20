import React from 'react';
import { TbPrompt, TbEdit } from 'react-icons/tb';
import { NavLink, useLocation } from 'react-router-dom';
import styles from './Navigation.module.css';

export default function Navigation() {
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
        </div>
    );
}

