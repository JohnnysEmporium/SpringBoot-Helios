import axios from "axios";
import { useEffect, useState } from "react";

function Get(url){
    
    const fetchFromServer = () => {
        // axios.get(props.url).then( res => {
        //         console.log(res)
        // })
        console.log(url)
    }


    useEffect(() => {
        console.log(url)
    })
    return 0;

}

export default Get;