import React, { useRef, useState } from 'react';
import styles from './InputNumber.module.css';

export default function InputNumber({ num, setNum, setContext, maxNum, minNum }) {
    const [prevNum, setPrevNum] = useState(num);
    const inputRef = useRef();

    // keyDown이 onChange 보다 먼저 실행됨.
    const handleKeyDown = (e) => {
        const value = parseInt(e.target.value) || 0;

        if(e.key === "Enter"){
            e.preventDefault();
            if(value >= minNum && value <= maxNum) {
                // UI에 보이는 수인 num으로 초기화 하기
                setContext(num);
            }
            else {
                alert('입력 범위를 확인 후 조회해주세요.');
                setNum(prevNum);
            }
        }
        else if(e.key === "ArrowUp"){
            if(value >= maxNum) {
                e.preventDefault();
            }
        }
        else if(e.key === "ArrowDown") {
            if(value <= minNum) {
                e.preventDefault();
            }
        }
    }
    
    const handleOnChange = (e) => {
        const value = parseInt(e.target.value) || 0;
        setNum(value);
        
        // 범위가 올바를 경우에만 prevNum 갱신
        if(value >= minNum && value <= maxNum){
            setPrevNum(value);
        }
    }
    
    const handleOnBlur = (e) => {
        if(prevNum < minNum) {
            setNum(minNum);
            setPrevNum(minNum);
        }
        else if(prevNum > maxNum) {
            setNum(maxNum);
            setPrevNum(maxNum);
        }
        else setNum(prevNum);
    }

    const handleOnClick = () => {
        inputRef.current.select();
    }

    return (
        <input
            className={styles.input}
            type='number'
            ref={inputRef}
            value={num}
            onChange={handleOnChange}
            onClick={handleOnClick}
            onBlur={handleOnBlur}
            onKeyDown={handleKeyDown}
            min={1}
            max={999}
        />
    );
}

