import axios from 'axios';
import React, { useState } from 'react';

import { TbHomePlus, TbHomeEdit, TbClipboardList, TbClipboardText, TbLogout } from 'react-icons/tb';
import { FiMinusSquare, FiPlusSquare } from 'react-icons/fi';

import { NavLink, useNavigate } from 'react-router-dom';
import styles from './Navigation.module.css';

export default function Navigation() {
    const navigate = useNavigate();

    const [isOpen, setOpen] = useState(true);
    const toggleSnb = () => {
        setOpen(prev => !prev);
    };

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
        <div 
            className={styles.gnb} 
            style={isOpen ? {width: `100%`, minWidth: `12em`} : {}}
        >
            <div className={styles.colFlex}>
                <div 
                    className={styles.logo}
                    style={isOpen ? {} : { justifyContent: 'center' }}
                >
                    {isOpen ? `Prompt` : ''}
                    <button 
                        className={styles.toggleBtn}
                        onClick={toggleSnb}>
                        {isOpen ? <FiMinusSquare /> : <FiPlusSquare />}
                    </button>
                </div>
                {/* <div className={styles.divider}/> */}

                <NavLink 
                    to={`/`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                    >
                    <TbHomePlus />
                    {isOpen ? <p>지시문 편집</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/input`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbHomeEdit />
                    {isOpen ? <p>입력 편집</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>

                {/* <div className={styles.divider}/> */}

                <NavLink 
                    to={`/inquire/instruction`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbClipboardList />
                    {isOpen ? <p>지시문 조회</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/inquire/io`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbClipboardText />
                    {isOpen ? <p>입력 조회</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
            </div>

            {/* <div className={styles.divider}/> */}

            <button onClick={handleLogout} className={styles.logout}>
                <TbLogout />
                {isOpen ? <p>로그아웃</p> : ''}
            </button>
        </div>
    );
}

