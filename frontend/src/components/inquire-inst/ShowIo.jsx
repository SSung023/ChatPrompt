import axios from 'axios';
import React, { useContext, useEffect, useMemo, useState } from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './ShowInst.module.css';
import { userContext } from '../../context/UserContext';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';

export default function ShowIo({ taskNum, setTaskNum }) {
    const context = useContext(userContext);

    const first_taskId = context.state.data.io_first_taskId;
    const last_taskId = context.state.data.io_last_taskId;

    const [data, setData] = useState();
    const [userId, setId] = useState();

    const getUserId = (taskId) => {
        if(taskId >= 1 && taskId < 7) setId('C')
        else if(taskId >= 7 && taskId < 31) setId('B')
        else if(taskId >= 31 && taskId < 39) setId('D')
        else if(taskId >= 39 && taskId < 61) setId('B')
        else if(taskId >= 61 && taskId < 69) setId('E')
        else if(taskId >= 69 && taskId < 91) setId('B')
        else if(taskId >= 91 && taskId < 99) setId('F')
        else if(taskId >= 99 && taskId < 121) setId('B')
    }

    const makeIoList = useMemo(() => {
        return (
            data && 
            data.map((inst, idx) => {
                const row1 = (
                    <TableRow key={idx*2}>
                        <TableHead>{`입력 ${idx+1}`}</TableHead>
                        <TableCell>
                            {!inst.input
                            ? <span style={{color:"var(--placeholder-txt-color)"}} className="noDrag">작성한 입력이 없습니다.</span>
                            : <span>{inst.input}</span>}
                        </TableCell>
                    </TableRow>
                )
                const row2 = (
                    <TableRow key={idx*2+1}>
                        <TableHead>{`출력 ${idx+1}`}</TableHead>
                        <TableCell>
                            {!inst.output
                            ? <span style={{color:"var(--placeholder-txt-color)"}} className="noDrag">작성한 출력이 없습니다.</span>
                            : <span>{inst.output}</span>}
                        </TableCell>
                    </TableRow>
                )
                    
                return [row1, row2];
            })
            
        );
    }, [data]);

    const loadSimilar = () => {
        axios.get(`/api/tasks/${taskNum}/assignment-io/lists`)
        .then(function(res) {
            // console.log(res);
            return res.data.dataList;
        })
        .then(function(data) {
            // console.log(data);
            setData(data);
        })
        .catch(function(err) {
            if(err.response.status === 400){
                alert('세션이 만료되었습니다. 로그인 후 다시 시도해주세요.');
                window.localStorage.removeItem("prompt-login");
                window.location.replace(window.location.href);
            }
        })
    }

    useEffect(() => {
        loadSimilar();
        getUserId(taskNum);
    }, [taskNum]);

    return (
        <div className={styles.showInst}>
            <div className={styles.header}>
                <p className={styles.title}>{(first_taskId == 1 && last_taskId == 120) ? `* 구축자 ${userId} 입출력` : `* 내가 쓴 지시문`}</p>
                <div className={styles.buttons}>
                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={() => {
                            if(taskNum > first_taskId){
                                setTaskNum(taskNum - 1);
                            }
                            else{
                                alert('첫 지시문입니다.');
                            }
                        }}    
                    ><AiOutlineLeft/></button>
                    <button 
                        className={`${styles.moveBtn} noDrag`}
                        onClick={() => {
                            if(taskNum < last_taskId){
                                setTaskNum(taskNum + 1);
                            }
                            else{
                                alert('마지막 지시문입니다.');
                            }
                        }}    
                    ><AiOutlineRight/></button>
                </div>
            </div>
            <Table>
                <TableBody>
                    {makeIoList}
                </TableBody>
            </Table>
        </div>
    );
}

