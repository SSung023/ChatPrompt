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
                            {/* <span>{io_pair.input1}</span>
                            <br/>
                            <span style={{color: `var(--lowb-main-color)`}}>{io_pair.input2}</span> */}
                            <span style={{color: `var(--g-dark-txt-color)`}}>{io_pair.input1}</span>
                            <br/>
                            <span>{io_pair.input2}</span>
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead>{`출력 ${io_pair.index}`}</TableHead>
                        <TableCell>
                            {/* <span>{io_pair.output1}</span>
                            <br/>
                            <span style={{color: `var(--lowb-main-color)`}}>{io_pair.output2}</span> */}
                            <span style={{color: `var(--g-dark-txt-color)`}}>{io_pair.output1}</span>
                            <br/>
                            <span>{io_pair.output2}</span>
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
                            <span style={{color: `var(--g-dark-txt-color)`}}>{defData.definition_eng}</span>
                            <br/>
                            <br/>
                            <span>{defData.definition_kor}</span>
                        </TableCell>
                    </TableRow>
                    {makeIOPairs()}
                </TableBody>
            </Table>
        </div>
    );
}