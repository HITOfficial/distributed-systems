
const express = require('express');
const {getExchangeRates, getCurrencies} = require("../controllers/currencyController");



const router = express.Router();

router.get('/currency-rate', getExchangeRates);
router.get('/currencies-list', getCurrencies);

module.exports = router;