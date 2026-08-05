import { Box, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router-dom";
import { APP_NAME } from "../constants/app";

const NotFoundPage = () => {
  const navigate = useNavigate();

  return (
    <Box sx={{ textAlign: "center", mt: 8 }}>
      <Typography variant="h4" gutterBottom>
        Page not found
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        This page does not exist in {APP_NAME}.
      </Typography>
      <Button variant="contained" onClick={() => navigate("/dashboard")}>
        Back to Dashboard
      </Button>
    </Box>
  );
};

export default NotFoundPage;
