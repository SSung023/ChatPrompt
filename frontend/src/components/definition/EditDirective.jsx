import React, { useCallback, useEffect, useRef, useState } from 'react';
// import TextArea from '../ui/textarea/TextArea';
import styles from './EditDirective.module.css';

export default function EditDirective() {
    const [input1, setInput1] = useState('');
    const [input2, setInput2] = useState('');

    const handleChange1 = (e) => {
        setInput1(e.target.value);
    };
    const handleChange2 = (e) => {
        setInput2(e.target.value);
    };
    
    return (
        <div className={styles.edit}>
            <form>
                {/* <TextArea input={input1} setInput={handleInput1}/>
                <TextArea input={input2} setInput={handleInput2}/> */}
                <textarea 
                    rows={1}
                    className={styles.input} 
                    onChange={() => handleChange1}
                    value={input1}
                />
                <textarea 
                    rows={1}
                    className={styles.input} 
                    onChange={() => handleChange2}
                    value={input2}
                />
            </form>
        </div>
    );
}

