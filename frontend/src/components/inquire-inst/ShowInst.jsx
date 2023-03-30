import axios from 'axios';
import React, { useEffect, useMemo, useState } from 'react';
// import { userContext } from '../../context/UserContext';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './ShowInst.module.css';

export default function ShowInst({ taskNum }) {
    // const context = useContext(userContext);
    // const taskId = context.state.data.inst_taskId;
    const [data, setData] = useState();

    const makeSimilarInst = useMemo(() => {
        return (
            data && 
            data.map((inst, idx) => {
                return (
                    <TableRow key={idx}>
                        <TableHead>{`지시문 ${idx+1}`}</TableHead>
                        <TableCell>
                            {!inst.similar_instruct || inst.similar_instruct === "유사지시문이 아직 작성되지 않았습니다."
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
            <p className={styles.title}>* 내가 쓴 지시문</p>
            <Table>
                <TableBody>
                    {makeSimilarInst}
                </TableBody>
            </Table>
        </div>
    );
}

