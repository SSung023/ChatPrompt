import axios from 'axios';
import React, { useState } from 'react';

import { TbFilePlus, TbFilePencil, TbFileSearch, TbLogout, TbTextSpellcheck, TbZoomCheck, TbChecks, TbCheck, TbListSearch, TbZoomQuestion } from 'react-icons/tb';
import { BsArrowBarLeft, BsArrowBarRight } from 'react-icons/bs';

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
            className={`${styles.gnb} noDrag`} 
            style={isOpen ? {width: `100%`, minWidth: `13em`} : {}}
        >
            <div className={styles.colFlex}>
                <div 
                    className={styles.logo}
                    style={isOpen ? {} : { justifyContent: 'center' }}
                >
                    {isOpen ? `Prompt` : ''}
                    <button 
                        className={isOpen ? `${styles.toggleBtn} ${styles.open}` : `${styles.toggleBtn} ${styles.close}`}
                        onClick={toggleSnb}>
                        {isOpen ? <BsArrowBarLeft /> : <BsArrowBarRight />}
                    </button>
                </div>
                {/* <div className={styles.divider}/> */}

                <NavLink 
                    to={`/`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                    >
                    <TbFilePlus />
                    {isOpen ? <p>지시문 구축</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/inquire/instruction`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbFileSearch />
                    {isOpen ? <p>지시문 전체 조회</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/duplicated/instruction`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbTextSpellcheck />
                    {isOpen ? <p>지시문 중복 검사</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>


                <NavLink 
                    to={`/input`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbFilePencil />
                    {isOpen ? <p>입출력 구축</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>
                <NavLink 
                    to={`/inquire/io`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbFileSearch />
                    {isOpen ? <p>입력 전체 조회</p> : ''}
                    <p className={styles.bar}></p>
                </NavLink>

                <NavLink 
                    to={`/inspect`} 
                    className={({isActive}) => isActive ? `${styles.navMenu} ${styles.active}` : styles.navMenu}
                >
                    <TbZoomQuestion />
                    {isOpen ? <p>검증하기</p> : ''}
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

