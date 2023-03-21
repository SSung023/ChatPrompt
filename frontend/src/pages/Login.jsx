import axios from 'axios';
import React, { useContext } from 'react';
import { Navigate, useNavigate } from 'react-router-dom';
import SelectBox from '../components/ui/selectbox/SelectBox';
import { userContext } from '../context/UserContext';
import SignIn from '../utility/SignIn';
import styles from './Login.module.css';

export default function Login() {
    const bodyStyle = {
        height: `100vh`,
        display: `flex`,
        flexDirection: `column`,
        alignItems: `center`,
        justifyContent: `center`,
    }

    const writer = ["박소영", "김다은", "성희연", "홍길동", "권경란"];
    const context = useContext(userContext);
    const name = context.state.data.name;

    const navigate = useNavigate();
    const handleLogin = () => {
        axios.post(`/api/login?username=${name}`)
        .then(function(res) {
            console.log(res);
        })
        .then(function(res) {
            window.localStorage.setItem('name', name);
            navigate('/');
        })
    }

    return (
        <div className='body' style={bodyStyle}>
            <div className={styles.wrapper}>
                <div className={styles.start}>
                    <h1>
                        <span style={{ color: `var(--main-color)` }}>{`${name}`}</span>
                        <span>{`님, 안녕하세요!`}</span>
                    </h1>
                    <button 
                        className={styles.button} 
                        onClick={handleLogin}
                    >{`시작하기 >`}</button>
                </div>
                
                <SelectBox defaultValue="구축자 선택" options={writer}/>
            </div>
        </div>
    );
}

