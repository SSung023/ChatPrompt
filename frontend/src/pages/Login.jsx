import axios from 'axios';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SET_FIRST_TASKID, SET_LAST_TASKID, SET_NAME, userContext } from '../context/UserContext';
import styles from './Login.module.css';

export default function Login() {
    const bodyStyle = {
        height: `100vh`,
        display: `flex`,
        flexDirection: `column`,
        alignItems: `center`,
        justifyContent: `center`,
    }

    const [identifier, setIdentifier] = useState('A');
    const [annotator, setAnnotator] = useState('');
    const context = useContext(userContext);

    const navigate = useNavigate();
    const handleLogin = (e) => {
        e.preventDefault();
        if(!annotator){
            alert('이름을 입력하세요.');
            return;
        }
        
        // 로그인 시 identifier과 username을 쿼리문으로 전송
        axios.post(`/api/login?identifier=${identifier}&username=${annotator}`)
        .then(function(res) {
            context.actions.contextDispatch({ type: SET_NAME, data: annotator });
            context.actions.contextDispatch({ type: SET_FIRST_TASKID, data: res.data.data.taskStartIdx });
            context.actions.contextDispatch({ type: SET_LAST_TASKID, data: res.data.data.taskEndIdx });
        })
        .then(function() {
            window.localStorage.setItem('prompt-login', true);
            navigate('/');
        })
        .catch(function(err) {
            // 로그인 요청에 실패한 경우
            alert(`${err.response.data.message}`);
        })
    }

    return (
        <div className='body' style={bodyStyle}>
            <div className={styles.wrapper}>
                <form
                    onSubmit={handleLogin}
                    className={styles.loginForm}
                >
                    <label>ID:</label>
                    <select
                        name = "annotator" 
                        onChange={(e) => {
                            setIdentifier(e.target.value);
                        }}
                        value={identifier}
                    >
                        {/* 구축자 id는 문자 */}
                        <option value={'A'} defaultValue>A</option>
                        <option value={'B'}>B</option>
                        <option value={'C'}>C</option>
                        <option value={'D'}>D</option>
                        <option value={'E'}>E</option>
                        <option value={'F'}>F</option>
                    </select>

                    <label>Name:</label>
                    <input
                        type="text" 
                        name="name"
                        value={annotator}
                        onChange={(e) => {
                            setAnnotator(e.target.value);
                        }}
                        placeholder='이름'
                        required
                        autoComplete='off'
                    />
                </form>
                <button
                    className={styles.button} 
                    onClick={handleLogin}
                >{`시작하기 >`}</button>
            </div>
        </div>
    );
}

