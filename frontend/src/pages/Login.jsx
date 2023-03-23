import axios from 'axios';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SET_NAME, userContext } from '../context/UserContext';
import styles from './Login.module.css';

export default function Login() {
    const bodyStyle = {
        height: `100vh`,
        display: `flex`,
        flexDirection: `column`,
        alignItems: `center`,
        justifyContent: `center`,
    }

    const [identifier, setIdentifier] = useState(0);
    const [annotator, setAnnotator] = useState('');
    const writers = ["박소영", "김다은", "성희연", "홍길동", "권경란"];
    const context = useContext(userContext);
    // const name = context.state.data.name;

    const navigate = useNavigate();
    const handleLogin = (e) => {
        e.preventDefault();
        if(!annotator){
            alert('이름을 입력하세요.');
            return;
        }
        if(!writers.includes(annotator)){
            alert('이름을 확인해주세요.');
            return;
        }
        if(writers.indexOf(annotator) !== parseInt(identifier)){
            alert('아이디를 확인해주세요.');
            return;
        }
        
        axios.post(`/api/login?username=${annotator}`)
        .then(function(res) {
            context.actions.contextDispatch({ type: SET_NAME, data: annotator })
            return res;
        })
        .then(function(res) {
            window.localStorage.setItem('name', annotator);
            navigate('/');
        })
        .catch(function(err) {
            // 로그인 요청에 실패한 경우
            alert('잠시 후에 다시 시도해주세요.');
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
                        <option value={0} defaultValue>A</option>
                        <option value={1}>B</option>
                        <option value={2}>C</option>
                        <option value={3}>D</option>
                        <option value={4}>E</option>
                        <option value={5}>F</option>
                        <option value={6}>G</option>
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

