
const express = require('express');
const {getExchangeRates, getCurrencies} = require("../controllers/currencyController");



const router = express.Router();

router.get('/currencies', getExchangeRates);
router.get('/currencies-list', getCurrencies);

module.exports = router;