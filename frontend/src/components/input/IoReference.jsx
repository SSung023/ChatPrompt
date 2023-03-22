import React, { useEffect, useMemo, useState } from 'react';
import styles from './IoReference.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import axios from 'axios';

export default function IoReference({ taskId, idx }) {
    const [definition, setDefinition] = useState();
    const [io, setio] = useState();

    const makeDefinition = useMemo(() => {
        // console.log('useMemo!');
        return (
            definition && 
            <TableRow>
                <TableHead>지시문</TableHead>
                <TableCell>
                    <span style={{color: `var(--g-dark-txt-color)`}}>{`${definition.definition1}`}</span>
                    <br/>
                    <br/>
                    <span>{`${definition.definition2}`}</span>
                </TableCell>
            </TableRow>
        );
    }, [definition]);

    // taskId가 변경되면 definition 초기화
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setDefinition(data);
        })
    }, [taskId]);

    // idx가 변경되면 io 초기화
    useEffect(() => {
        axios.get(`/api/tasks/${taskId}/io-pairs/${idx}`)
        .then(function(res) {
            console.log(res.data.data);
        })
    }, [idx]);


    return (
        idx && <div className={styles.ioReference}>
            <div className={styles.header}>
                <p className={styles.title}>* 참고자료</p>
                <div className={styles.wrapper}>
                    <Table>
                        <TableBody>
                            {makeDefinition}
                            <TableRow>
                                <TableHead>{`입력${idx}`}</TableHead>
                                <TableCell>{}</TableCell>
                            </TableRow>
                            <TableRow>
                                <TableHead>{`출력${idx}`}</TableHead>
                                <TableCell>{}</TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

