import { useEffect, useState } from "react";

import {
  Box,
  Typography,
  Paper,
  CircularProgress,
  List,
  ListItem,
  ListItemText
} from "@mui/material";

import api from "../services/api";


interface Analysis {

  score:number;

  strengths:string[];

  weaknesses:string[];

  suggestions:string[];

}



const AnalysisPage = () => {


  const [analysis,setAnalysis] =
    useState<Analysis | null>(null);


  const [loading,setLoading] =
    useState(true);


useEffect(() => {

  const analyzeResume = async () => {

    try {

      const resumeId =
        localStorage.getItem("resumeId");


      if (!resumeId) {
        console.error("Resume ID not found");
        return;
      }


      const response =
        await api.post(
          `/resume/analyze/${resumeId}`
        );


      setAnalysis(response.data);


    } catch(error) {

      console.error(
        "Resume analysis failed",
        error
      );


    } finally {

      setLoading(false);

    }

  };


  analyzeResume();


}, []);





  if(loading){

    return (

      <Box
        sx={{
          display:"flex",
          justifyContent:"center",
          mt:10
        }}
      >

        <CircularProgress/>

      </Box>

    );

  }



  return (

    <Box
      sx={{
        maxWidth:900,
        margin:"auto",
        padding:3
      }}
    >


      <Typography
        variant="h4"
        gutterBottom
      >
        Resume ATS Analysis
      </Typography>



      {
        analysis &&

        <>


        <Paper
          elevation={3}
          sx={{
            padding:3,
            mb:3,
            textAlign:"center"
          }}
        >

          <Typography variant="h6">
            ATS Score
          </Typography>


          <Typography
            variant="h2"
          >
            {analysis.score}/100
          </Typography>


        </Paper>





        <Paper
          sx={{
            padding:3,
            mb:3
          }}
        >

          <Typography variant="h5">
            Strengths
          </Typography>


          <List>

          {
            (analysis.strengths || []).map(
              (item,index)=>(

                <ListItem key={index}>

                  <ListItemText
                    primary={item}
                  />

                </ListItem>

              )
            )
          }

          </List>

        </Paper>





        <Paper
          sx={{
            padding:3,
            mb:3
          }}
        >

          <Typography variant="h5">
            Weaknesses
          </Typography>


          <List>

          {(analysis.weaknesses || []).map(
              (item,index)=>(

                <ListItem key={index}>

                  <ListItemText
                    primary={item}
                  />

                </ListItem>

              )
            )
          }

          </List>

        </Paper>





        <Paper
          sx={{
            padding:3
          }}
        >

          <Typography variant="h5">
            Suggestions
          </Typography>


          <List>

          {(analysis.suggestions || []).map(
              (item,index)=>(

                <ListItem key={index}>

                  <ListItemText
                    primary={item}
                  />

                </ListItem>

              )
            )
          }

          </List>


        </Paper>


        </>

      }


    </Box>

  );


};


export default AnalysisPage;