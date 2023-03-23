import axios from 'axios';
import React, { useContext, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SET_FIRST_IO_IDX, SET_LAST_IO_IDX, SET_NAME, userContext } from '../context/UserContext';
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
    const writers = ["박소영", "김다은", "성희연", "홍길동", "권경란"];
    const context = useContext(userContext);
    // const name = context.state.data.name;

    const navigate = useNavigate();
    const handleLogin = (e) => {
        e.preventDefault();
        console.log("A".charCodeAt());
        if(!annotator){
            alert('이름을 입력하세요.');
            return;
        }
        if(!writers.includes(annotator)){
            alert('이름을 확인해주세요.');
            return;
        }
        if(writers.indexOf(annotator) !== (identifier.charCodeAt()-65)){
            alert('아이디를 확인해주세요.');
            return;
        }
        
        axios.post(`/api/login?identifier=${identifier}`)
        .then(function(res) {
            context.actions.contextDispatch({ type: SET_NAME, data: annotator });
            context.actions.contextDispatch({ type: SET_FIRST_IO_IDX, data: res.data.data.taskStartIdx });
            context.actions.contextDispatch({ type: SET_LAST_IO_IDX, data: res.data.data.taskEndIdx });
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
                        <option value={'A'} defaultValue>A</option>
                        <option value={'B'}>B</option>
                        <option value={'C'}>C</option>
                        <option value={'D'}>D</option>
                        <option value={'E'}>E</option>
                        <option value={'F'}>F</option>
                        <option value={'G'}>G</option>
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

