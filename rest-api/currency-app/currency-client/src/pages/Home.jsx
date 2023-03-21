import {Box, Button, Typography} from "@mui/material";
import {useNavigate} from "react-router-dom";

const Home = () => {
    const x= 3

    const navigation = useNavigate()
    return (
        <Box sx={{
            display:"flex",
            flexDirection:"column",
            alignItems:"center",
            height:"100%",
        }}>
            <Typography variant="h3">Home
            </Typography>
            <Box sx={{
                marginTop:"15rem",
                display:"flex",
                flexDirection:"column",
                height:"5rem",
                justifyContent:"space-between",
            }}>
                <Button variant="contained" onClick={() => navigation('../currencies')}>
                    Go to Currencies
                </Button>
                <Button
                    variant={"contained"} onClick={() => {
                    localStorage.removeItem('accessToken')
                    navigation('../login')
                }}>Logout</Button>
            </Box>
        </Box>
    )
}

export default Home