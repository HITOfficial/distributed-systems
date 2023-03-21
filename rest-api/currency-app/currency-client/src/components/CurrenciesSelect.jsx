import {FormControl, InputLabel, MenuItem, Select, Typography} from "@mui/material";

const CurrenciesSelect = ({currenciesList, onChange}) => {
    return (
        <FormControl>
            <InputLabel>CurrenciesSelect</InputLabel>
            <Select
            label="CurrenciesSelect"
            onChange={onChange}
            defaultValue="eur"
            style={{
            width: "16.9375rem"}
            }
            >
                {
                    currenciesList?.map(obj => {
                        const [[key, value]] = Object.entries(obj)
                        return (
                            <MenuItem
                                sx={{
                                    display:"flex",
                                    justifyContent:"space-between"
                                }}
                                key={key} value={key}>
                                <Typography >{value}</Typography>
                                <Typography
                                    textTransform="uppercase"
                                    fontWeight="bold"
                                >
                                {key} </Typography>
                            </MenuItem>
                        )
                    })
                }


            </Select>
        </FormControl>
    )
}

export default CurrenciesSelect