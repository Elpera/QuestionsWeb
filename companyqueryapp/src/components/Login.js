
import React, { useEffect, useState } from "react";
import { Button, Card, CardContent, TextField, Typography } from "@mui/material";
import { Box } from "@mui/system";
import Center from "./Center";
import useForm from "../hooks/useForm";
import { createAPIEndpoint, ENDPOINTS } from "../api";
import useStateContext from "../hooks/useStateContext";
import { useNavigate } from "react-router-dom";

const getFreshModel= () =>({
    companyname:'',
    email:''
})


export default function Login(){
    const { context, setContext, resetContext } = useStateContext();
    const navigate =useNavigate()

    const {
        values,
        setValues,
        errors,
        setErrors,
        handleInputChange
    } = useForm(getFreshModel);

    useEffect(()=>{
        resetContext()

    },[])

    const login = e => {
        e.preventDefault();
        if (validate())
            createAPIEndpoint(ENDPOINTS.companies)
                .post(values)
                .then(res => {
                    setContext({ companyId: res.data.companyId })
                    navigate('/quiz')
                })
                .catch(err => console.log(err))
    }
   
    const validate = () =>{
        let temp={}
        temp.email = (/\S+@\S+\.\S+/).test(values.email) ? "" : "Email is not valid."
        temp.companyname = values.companyname != "" ? "" : "This field is required."
        setErrors(temp)
        return Object.values(temp).every(x => x == "")
    }



    return(
        <Center>
        <Card sx={{width:400}}>
            <CardContent sx={{textAlign:"center"}}>
                <Typography variant="h3" sx={{my:3}}>
                 Questionnaire
                </Typography>

        <Box sx={{
            '&  .MuiTextField-root':{
                margin:1,
                width:'90%'
            }
        }}>
        <form noValidate autoComplete="off" onSubmit={login}>
            <TextField
            id="email"
            label="Email"
            name ="email"
            value={values.email}
            onChange={handleInputChange}
            variant = "outlined"
            {...(errors.email &&  {error:true, helperText:errors.email})}
            >

            </TextField>

            <TextField
            id="companyname"
            label="Company Name"
            name ="companyname"
            value={values.companyname}
            onChange={handleInputChange}
            variant = "outlined"
            {...(errors.companyname &&  {error:true, helperText:errors.companyname})}
            >
            </TextField>

            
            <Button 
            id="start"
            type="submit"
            variant="contained"
            size="large"
            sx={{
                width:'90%'
            }}>
            Start
            </Button>
        </form>
        </Box>
        </CardContent>
        </Card>
        </Center>
    )
}


