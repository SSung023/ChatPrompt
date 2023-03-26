import axios from 'axios';
import React from 'react';
import { TbHomePlus, TbHomeEdit, TbClipboardList, TbClipboardText, TbLogout } from 'react-icons/tb';
import { NavLink, useNavigate } from 'react-router-dom';
import styles from './Navigation.module.css';

export default function Navigation() {
    const navigate = useNavigate();
    const handleLogout = () => {
        axios.post('/api/logout')
        .then(function(res) {
            window.localStorage.removeItem('prompt-login');
            navigate('/login');
        })
        .catch(function (err) {
            console.log(err);
        })
    }

    return (
        <div className={styles.gnb}>
            <ul className={styles.colFlex}>
                <NavLink 
                    to={`/`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                    >
                    <TbHomePlus />
                    <p>지시문 편집</p>
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/input`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbHomeEdit />
                    <p>입력 편집</p>
                    <p className={styles.bar}></p>
                </NavLink>

                <div className={styles.divider}/>

                <NavLink 
                    to={`/inquire/instruction`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbClipboardList />
                    <p>지시문 조회</p>
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/inquire/io`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbClipboardText />
                    <p>입력 조회</p>
                    <p className={styles.bar}></p>
                </NavLink>
            </ul>

            <div className={styles.divider}/>

            <button onClick={handleLogout} className={styles.logout}>
                로그아웃
                <TbLogout />
            </button>
        </div>
    );
}

