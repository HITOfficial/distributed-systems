require('dotenv').config()
const express = require('express');
const  router =   require("./src/routes/currencyRouter")
const passport = require('passport');
const GoogleStrategy = require('passport-google-oauth20').Strategy;
const cookieParser = require('cookie-parser');
const session = require('express-session');

const app = express();

app.use(cookieParser());
app.use(session({
    secret: process.env.SECRET,
    resave: true,
    saveUninitialized: true
}));

passport.use(new GoogleStrategy({
    clientID: process.env.CLIENT_ID,
    clientSecret: process.env.CLIENT_SECRET,
    callbackURL: process.env.CALLBACK_URL
},(accessToken, refreshToken, profile, done) => {
  done(null, profile)
}))

passport.serializeUser((user, done) => {
    done(null, user)
})
passport.deserializeUser((user, done) => {
    done(null, user)
})


app.use(passport.initialize())
app.use(passport.session())


// Define Google OAuth2 routes
app.get('/auth/google',
    passport.authenticate('google', { scope: ['profile', 'email'] }))

app.get('/auth/google/callback',
    passport.authenticate('google', { failureRedirect: '/login' }),
    (req, res) => {
        // Set cookie after successful authentication
        res.cookie('user', JSON.stringify(req.user));
        res.redirect('/');
    })

app.get('/logout', (req, res) => {
    req.logout();
    res.clearCookie('user');
    res.redirect('/');
});

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