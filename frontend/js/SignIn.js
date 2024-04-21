import * as React from 'react';
import { useNavigate } from 'react-router-dom';
import Avatar from '@mui/material/Avatar';
import Button from '@mui/material/Button';
import CssBaseline from '@mui/material/CssBaseline';
import TextField from '@mui/material/TextField';
import FormControlLabel from '@mui/material/FormControlLabel';
import Checkbox from '@mui/material/Checkbox';
import Link from '@mui/material/Link';
import Grid from '@mui/material/Grid';
import Box from '@mui/material/Box';
import LockOutlinedIcon from '@mui/icons-material/LockOutlined';
import Typography from '@mui/material/Typography';
import Container from '@mui/material/Container';
import { createTheme, ThemeProvider } from '@mui/material/styles';
import axios from "axios";

const defaultTheme = createTheme();

export default function SignIn(props) {
    const navigate = useNavigate();

    function getAxiosConfiguration() {
        return {
            timeout: 3000,
            signal: AbortSignal.timeout(6000)
        };
    }

    async function fetchUserDetails(username, password) {
        return new Promise((resolve) => {
            setTimeout(() => {
                resolve(
                    axios.post(`/aurum/api/auth/signin?userName=${username}&password=${password}`, getAxiosConfiguration())
                        .then(response => {
                            if (response.data !== null) {
                                if (response.data.userName === username && response.data.password === password) {
                                    console.log('data matches');
                                    return response;
                                }
                            }
                        })
                        .catch(error => {
                            console.log(error);
                        })
                );
            }, 1000);
        });
    }


    const handleSubmit = (event) => {
        event.preventDefault();
        const data = new FormData(event.currentTarget);
        const username = data.get('username');
        const password = data.get('password');

        fetchUserDetails(username, password).then(response => {
            if (response === undefined) {
                console.log('unsuccessful');
            } else if (response.status === 200 && response.data.password === password) {
                console.log("success");
                console.log(response.data);

                // clear the form
                document.getElementById('username').value = '';
                document.getElementById('password').value = '';

                let clientInfo = {
                    userName: response.data.userName,
                    emailAddress: response.data.emailAddress,
                    firstName: response.data.firstName,
                    lastName: response.data.lastName,
                    clientId: response.data.clientId
                };
                sessionStorage.setItem('clientInfo', JSON.stringify(clientInfo));
                window.location.href = "/";
            } else {
                console.log('unsuccessful');
            }
        });

        // clear the form
        document.getElementById('username').value = '';
        document.getElementById('password').value = '';
    };

    return (
         <ThemeProvider theme={defaultTheme}>
            <Container component="main" maxWidth="xs">
                <CssBaseline />
                <Box
                    sx={{
                        marginTop: 8,
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                    }}
                >
                    <Avatar sx={{ m: 1, bgcolor: 'secondary.main' }}>
                        <LockOutlinedIcon />
                    </Avatar>
                    <Typography component="h1" variant="h5">
                        Sign in - aurum
                    </Typography>
                    <Box component="form" onSubmit={handleSubmit} noValidate sx={{ mt: 1 }}>
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            id="username"
                            label="User Name"
                            name="username"
                            autoComplete="username"
                            autoFocus
                        />
                        <TextField
                            margin="normal"
                            required
                            fullWidth
                            name="password"
                            label="Password"
                            type="password"
                            id="password"
                            autoComplete="current-password"
                        />
                        <FormControlLabel
                            control={<Checkbox value="remember" color="primary" />}
                            label="Remember me"
                        />
                        <Button
                            type="submit"
                            fullWidth
                            variant="contained"
                            sx={{ mt: 3, mb: 2 }}
                        >
                            Sign In
                        </Button>
                        <Grid container>
                            <Grid item xs>
                                <Link href="#" variant="body2">
                                    Forgot password?
                                </Link>
                            </Grid>
                            <Grid item>
                                <Link href="#" variant="body2">
                                    {"Don't have an account? Sign Up"}
                                </Link>
                            </Grid>
                        </Grid>
                    </Box>
                </Box>
            </Container>
        </ThemeProvider>
    );
}