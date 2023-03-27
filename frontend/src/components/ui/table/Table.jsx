import React from 'react';
import styles from './Table.module.css';

export default function Table({ children, style }) {
    return (
        <table 
            className={styles.table} 
            style={style}
        >{children}</table>
    );
}

export function TableHead({ children, align, style, rowspan, colspan }) {
    return (
        <th 
            className={styles.th} 
            align={align} 
            style={style}
            colSpan={colspan ? `${colspan}` : ''}
            rowSpan={rowspan ? `${rowspan}` : ''}
        >{children}</th>
    )
}

export function TableBody({ children, style, rowspan, colspan }){
    return (
        <tbody 
            className={styles.tbody} 
            style={style}
            colSpan={colspan ? `${colspan}` : ''}
            rowSpan={rowspan ? `${rowspan}` : ''}
        >{children}</tbody>
    )
}

export function TableRow({ children, style, rowspan, colspan }) {
    return (
        <tr 
            className={styles.tr} 
            style={style}
            colSpan={colspan ? `${colspan}` : ''}
            rowSpan={rowspan ? `${rowspan}` : ''}
        >{children}
        </tr>
    )
}

export function TableCell({ children, style, rowspan, colspan }) {
    return (
        <td 
            className={styles.td} 
            style={style}
            colSpan={colspan ? `${colspan}` : ''}
            rowSpan={rowspan ? `${rowspan}` : ''}
        >{children}</td>
    )
}