import { Card, CardContent, CardHeader, List, ListItemButton, Typography } from "@mui/material";
import React, { useContext, useEffect, useState } from "react";
import { createAPIEndpoint, ENDPOINTS } from "../api";
import useStateContext from '../hooks/useStateContext'
import { useNavigate } from "react-router-dom";

export default function Quiz(){
    //  const {context, setContext} = useStateContext()

    //  console.log(context)
    const[qns,setQns] = useState([])
    const[compa,setCompa] = useState([])
    const[qnIndex, setQnIndex] = useState(0)
    const{context, setContext} = useStateContext()
    const navigate =useNavigate()
    useEffect(()=>{
        setContext({
            selectedOptions:[]
        })
        createAPIEndpoint(ENDPOINTS.questions)
        .fetch()
        .then(res=>{
            setQns(res.data)
            console.log(res.data)
        })
        .catch(err=>{console.log(err);})
    },[])

    useEffect(()=>{
        createAPIEndpoint(ENDPOINTS.companies)
        .fetchById(context.companyId)
        .then(res=>{
            setCompa(res.data)
            console.log(res.data)
        })
        .catch(err=>{console.log(err);})
    },[])

    

const updateAnswer =(qnId,optionIndex)=>{
    const temp = [...context.selectedOptions]
    temp.push({
        qnId,
        selected:optionIndex
    })
    if (qnIndex < (qns.length-1)){
        setContext({selectedOptions:[...temp]})
        setQnIndex(qnIndex+1)
    }else {
        setContext({selectedOptions:[...temp]})
        let ans='';
        for (let index = 0; index < context.selectedOptions.length+1; index++) {
            if (index == context.selectedOptions.length){
                ans = ans+optionIndex
            }
            else {
                ans = ans+context.selectedOptions[index].selected+','
            }
            
        }
        compa.answers=ans;//{context.selectedOptions[1].selected}
        createAPIEndpoint(ENDPOINTS.companies)
        .put(context.companyId,compa)
        .then(respo=>{
            //setCompa(respo.data)
            console.log(respo.data)
        })
        .catch(err=>{console.log(err)});
        
        //send put with results (context.selectedOptions[i].selected)
        navigate('/result')

    }
}

    return(
        qns.length!=0
        ? <Card
        sx={{maxWidth:640,mx:'auto',mt:5}}
        >
            <CardHeader title={'Question '+(qnIndex+1) + ' of '+qns.length}></CardHeader>
            <CardContent>
                <Typography variant="h6">
                    {qns[qnIndex].qnInWords}
                </Typography>
                <List>
                    {qns[qnIndex].options.map((item,index)=>
                    <ListItemButton key={index} onClick={()=>updateAnswer(qns[qnIndex].qnId, index)}>
                        <div id={"option"+index}>
                            <b>{String.fromCharCode(65+index)+' . '}</b>{item}
                        </div>
                    </ListItemButton>
                    )}


                </List>
            </CardContent>
        </Card>
        :null
        )
}