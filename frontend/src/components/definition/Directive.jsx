import React from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './Directive.module.css';

export default function Directive({ defData, originalDefData }) {
    return (
        defData && originalDefData &&
        <div className={styles.directive}>
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
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문 1
                        </TableHead>
                        <TableCell>
                            {defData.definition1}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문 2
                        </TableHead>
                        <TableCell>
                            {defData.definition2}
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}

