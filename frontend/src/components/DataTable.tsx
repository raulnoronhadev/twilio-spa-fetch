import { DataGrid, type GridColDef, type GridRowIdGetter, type GridValidRowModel } from '@mui/x-data-grid';
import Paper from '@mui/material/Paper';

interface DataTableProps<R extends GridValidRowModel> {
    rows: R[];
    columns: GridColDef<R>[];
    loading?: boolean;
    getRowId?: GridRowIdGetter<R>;
}

export default function DataTable<R extends GridValidRowModel>({ rows, columns, loading, getRowId }: DataTableProps<R>) {
    return (
        <Paper sx={{ height: '100%', width: '100%', minHeight: 400 }}>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                getRowId={getRowId}
                initialState={{ pagination: { paginationModel: { page: 0, pageSize: 15 } } }}
                pageSizeOptions={[5, 10, 15, 25]}
                disableRowSelectionOnClick
                sx={{ border: 0 }}
            />
        </Paper>
    );
}
