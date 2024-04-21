import Typography from "@mui/material/Typography";
import Link from "@mui/material/Link";
import * as React from "react";
import SignIn from "./SignIn";
import Dashboard from "./dashboard/Dashboard";

function Copyright(props) {
    return (
        <Typography variant="body2" color="text.secondary" align="center" {...props}>
            {'Copyright © '}
            <Link color="inherit" href="https://mui.com/">
                aurum
            </Link>{' '}
            {new Date().getFullYear()}
            {'.'}
        </Typography>
    );
}

export default function Container() {
    let showSignInComponent = true;
    let showDashboardComponent = false;

    const clientInfoQuery = sessionStorage.getItem('clientInfo');
    if (clientInfoQuery !== null && clientInfoQuery !== undefined) {
        showSignInComponent = false;
        showDashboardComponent = true;
        console.log(JSON.stringify(clientInfoQuery.userName));
    }

    return (
        <div>
            {showSignInComponent && <SignIn />}
            {showDashboardComponent && <Dashboard clientInfo={clientInfoQuery}/>}
            <Copyright />
        </div>
    );
}
