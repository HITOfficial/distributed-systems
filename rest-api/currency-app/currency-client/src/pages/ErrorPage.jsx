import {useNavigate} from "react-router-dom";
import {Box, Button, styled, Typography} from "@mui/material";

const ErrorPageWrapper = styled(Box)({
    display: "flex",
    flexDirection: "column",
    justifyItems: "center",
    alignItems: "center",
    width: "100%",
    height: "100%",
    marginTop:"5rem"
})

const ErrorPage = () => {
    const navigation = useNavigate()
    return (
<ErrorPageWrapper>
    <Typography >404 Page not found</Typography>
    <Button variant="contained" onClick={() => navigation("/")}>Go back to Home</Button>
</ErrorPageWrapper>
    )
}

export default ErrorPage