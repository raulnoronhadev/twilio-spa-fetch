import {
    DataGrid,
    type GridColDef,
    type GridPaginationModel,
    type GridRowIdGetter,
    type GridValidRowModel,
} from '@mui/x-data-grid';
import Paper from '@mui/material/Paper';

export interface ServerPaginationProps {
    paginationModel: GridPaginationModel;
    onPaginationModelChange: (model: GridPaginationModel) => void;
    hasNextPage: boolean;
}

interface DataTableProps<R extends GridValidRowModel> {
    rows: R[];
    columns: GridColDef<R>[];
    loading?: boolean;
    getRowId?: GridRowIdGetter<R>;
    /** When provided, pagination is driven by the API's cursor tokens. */
    serverPagination?: ServerPaginationProps;
}

export default function DataTable<R extends GridValidRowModel>({
    rows, columns, loading, getRowId, serverPagination,
}: DataTableProps<R>) {
    return (
        <Paper sx={{ height: '100%', width: '100%', minHeight: 400 }}>
            <DataGrid
                rows={rows}
                columns={columns}
                loading={loading}
                getRowId={getRowId}
                disableRowSelectionOnClick
                pageSizeOptions={[10, 20, 50]}
                sx={{ border: 0 }}
                {...(serverPagination
                    ? {
                        paginationMode: 'server' as const,
                        // rowCount is unknown with cursor pagination; the grid relies
                        // on hasNextPage to enable/disable the "next" button.
                        rowCount: -1,
                        paginationMeta: { hasNextPage: serverPagination.hasNextPage },
                        paginationModel: serverPagination.paginationModel,
                        onPaginationModelChange: serverPagination.onPaginationModelChange,
                    }
                    : {
                        initialState: { pagination: { paginationModel: { page: 0, pageSize: 20 } } },
                    })}
            />
        </Paper>
    );
}
