import React from 'react';
import {Navigate, Route} from 'react-router-dom';

const PrivateRoute = ({ children}) => {
    if (localStorage.getItem('accessToken') !== null) {
        return children;
    }

    return (
        <Navigate to='/login' />
    )
}

export default PrivateRoute;