import React, { useContext, useEffect, useState } from 'react';
import styles from './Instruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import { SET_SUB_IDX, SET_TASKNAME, userContext } from '../../context/UserContext';
import axios from 'axios';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import InputNumber from '../ui/input/InputNumber';

export default function Instruction() {
    const context = useContext(userContext);

    const taskId = context.state.data.io_taskId;
    const subIdx = context.state.data.sub_idx;

    // context handler
    const handleSubIdx = (value) => {
        context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(value) });
    }

    // state
    const [data, setData] = useState();
    
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
    
    // 유사 지시문 로드
    useEffect(() => {
        handleLoad();
    }, [taskId, subIdx]);

    return (
        data &&
        <div className={styles.instruction}>
            <div className={styles.wrapper}>
                <div className={styles.header}>
                    <p className={styles.title}>* 내가 쓴 지시문</p>

                    <form
                        className={styles.form}
                        onSubmit={(e) => {e.preventDefault()}}
                    >
                        <label className={`noDrag ${styles.label}`}>sub index: </label>
                        <InputNumber
                            context={subIdx}
                            setContext={handleSubIdx}
                            maxNum={10}
                            minNum={1}
                        />
                    </form>
                </div>
                <div className={styles.moveBtns}>
                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={() => {
                            if(subIdx > 1){
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subIdx)-1});
                            }
                            else {
                                alert('첫 지시문입니다.');
                            }
                        }}    
                    ><AiOutlineLeft/></button>

                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={() => {
                            if(subIdx < 10){
                                context.actions.contextDispatch({ type: SET_SUB_IDX, data: parseInt(subIdx)+1});
                            }
                            else {
                                alert('마지막 지시문입니다.');
                            }
                        }}
                    ><AiOutlineRight/></button>
                </div>
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
        </div>
    );
}