import { useState } from 'react';
import {
    Alert, Box, Button, CircularProgress, Dialog, DialogActions,
    DialogContent, DialogTitle, MenuItem, TextField, Typography,
} from '@mui/material';
import { useBackups } from '../hooks/useTwilioQueries';

interface RestoreBackupDialogProps {
    open: boolean;
    title: string;
    /** S3 key prefix: "flows/" for Studio Flows, "workspace/" for TaskRouter. */
    prefix: string;
    restoring: boolean;
    onClose: () => void;
    onRestore: (fileName: string) => void;
}

function formatSize(bytes: number): string {
    if (bytes < 1024) return `${bytes} B`;
    if (bytes < 1024 * 1024) return `${(bytes / 1024).toFixed(1)} KB`;
    return `${(bytes / (1024 * 1024)).toFixed(1)} MB`;
}

export default function RestoreBackupDialog({
    open, title, prefix, restoring, onClose, onRestore,
}: RestoreBackupDialogProps) {
    const [fileName, setFileName] = useState('');
    const { data: backups, isLoading, isError, error } = useBackups(prefix, open);

    const handleClose = () => {
        setFileName('');
        onClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} fullWidth>
            <DialogTitle>{title}</DialogTitle>
            <DialogContent>
                {isLoading && (
                    <Box display="flex" justifyContent="center" p={3}>
                        <CircularProgress size={28} />
                    </Box>
                )}
                {isError && <Alert severity="error">{error.message}</Alert>}
                {backups && backups.length === 0 && (
                    <Alert severity="info">No backup files found.</Alert>
                )}
                {backups && backups.length > 0 && (
                    <TextField
                        select
                        fullWidth
                        margin="dense"
                        label="Backup file"
                        value={fileName}
                        onChange={(e) => setFileName(e.target.value)}
                    >
                        {backups.map((backup) => (
                            <MenuItem key={backup.fileName} value={backup.fileName}>
                                <Box>
                                    <Typography variant="body2">{backup.fileName}</Typography>
                                    <Typography variant="caption" color="text.secondary">
                                        {new Date(backup.lastModified).toLocaleString()} — {formatSize(backup.size)}
                                    </Typography>
                                </Box>
                            </MenuItem>
                        ))}
                    </TextField>
                )}
            </DialogContent>
            <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button
                    variant="contained"
                    disabled={fileName === '' || restoring}
                    onClick={() => onRestore(fileName)}
                >
                    {restoring ? 'Restoring...' : 'Restore'}
                </Button>
            </DialogActions>
        </Dialog>
    );
}
