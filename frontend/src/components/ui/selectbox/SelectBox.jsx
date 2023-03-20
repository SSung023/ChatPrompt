import React, { useContext, useEffect, useState } from 'react';
import styles from './SelectBox.module.css';
import { FaAngleDown } from 'react-icons/fa';
import { userContext, SET_NAME } from '../../../context/UserContext';

export default function SelectBox({ defaultValue, options }) {
    const context = useContext(userContext);

    const [selected, setSelected] = useState(context.state.data.name);
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
        context.actions.contextDispatch({ type: SET_NAME, data: `${selected}` })
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