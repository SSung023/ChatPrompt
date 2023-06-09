import React, { useEffect, useMemo, useState } from 'react';
import styles from './IoReference.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import axios from 'axios';

export default function IoReference({ taskId, idx }) {
    const [definition, setDefinition] = useState();
    const [io, setIo] = useState();

    const makeDefinition = useMemo(() => {
        return (
            definition && 
            <TableRow>
                <TableHead>지시문</TableHead>
                <TableCell>
                    <span style={{color: `var(--g-txt-color)`}}>{`${definition.definition1}`}</span>
                    <br/>
                    <br/>
                    <span>{`${definition.definition2}`}</span>
                </TableCell>
            </TableRow>
        );
    }, [definition]);

    // taskId가 변경되면 지시문 원문 초기화
    useEffect(() => {
        taskId && axios.get(`/api/tasks/${taskId}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setDefinition(data);
        })
    }, [taskId]);

    // idx가 변경되면 io 초기화
    useEffect(() => {
        idx && axios.get(`/api/tasks/${taskId}/io-pairs/${idx}`)
        .then(function(res) {
            return res.data.data;
        })
        .then(function(data) {
            setIo(data.ioResponse);
        })
    }, [taskId, idx]);


    return (
        io && <div className={styles.ioReference}>
            <div className={styles.header}>
                <p className={styles.title}>* 참고자료</p>
                <div className={styles.wrapper}>
                    <Table>
                        <TableBody>
                            {makeDefinition}
                            <TableRow>
                                <TableHead>{`입력 ${idx}`}</TableHead>
                                <TableCell>
                                    <span style={{color: `var(--g-txt-color)`}}>{io.input1}</span>
                                    <br/>
                                    <span>{io.input2}</span>
                                </TableCell>
                            </TableRow>
                            <TableRow>
                                <TableHead>{`출력 ${idx}`}</TableHead>
                                <TableCell>
                                    <span style={{color: `var(--g-txt-color)`}}>{io.output1}</span>
                                    <br/>
                                    <span>{io.output2}</span>
                                </TableCell>
                            </TableRow>
                        </TableBody>
                    </Table>
                </div>
            </div>
        </div>
    );
}

