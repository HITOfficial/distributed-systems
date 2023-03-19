const axios = require('axios');
const API_BASE_URL = process.env.API_BASE_URL;


exports.getExchangeRates = async (req, res) => {
    try {
        const {date, curr} = req.query;
        if (!date || !curr) {
            res.status(400).send("Please provide a date and currency")
            return
        }
        const response = await axios.get(`${API_BASE_URL}/${date}/currencies/${curr}.min.json`);
        const {data, statusText, status} = response
        if (status == 200) {
            const [_, rates] = Object.entries(data)
            const r = rates[1]
            res.json(Object.keys(r).map(key => ({[key]: r[key]})))
            return
        }
        res.status(status).send(statusText)
    } catch {
        res.status(500).send("Server error")
    }
}

exports.getCurrencies = async (req, res) => {
    try {
        const response =  await axios.get(`${API_BASE_URL}/latest/currencies.min.json`)
        const {data, statusText, status} = response
        if(status == 200){
            res.json(Object.keys(response.data).map(key =>  ({ [key]: data[key] })))
            return
        }
        res.status(status).send(statusText)
    } catch {
        res.status(500).send("Server error")

    }
}
