import { Alert, Box, Typography } from '@mui/material';
import type { GridColDef } from '@mui/x-data-grid';
import DataTable from '../../components/DataTable';
import { usePhoneNumbers } from '../../hooks/useTwilioQueries';
import type { PhoneDTO } from '../../types/twilio';

const columns: GridColDef<PhoneDTO>[] = [
    {
        field: 'phone_number', headerName: 'Phone Number', width: 180,
        valueGetter: (_, row) => row.phone_number?.endpoint ?? '',
    },
    { field: 'friendly_name', headerName: 'Name', flex: 1, minWidth: 180 },
    { field: 'phone_number_sid', headerName: 'SID', flex: 1, minWidth: 280 },
    { field: 'status', headerName: 'Status', width: 120 },
    { field: 'origin', headerName: 'Origin', width: 120 },
    { field: 'voice_url', headerName: 'Voice URL', flex: 1, minWidth: 200 },
    { field: 'smsUrl', headerName: 'SMS URL', flex: 1, minWidth: 200 },
];

export default function PhoneNumbers() {
    const { data: phoneNumbers, isLoading, isError, error } = usePhoneNumbers();

    return (
        <Box>
            <Typography variant="h5" mb={2}>Phone Numbers</Typography>
            {isError && <Alert severity="error" sx={{ mb: 2 }}>{error.message}</Alert>}
            <DataTable
                rows={phoneNumbers ?? []}
                columns={columns}
                loading={isLoading}
                getRowId={(row) => row.phone_number_sid ?? row.phone_number?.endpoint ?? row.friendly_name}
            />
        </Box>
    );
}
