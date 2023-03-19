require('dotenv').config()
const express = require('express');
const  router =   require("./src/routes/currencyRouter")

const app = express();

// allow cors
app.use((req, res, next) => {
    res.header('Access-Control-Allow-Origin', '*');
    res.header('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
    next();
})
app.use(express.json());
app.use('/', router)

const PORT = process.env.PORT;

app.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`)
})