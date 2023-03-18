require('dotenv').config()
const express = require('express');
const  router =   require("./src/routes/currencyRouter")

const app = express();

app.use(express.json());
app.use('/', router)

const PORT = process.env.PORT;

app.listen(PORT, () => {
    console.log(`Server listening on port ${PORT}`)
})