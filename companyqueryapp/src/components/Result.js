import React, { useEffect, useState } from "react";
import { Button, Card, CardContent, TextField, Typography } from "@mui/material";
import Center from "./Center";
import { createAPIEndpoint, ENDPOINTS } from "../api";
import useStateContext from '../hooks/useStateContext'

export default function Result(){
    const[result,setResult] = useState([])
    const{context, setContext} = useStateContext()
    
    // useEffect(()=>{
    //     createAPIEndpoint(ENDPOINTS.companies)
    //     .fetchById(context.companyId)
    //     .then(res=>{
    //         setResult(res.data)
    //         console.log(res.data)
    //     })
    //     .catch(err=>{console.log(err);})
    // },[])

    useEffect(()=>{
        createAPIEndpoint(ENDPOINTS.companies)
        .fetchById(context.companyId)
        .then(res=>{
            setResult(res.data)
            console.log(res.data)
        })
        .catch(err=>{console.log(err);})
    },[])

    return(
        <Center>
        <Card sx={{width:400}}>
            <CardContent sx={{textAlign:"center"}}>
                <Typography variant="h3" sx={{my:3}}>
                 Results:
                </Typography>
                <Typography variant="h6">
                    Based on your score {result.companyName}: {result.answers}.
                </Typography>
        </CardContent>
        </Card>
        </Center>
    )

}