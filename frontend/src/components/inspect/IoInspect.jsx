import React, { useState } from 'react';
import { TableCell, TableHead, TableRow } from '../ui/table/Table';

export default function IoInspect({ data, idx }) {
    const row1 = (
        <Input data={data.input} idx={idx} key={idx*2}/>
    )
    const row2 = (
        <Output data={data.output} idx={idx} key={idx*2+1}/>
    );
    return (
        // console.log(data)
        data && [row1, row2]
    );
}

function Input({ data, idx }) {
    return (
        <TableRow>
            <TableHead>{`입력${idx+1}`}</TableHead>
            <TableCell>{`${data}`}</TableCell>
        </TableRow>
    );
}

function Output({ data, idx }) {
    return (
        <TableRow>
            <TableHead>{`출력${idx+1}`}</TableHead>
            <TableCell>
                {`${data}`}
                <input type="checkbox"/>
            </TableCell>
        </TableRow>
    );
}

