import axios from 'axios';
import React from 'react';
import { TbPrompt, TbEdit } from 'react-icons/tb';
import { NavLink, useNavigate } from 'react-router-dom';
import SelectBox from '../ui/selectbox/SelectBox';
import styles from './Navigation.module.css';

export default function Navigation() {
    const navigate = useNavigate();
    const handleLogout = () => {
        axios.get('/api/logout')
        .then(function(res) {
            window.localStorage.removeItem('name');
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

            <button onClick={handleLogout} className={styles.logout}>로그아웃</button>
            {/* <SelectBox defaultValue="구축자 선택" options={writer}/>     */}
        </div>
    );
}

