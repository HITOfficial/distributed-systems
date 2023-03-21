import {createBrowserRouter} from "react-router-dom";
import Home from "../pages/Home";
import ErrorPage from "../pages/ErrorPage";
import App from "../App";
import Currencies from "../pages/Currencies";
import Login from "../pages/Login";
import PrivateRoute from "../components/PrivateRoute";

const router = createBrowserRouter([
    {
        path: "/",
        element: <App />,
        errorElement: <PrivateRoute><ErrorPage /></PrivateRoute>,
        children: [
            {
                path: "/login",
                element: <Login />
            },
            {
                path: "/home",
                element: <PrivateRoute><Home /></PrivateRoute>
            },
            {
                path: "/currencies",
                element: <PrivateRoute><Currencies /></PrivateRoute>
            },
            ]
    },
])

export default router;
