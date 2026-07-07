import { useCallback, useState } from 'react';
import type { GridPaginationModel } from '@mui/x-data-grid';

/**
 * Bridges MUI DataGrid page-index pagination with the API's cursor tokens:
 * keeps a map of page index -> pageToken, filled as pages are visited.
 */
export function useServerPagination(defaultPageSize = 20) {
    const [paginationModel, setPaginationModel] = useState<GridPaginationModel>({
        page: 0,
        pageSize: defaultPageSize,
    });
    const [pageTokens, setPageTokens] = useState<Record<number, string>>({});

    const pageToken: string | undefined = pageTokens[paginationModel.page];

    const onPaginationModelChange = useCallback((model: GridPaginationModel) => {
        setPaginationModel((current) => {
            if (model.pageSize !== current.pageSize) {
                // Page size changed: cursors are no longer valid, restart from page 0.
                setPageTokens({});
                return { page: 0, pageSize: model.pageSize };
            }
            return model;
        });
    }, []);

    /** Call whenever a page response arrives to enable navigation to the next page. */
    const nextPage = paginationModel.page + 1;
    const registerNextPageToken = useCallback((nextToken: string | null | undefined) => {
        if (!nextToken) return;
        setPageTokens((tokens) =>
            tokens[nextPage] === nextToken ? tokens : { ...tokens, [nextPage]: nextToken }
        );
    }, [nextPage]);

    return { paginationModel, pageToken, onPaginationModelChange, registerNextPageToken };
}
