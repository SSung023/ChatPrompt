import React, { useEffect, useState } from 'react';
import styles from './SelectBox.module.css';
import { FaAngleDown } from 'react-icons/fa';

export default function SelectBox({ defaultValue, options }) {
    const [selected, setSelected] = useState(window.localStorage.getItem("writer"));
    const [visible, setVisible] = useState(false);

    const handleSelected = (value) => {
        setSelected(value);
    }

    const handleVisible = (value) => {
        setVisible(value);
    }

    const makeOptions = () => {
        return options.map((option) => {
            return <Option value={option} setSelect={handleSelected} setVisible={handleVisible} key={option}/>
        })
    }

    useEffect(() => {
        window.localStorage.setItem("writer", selected);
        // console.log(window.localStorage.getItem("writer"));
    }, [selected]);

    return (
        <div className={styles.selectBox}>
            <button className={styles.defaultValue} onClick={() => setVisible(prev => !prev)}>
                {selected ? selected : defaultValue}
                <FaAngleDown/>
            </button>
            <div className={`${styles.options} ${visible ? styles.active : ''}`}>
                {options && makeOptions()}
            </div>
        </div>
    );
}

function Option({ value, setSelect, setVisible }) {
    const handleClickOption = () => {
        setSelect(value);
        setVisible(false);
    }

    return (
        <button className={styles.option} onClick={handleClickOption}>
            {value}
        </button>
    );
}