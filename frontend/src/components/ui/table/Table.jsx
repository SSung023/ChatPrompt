import React from 'react';
import styles from './Table.module.css';

export default function Table({ children }) {
    return (
        <table className={styles.table}>{children}</table>
    );
}

export function TableHead({ children, align }) {
    return (
        <th className={styles.th} align={align}>{children}</th>
    )
}

export function TableBody({ children }){
    return (
        <tbody className={styles.tbody}>{children}</tbody>
    )
}

export function TableRow({ children }) {
    return (
        <tr className={styles.tr}>{children}</tr>
    )
}

export function TableCell({ children, style }) {
    return (
        <td className={styles.td} style={style}>{children}</td>
    )
}