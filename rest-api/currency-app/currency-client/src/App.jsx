import {Container, Paper} from "@mui/material";
import {Outlet} from "react-router-dom";
import {createContext, useState} from "react";
import dayjs from "dayjs";


export const CurrenciesListContext = createContext()

function App() {
    const [currencies, setCurrencies] = useState({
        currenciesList: [],
        actualCurrency: 'eur',
        currenciesListLoaded: false,
        currenciesError: false,
        selectedDate: dayjs("2023-01-01"),
        currencyRates: []
    })


  return (
      <CurrenciesListContext.Provider value={{
          currencies,
            setCurrencies,
      }}>
          <Container
              component={Paper}
              maxWidth="sm" sx={{
              marginTop:"2rem",
              height:"80vh",
              minHeight:"10rem",
              boxSizing:"border-box",
              padding: "1rem .5rem"
          }}>
              <Outlet/>
          </Container>
      </CurrenciesListContext.Provider>
  )
}

export default App;
