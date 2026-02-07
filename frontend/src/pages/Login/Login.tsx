import { Box, Container, Typography, TextField, Button } from '@mui/material';
import { useState, type FormEvent } from 'react';

export default function Login() {

    const [accountSid, setAccountSid] = useState('');
    const [authToken, setAuthToken] = useState('');

    const handleSubmit = (event: FormEvent<HTMLFormElement>) => {
        event.preventDefault();
        console.log('Account SID: ', accountSid, 'Password: ', authToken);
    }

    return (
        <Container sx={{ height: "100vh" }}>
            <Box height="100%" width="100%" display="flex" justifyContent="center" alignItems="center">
                <form noValidate autoComplete="off" onSubmit={handleSubmit}>
                    <Box bgcolor="#ffffff" height="500px" width="600px" borderRadius="20px" display="flex" justifyContent="center" alignItems="center" flexDirection="column" gap="10px">
                        <Typography variant='h3' color='#000000'>Welcome</Typography>
                        <TextField
                            label="Account SID"
                            variant="outlined"
                            placeholder="Account SID"
                            value={accountSid}
                            onInput={e => setAccountSid((e.target as HTMLInputElement).value)}
                            required
                            sx={{
                                width: "32em",
                                borderWidth: "1px",
                                "& .MuiOutlinedInput-root": {
                                    "& fieldset": {
                                        borderColor: "rgb(155, 155, 155)",
                                        borderWidth: "1px",
                                    }
                                },
                                "&:hover fieldset": {
                                    borderColor: "blue",
                                },
                            }}
                        />
                        <TextField
                            label="Auth Token"
                            variant="outlined"
                            type="password"
                            value={authToken}
                            onInput={e => setAuthToken((e.target as HTMLInputElement).value)}
                            required
                            sx={{
                                borderWidth: "1px",
                                "& .MuiOutlinedInput-root": {
                                    "& fieldset": {
                                        borderColor: "rgb(155, 155, 155)",
                                        borderWidth: "1px",
                                    }
                                },
                                width: "32em",
                            }}
                        />
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            size="large"
                            sx={{ p: 2 }}
                        >
                            Continue
                        </Button>

                    </Box>
                </form>
            </Box>

        </Container >
    )
}