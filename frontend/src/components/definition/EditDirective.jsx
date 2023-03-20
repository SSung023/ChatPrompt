import React, { useState } from 'react';
import TextArea from '../ui/textarea/TextArea';
import styles from './EditDirective.module.css';

export default function EditDirective() {
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    const handleChange1 = (value) => {
        setInput1(value);
    };
    const handleChange2 = (value) => {
        setInput2(value);
    };
    
    return (
        <>
            <p className={styles.title}>* 다음 유사 지시문 2개를 작성하시오.</p>
            <div className={styles.edit}>
                <form>
                    <TextArea input={input1} setInput={handleChange1}/>
                    <div className={styles.divider}/>
                    <TextArea input={input2} setInput={handleChange2}/>
                </form>
            </div>
        </>
    );
}

