import React, { useEffect, useRef, useState } from 'react';
import styles from './InputNumber.module.css';

export default function InputNumber({ context, setContext, maxNum, minNum }) {
    const [num, setNum] = useState(context);
    const inputRef = useRef();

    // keyDown이 onChange 보다 먼저 실행됨.
    const handleKeyDown = (e) => {
        const value = parseInt(e.target.value, 10) || 0;

        if(e.key === "Enter"){
            e.preventDefault();
            if(value >= minNum && value <= maxNum) {
                setContext(value);
            }
            else {
                alert('입력 범위를 확인 후 조회해주세요.');
                // context로 초기화하기
                setNum(context);
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
        const value = parseInt(e.target.value, 10) || 0;
        setNum(value);
    }
    
    const handleOnBlur = (e) => {
        setNum(context);
    }

    const handleOnClick = () => {
        inputRef.current.select();
    }

    useEffect(() => {
        setNum(context);
    }, [context]);

    return (
        <input
            className={styles.input}
            type='number'
            ref={inputRef}
            value={num.toString().replace(/^0+/, '')}
            onChange={handleOnChange}
            onClick={handleOnClick}
            onBlur={handleOnBlur}
            onKeyDown={handleKeyDown}
            min={1}
            max={999}
        />
    );
}

