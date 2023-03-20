import React from 'react';
import Table, { TableCell, TableHead, TableRow } from '../ui/table/Table';
import styles from './Directive.module.css';

export default function Directive() {
    return (
        <div className={styles.directive}>
            <Table>
                <TableRow>
                    <TableHead align={'center'}>
                        지시문
                    </TableHead>
                    <TableCell style={{ padding: `1em` }}>
                        (수정중) /prompt/preparation/refinedDefinition.txt에서 읽어서 추출하기 이 작업은 중첩된 기본 자연어 명령을 따르고 기본 논리 및 조건문을 포함하여 일련의 작업을 수행하는 능력을 평가합니다.
                    </TableCell>
                </TableRow>
                <TableRow>
                    <TableHead align={'center'}>
                        기계 번역문1
                    </TableHead>
                    <TableCell style={{ padding: `1em` }}>
                        이 작업은 중첩된 기본 자연어 명령을 따르고 기본 논리 및 조건문을 포함하여 일련의 작업을 수행하는 능력을 평가합니다.
                    </TableCell>
                </TableRow>
            </Table>
        </div>
    );
}

