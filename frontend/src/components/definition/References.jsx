import React, { useEffect, useState } from 'react';
import Table, { TableBody, TableHead, TableRow, TableCell } from '../ui/table/Table';
import styles from './References.module.css';
import axios from 'axios';

export default function References({ taskId, originalDefData }) {
    const [ioData, setIo] = useState();
    
    useEffect(() => {
        taskId && (axios.get(`/api/tasks/${taskId}/io-pairs`)
        .then(function(res) {
            setIo(res.data.dataList);
        }))
    }, [taskId]);

    const makeIOPairs = () => {
        return ioData.map((io_pair, idx) => {
            const row1 =  (
            <TableRow key={idx*2}>
                <TableHead>{`입력 ${io_pair.index}`}</TableHead>
                <TableCell>
                    <span style={{color: `var(--g-dark-txt-color)`}}>{io_pair.input1}</span>
                    <br/>
                    <span>{io_pair.input2}</span>
                </TableCell>
            </TableRow>
            );
            const row2 = (
                <TableRow key={idx*2 + 1}>
                    <TableHead>{`출력 ${io_pair.index}`}</TableHead>
                    <TableCell>
                        <span style={{color: `var(--g-dark-txt-color)`}}>{io_pair.output1}</span>
                        <br/>
                        <span>{io_pair.output2}</span>
                    </TableCell>
                </TableRow>
            )
            return [row1, row2];
        })
    }

    return (
        originalDefData && ioData &&
        <div className={styles.references}>
            <p className={styles.title}>* 참고자료</p>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>지시문</TableHead>
                        <TableCell>
                            <span style={{color: `var(--g-dark-txt-color)`}}>{`${originalDefData.definition1}`}</span>
                            <br/>
                            <br/>
                            <span>{`${originalDefData.definition2}`}</span>
                        </TableCell>
                    </TableRow>
                    {makeIOPairs()}
                </TableBody>
            </Table>
        </div>
    );
}