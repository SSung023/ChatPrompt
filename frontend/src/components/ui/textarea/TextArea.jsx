import React, { useCallback, useEffect, useRef } from 'react';
import styles from './TextArea.module.css';

export default function TextArea({ input, setInput }) {

    const textAreaRef = useRef();
    // const handleChange = useCallback((e) => {
    //     e.preventDefault();
    //     const val = e.target.value;

    //     setInput(val);
    // }, []);

    // 줄바꿈시 text area의 높이가 늘어나게 처리
    useEffect(() => {
        if(textAreaRef.current){
            textAreaRef.current.style.height = `auto`;
            textAreaRef.current.style.height = `${textAreaRef.current.scrollHeight}px`;
        }
    }, [input]);

    return (
        <textarea 
            rows={1}
            className={styles.input} 
            onChange={(e) => setInput(e.target.value)}
            placeholder={`지시문을 작성하세요`}
            value={input}
            ref={textAreaRef}
        />
    );
}

