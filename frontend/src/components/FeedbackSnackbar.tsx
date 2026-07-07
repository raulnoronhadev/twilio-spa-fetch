import { Alert, Snackbar } from '@mui/material';

export interface Feedback {
    severity: 'success' | 'error';
    message: string;
}

interface FeedbackSnackbarProps {
    feedback: Feedback | null;
    onClose: () => void;
}

export default function FeedbackSnackbar({ feedback, onClose }: FeedbackSnackbarProps) {
    return (
        <Snackbar
            open={feedback !== null}
            autoHideDuration={6000}
            onClose={onClose}
            anchorOrigin={{ vertical: 'bottom', horizontal: 'center' }}
        >
            <Alert severity={feedback?.severity} onClose={onClose} sx={{ width: '100%' }}>
                {feedback?.message}
            </Alert>
        </Snackbar>
    );
}
