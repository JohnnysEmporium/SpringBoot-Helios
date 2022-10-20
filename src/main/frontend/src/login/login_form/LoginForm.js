import EntryField from './../../components/EntryField';
import './LoginForm.css';
import Get from './../../connection/Get';

function LoginForm(){

    const submitForm = () => {
        Get("tetetette")
    }

    return(
        <div className='login-container'>
            <EntryField labelText="Login"/>
            <EntryField labelText="Password" type="password"/>
            <div className='button-div'>
                <button onClick={submitForm}>Login</button>
            </div>
        </div>
    );

}

export default LoginForm;