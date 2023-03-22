import React from 'react';
import styles from './IoReference.module.css';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';

export default function IoReference({ idx }) {
    return (
        idx && <div className={styles.ioReference}>
            <div className={styles.header}>
                <p className={styles.title}>* 예시</p>
                <div className={styles.wrapper}>
                    <Table>
                        <TableBody>
                            <TableRow>
                                <TableHead>지시문</TableHead>
                                <TableCell>{}</TableCell>
                            </TableRow>
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

