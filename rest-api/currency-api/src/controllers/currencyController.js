const axios = require('axios');
const API_BASE_URL = process.env.API_BASE_URL;


exports.getExchangeRates = async (req, res) => {
    const { date, curr} = req.query;
    const response = await axios.get(`${API_BASE_URL}/${date}/currencies/${curr}.json`);
    if(response.statusText == "OK"){
        console.log(response.status, response.statusText)
        res.json(response.data)
        return
    }
    console.error(response.statusText)
    res.status(response.status).send(response.statusText)
};

exports.getCurrencies = async (req, res) => {
    const response = await axios.get(`${API_BASE_URL}/latest/currencies.json`)
    if(response.statusText == "OK"){
        console.log(response.status, response.statusText)
        res.json(response.data)
        return
    }
        console.error(response.statusText)
        res.status(response.status).send(response.statusText)
}
