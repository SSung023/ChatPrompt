import axios from 'axios';
import React, { useEffect, useMemo, useState } from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './ShowInst.module.css';

export default function ShowIo({ taskNum }) {
    const [data, setData] = useState();

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
    }, [taskNum]);

    return (
        <div className={styles.showInst}>
            <p className={styles.title}>* 내가 쓴 입출력</p>
            <Table>
                <TableBody>
                    {makeIoList}
                </TableBody>
            </Table>
        </div>
    );
}

