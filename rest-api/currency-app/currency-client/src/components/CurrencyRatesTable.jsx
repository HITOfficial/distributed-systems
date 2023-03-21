import {Table, TableCell, TableBody, TableRow, TableHead, TableContainer} from "@mui/material";

const CurrencyRatesTable   = ({ rates }) => {
    return (
        <TableContainer
            sx={{
            marginTop:"1rem",
            width: "100%",
                height:"34rem",
            overflow:"auto",
            }}
        >
        <Table sx={{
            backgroundColor:"rgba(255,255,255,0.8)",
            display: "table"
        }}
        stickyHeader
        >
            <TableHead>
                <TableRow sx={{
                    textTransform:"uppercase",
                }}>
                    <TableCell>Currency</TableCell>
                    <TableCell
                    sx={{
                        textAlign:"right"
                    }}
                    >Rate</TableCell>
                </TableRow>
                </TableHead>
            <TableBody >
                {
                    rates?.map(obj => {
                        const [[key, value]] = Object.entries(obj)
                        return (
                            <TableRow key={key}>
                                <TableCell>{key}</TableCell>
                                <TableCell sx={{
                                    textAlign:"right"
                                }}>{value}</TableCell>
                            </TableRow>
                        )
                    })
                }
            </TableBody>
        </Table>
        </TableContainer>
    )
}

export default CurrencyRatesTable