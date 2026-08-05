import { useState } from "react";
import {
  Box,
  Typography,
  TextField,
  Button,
  Paper,
  CircularProgress
} from "@mui/material";

import api from "../services/api";


const ImproveResumePage = () => {


  const [section, setSection] = useState("");

  const [response, setResponse] = useState("");

  const [loading, setLoading] = useState(false);



  const improveResume = async () => {


    if (!section.trim()) {
      return;
    }


    try {

      setLoading(true);


      const resumeId =
        localStorage.getItem("resumeId");


      const result = await api.post(
        "/resume/improve",
        {
          resumeId,
          section
        }
      );


      setResponse(
        result.data.answer
      );


    } catch(error) {

      console.error(error);

      setResponse(
        "Something went wrong"
      );


    } finally {

      setLoading(false);

    }

  };




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
        Improve Resume With AI
      </Typography>



      <Typography
        sx={{
          mb:3
        }}
      >
        Enter the resume section you want to improve.
      </Typography>




      <TextField

        fullWidth

        multiline

        rows={5}

        value={section}

        onChange={
          (e)=>
            setSection(e.target.value)
        }

        placeholder=
        "Example: Improve my OpenText experience section"

      />



      <Button

        variant="contained"

        sx={{
          mt:2
        }}

        onClick={improveResume}

        disabled={loading}

      >

        {
          loading
          ?
          "Improving..."
          :
          "Improve Resume"
        }

      </Button>




      {
        loading &&
        <CircularProgress
          sx={{
            mt:3
          }}
        />
      }




      {
        response &&

        <Paper
          elevation={3}
          sx={{
            mt:4,
            padding:3
          }}
        >

          <Typography
            variant="h6"
          >
            AI Suggestion
          </Typography>


          <Typography
            sx={{
              mt:2
            }}
          >
            {response}
          </Typography>


        </Paper>

      }



    </Box>

  );

};


export default ImproveResumePage;