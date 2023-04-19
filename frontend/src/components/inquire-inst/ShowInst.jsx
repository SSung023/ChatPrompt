import axios from 'axios';
import React, { useContext, useEffect, useMemo, useState } from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './ShowInst.module.css';
import { AiOutlineLeft, AiOutlineRight } from 'react-icons/ai';
import { userContext } from '../../context/UserContext';

export default function ShowInst({ taskNum, setTaskNum }) {
    const context = useContext(userContext);
    // const taskId = context.state.data.inst_taskId;

    const first_taskId = context.state.data.first_taskId;
    const last_taskId = context.state.data.last_taskId;

    const [data, setData] = useState();

    const makeSimilarInst = useMemo(() => {
        return (
            data && 
            data.map((inst, idx) => {
                return (
                    <TableRow key={idx}>
                        <TableHead>{`지시문 ${idx+1}`}</TableHead>
                        <TableCell>
                            {!inst.similar_instruct
                            ? <span style={{color:"var(--placeholder-txt-color)"}} className="noDrag">작성한 지시문이 없습니다.</span>
                            : <span>{inst.similar_instruct}</span>}
                        </TableCell>
                    </TableRow>
                )
            })
            
        );
    }, [data]);

    const loadSimilar = () => {
        axios.get(`/api/tasks/${taskNum}/assignment-similar/lists`)
        .then(function(res) {
            // console.log(res);
            return res.data.dataList;
        })
        .then(function(data) {
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
    }, [taskNum]);

    return (
        <div className={styles.showInst}>
            <div className={styles.header}>
                <p className={styles.title}>* 내가 쓴 지시문</p>
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
                    {makeSimilarInst}
                </TableBody>
            </Table>
        </div>
    );
}

