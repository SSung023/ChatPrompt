import React, { useContext, useEffect, useRef, useState } from 'react';
import styles from './Instruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_SUB_IDX, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

export default function Instruction() {
    const context = useContext(userContext);

    const taskId = context.state.data.io_taskId;
    const subIdx = context.state.data.sub_idx;

    // state
    const [data, setData] = useState();
    const [subNum, setSubNum] = useState(subIdx);

    // ref
    const subNumRef = useRef();
    
    const handleLoad = () => {
        // console.log('load');
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
        if(e.key === "Enter"){
            const id = e.target.id;
            e.preventDefault();
            if(id === "subIdx") {
                const value= e.target.value;
                value >= 1 && value <= 10 && setSubNum(parseInt(value));
                context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum) });
            }
            handleLoad(e);
        }
    }
    const handleOnBlur = (e) => {
        const id = e.target.id;
        if(id === "subIdx") {
            const value = e.target.value;
            value >= 1 && value <= 10 && setSubNum(parseInt(value));
        }
    }
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
        setSubNum(subIdx);
    }, [taskId, subIdx]);

    return (
        data &&
        <div className={styles.instruction}>
            <div className={styles.header}>
                <p className={styles.title}>* 내가 쓴 지시문</p>

                <form
                    className={styles.form}
                    onSubmit={(e) => {e.preventDefault()}}
                >
                    <label className={`noDrag ${styles.label}`}>sub index: </label>
                    <input 
                        className={styles.input}
                        ref={subNumRef}
                        type="number"
                        id="subIdx"
                        onChange={(e) => {
                            const value = parseInt(e.target.value);
                            value >= 1 && value <= 10 && setSubNum(parseInt(e.target.value))
                        }}
                        max="10"
                        min="1"
                        value={subNum}
                        onKeyDown={handlePressEnter}
                        onBlur={handleOnBlur}
                        onClick={() => {
                            subNumRef.current.select();
                        }}
                    />
                    <p 
                        className='noDrag'
                        style={{
                            color: "var(--light-main-color)", 
                            fontSize: "14px",
                            marginLeft: "1em",
                    }}>✓ 엔터를 누르면 조회됩니다.</p>
                </form>
            </div>
            
            {/* show similar instruct */}
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>{`유사\n지시문 ${subIdx}`}</TableHead>
                        {/* <TableHead>{`${subIdx}번`}</TableHead> */}
                        <TableCell>{data.similarInstruct1 ? data.similarInstruct1 : <span style={{color: "var(--placeholder-txt-color)"}} className="noDrag">작성한 지시문이 없습니다.</span>}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>

            {/* buttons */}
            <div className={styles.buttons}>
                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(subNum > 1){
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum)-1});
                            setSubNum(prev => parseInt(prev)-1);
                        }
                        else {
                            alert('첫 지시문입니다.');
                        }
                    }}    
                ><AiOutlineLeft/>이전</button>

                <button 
                    className={`${styles.moveBtn} noDrag`}
                    onClick={() => {
                        if(subNum < 10){
                            context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subNum)+1});
                            setSubNum(prev => parseInt(prev)+1);
                        }
                        else {
                            alert('마지막 지시문입니다.');
                        }
                    }}
                >다음<AiOutlineRight/></button>
            </div>
        </div>
    );
}