import React from 'react';
import styles from './Instruction.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';

export default function Instruction({ data }) {
    return (
        data &&
        <div className={styles.instruction}>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead>지시문 1</TableHead>
                        <TableCell>{data.similarInstruct1}</TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead>지시문 2</TableHead>
                        <TableCell>{data.similarInstruct2}</TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}