import {GoogleLogin, GoogleOAuthProvider} from '@react-oauth/google';
import axios from 'axios';
import {useNavigate} from "react-router-dom";

const Login = () => {

    const navigation = useNavigate()
    const handleGoogleLoginSuccess = async (response) => {
        localStorage.setItem('accessToken', response.credential)
        navigation('/home')
    };

    const handleGoogleLoginFailure = (error) => {
        console.error(error);
        alert('Google login failed. Please try again.');
    }


    return (
        <GoogleOAuthProvider clientId="1056381484856-fpmn9thbnburht4om9rh5hjuv6h6v1bs.apps.googleusercontent.com">
            <GoogleLogin
                buttonText="Login with Google"
                onSuccess={handleGoogleLoginSuccess}
                onFailure={handleGoogleLoginFailure}
                cookiePolicy={'single_host_origin'}
                />
        </GoogleOAuthProvider>

    )
}

export default Login