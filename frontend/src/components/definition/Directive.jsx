import React from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './Directive.module.css';

export default function Directive({ data }) {
    return (
        data && 
        <div className={styles.directive}>
            <Table>
                <TableBody>
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문
                        </TableHead>
                        <TableCell>
                            {data.instruction}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead align={'center'}>
                            기계 번역문1
                        </TableHead>
                        <TableCell>
                            {data.definition_kor}
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}

