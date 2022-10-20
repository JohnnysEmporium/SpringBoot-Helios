import './css/EntryField.css'

function EntryField(props){
    return(
        <div className='container'>
            <label>{props.labelText}</label>
            <input className='input' type={props.type}/>
        </div>
    );
}

EntryField.defaultProps = {
    type: "text"
}

export default EntryField;