import React, { useContext, useEffect, useState } from 'react';
import styles from './Instruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_SUB_IDX, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';

export default function Instruction() {
    const context = useContext(userContext);
    const subIdx = context.state.data.sub_idx;
    const taskId = context.state.data.io_taskId;

    const [data, setData] = useState();
    const [subNum, setSubNum] = useState(1);

    
    const handleLoad = () => {
        console.log('load');
        axios.get(`/api/tasks/${taskId}/assignment/${subIdx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setData(data);
            context.actions.contextDispatch({ type: SET_TASKNAME, data: data.taskTitle});
        })
    }
    const handlePressEnter = (e) => {
        if(e.key === "Enter") {
            e.preventDefault();

            const value = e.target.value;
            value >= 1 && value <= 10 && setSubNum(value);
            context.actions.contextDispatch({ type: SET_SUB_IDX, data: subNum });
            // handleLoad();
        }
    }
    const handleOnBlur = (e) => {
        const value = e.target.value;
        value >= 1 && value <= 10 && setSubNum(value);
    }
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
    }, [taskId, subIdx]);

    return (
        data &&
        <div className={styles.instruction}>
            <div className={styles.header}>
                <label>sub index: </label>
                <input 
                    type="number"
                    onChange={(e) => {
                        const value = e.target.value;
                        value >= 1 && value <= 10 && setSubNum(parseInt(e.target.value))
                    }}
                    max="10"
                    min="1"
                    value={subNum}
                    onKeyDown={handlePressEnter}
                    onBlur={handleOnBlur}
                />
                {/* <span style={{ 
                    color: `#e02b2b`, 
                    fontSize: `12px`, 
                    marginLeft: `1em`,
                    lineHeight: `1.5em`,
                }}>
                    ⚠ 엔터를 누르면 저장되지 않고 이동합니다.
                </span> */}
            </div>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>{`유사\n지시문${subIdx}`}</TableHead>
                        <TableCell>{data.similarInstruct1 ? data.similarInstruct1 : <span style={{color: "var(--light-txt-color)"}}>작성한 지시문이 없습니다.</span>}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}