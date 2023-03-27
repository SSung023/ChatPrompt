import React from 'react';
import Table, { TableBody, TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './Directive.module.css';

export default function Directive({ defData, originalDefData }) {
    return (
        defData && originalDefData &&
        <div className={styles.directive}>
            {/* <p className={styles.info}>* 지시문 원문</p> */}
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
                </TableBody>
            </Table>

            {/* <p className={styles.info}>* 지시문</p> */}
            <Table>
                <TableBody>
                    
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문1
                        </TableHead>
                        <TableCell>
                            {defData.definition1}
                        </TableCell>
                    </TableRow>
                    <TableRow>
                        <TableHead align={'center'}>
                            지시문2
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

