import React, { useEffect } from 'react';
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
                            지시문1
                        </TableHead>
                        <TableCell>
                            {data.definition1}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문2
                        </TableHead>
                        <TableCell>
                            {data.definition2}
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    );
}

