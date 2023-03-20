import React from 'react';
import styles from './Table.module.css';

export default function Table({ children, style }) {
    return (
        <table className={styles.table} style={style}>{children}</table>
    );
}

export function TableHead({ children, align, style }) {
    return (
        <th className={styles.th} align={align} style={style}>{children}</th>
    )
}

export function TableBody({ children, style }){
    return (
        <tbody className={styles.tbody} style={style}>{children}</tbody>
    )
}

export function TableRow({ children, style }) {
    return (
        <tr className={styles.tr} style={style}>{children}</tr>
    )
}

export function TableCell({ children, style }) {
    return (
        <td className={styles.td} style={style}>{children}</td>
    )
}