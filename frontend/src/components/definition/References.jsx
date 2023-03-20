import React, { useContext, useEffect, useState } from 'react';
import { userContext } from '../../context/UserContext';
import Table, { TableBody, TableHead, TableRow, TableCell } from '../ui/table/Table';
import styles from './References.module.css';
import axios from 'axios';

export default function References({ taskId, defData }) {
    const context = useContext(userContext);
    const [data, setData] = useState();

    useEffect(() => {
        const tId = parseInt(taskId);
        taskId && (axios.get(`/api/tasks/${tId}/io-pairs`)
        .then(function(res) {
            setData(res.data.dataList);
        }))
    }, [taskId]);

    const makeIOPairs = () => {
        return data.map((io_pair) => {
            return (
                <>
                    <TableRow>
                        <TableHead>{`입력 ${io_pair.index}`}</TableHead>
                        <TableCell>
                            {io_pair.input1}
                            <br/>
                            {io_pair.input2}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead>{`출력 ${io_pair.index}`}</TableHead>
                        <TableCell>
                            {io_pair.output1}
                            <br/>
                            {io_pair.output2}
                        </TableCell>
                    </TableRow>
                </>
            );
        })
    }

    return (
        data &&
        <div className={styles.references}>
            <p className={styles.title}>* 참고자료</p>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>지시문</TableHead>
                        <TableCell>
                            {defData.definition_eng}
                            <br/>
                            <br/>
                            {defData.definition_kor}
                        </TableCell>
                    </TableRow>
                    {makeIOPairs()}
                </TableBody>
            </Table>
        </div>
    );
}