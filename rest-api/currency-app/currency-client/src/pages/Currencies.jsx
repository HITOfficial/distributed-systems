import {Box, Button, Typography} from "@mui/material";
import {useContext, useEffect} from "react";
import {CurrenciesListContext} from "../App";
import axios from "axios";
import CurrenciesSelect from "../components/CurrenciesSelect";
import ControlledDatePicker from "../components/ControlledDatePicker";
import CurrencyRatesTable from "../components/CurrencyRatesTable";
import {useNavigate} from "react-router-dom";

const CURRENCIES_LIST_URL = "http://localhost:3002/currencies-list"
const CURRENCY_RATE_URL = "http://localhost:3002/currency-rate"

const Currencies = () => {
    const {currencies, setCurrencies} = useContext(CurrenciesListContext)

    const {
        currenciesList,
        actualCurrency,
        currenciesLoaded,
        currenciesError,
        selectedDate,
        currencyRates
    } = currencies

    const navigation = useNavigate()

    const handleCurrencyChange = (event) => {
        setCurrencies((prevState) => ({
            ...prevState,
            actualCurrency: event.target.value
        }))
    }

    const handleDateChange = (date) => {
        setCurrencies((prevState) => ({
            ...prevState,
            selectedDate: date
        }))
    }

    const fetchCurrenciesList = async () => {
        const response = await axios.get(CURRENCIES_LIST_URL)
        if (response.status === 200) {
            setCurrencies((prevState) => ({
                ...prevState,
                currenciesList: response.data,
                currenciesLoaded: true
            }))
            return
        }
        setCurrencies((prevState) => ({
            ...prevState,
            currenciesError: true
        }))
    }

    const fetchCurrencyRate = async () => {
        try {
            const response = await axios.get(CURRENCY_RATE_URL, {
                params: {
                    curr: actualCurrency,
                    date: selectedDate.format("YYYY-MM-DD")
                }
            })
            if (response.status === 200) {
                setCurrencies((prevState) => ({
                    ...prevState,
                    currencyRates: response.data
                }))
            }
        } catch {
            setCurrencies((prevState) => ({
                ...prevState,
                currencyRates: []
            }))
        }

    }

    useEffect(() => {
        if (actualCurrency && selectedDate) {
            fetchCurrencyRate()
        }
    }, [actualCurrency, selectedDate])




    return (
        <Box>
            <Button  size="small" variant="contained" onClick={() => navigation("/Home")}>Go to Home</Button>
            <Box>
                <Box sx={{
                    display:"flex",
                    justifyContent:"center",
                }}>
                    {
                        !currencies.currenciesLoaded ?
                            <Button variant="contained" onClick={fetchCurrenciesList}>fetch currencies list</Button>
                            :
                            <Box>
                                <CurrenciesSelect currenciesList={currenciesList} onChange={handleCurrencyChange} />
                                <ControlledDatePicker selectedDate={selectedDate} onChange={handleDateChange}/>
                            </Box>
                    }
                </Box>
                {
                    currenciesLoaded && (
                        <CurrencyRatesTable
                            rates={currencyRates}
                        />
                    )
                }
            </Box>
        </Box>
    )
}

export default Currencies