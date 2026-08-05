import { useState } from "react";
import { useNavigate, Link as RouterLink } from "react-router-dom";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  Alert,
  Link,
} from "@mui/material";
import { login } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import axios from "axios";
import { APP_NAME } from "../constants/app";

const LoginPage = () => {
  const navigate = useNavigate();
  const { login: saveToken } = useAuth();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");

  const handleLogin = async () => {
    setError("");
    try {
      const res = await login(email, password);
      saveToken(res.token);
      navigate("/dashboard");
    } catch (err: unknown) {
      if (axios.isAxiosError(err)) {
        setError(
          (err.response?.data as { message?: string })?.message ||
            "Login failed. Check your credentials."
        );
      } else {
        setError("Login failed. Please try again.");
      }
    }
  };

  return (
    <Box
      sx={{
        maxWidth: 420,
        mx: "auto",
        mt: 6,
      }}
    >
      <Paper sx={{ p: 4 }}>
        <Typography
          variant="h5"
          gutterBottom
          sx={{ fontWeight: 700, color: "primary.main" }}
        >
          {APP_NAME}
        </Typography>
        <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
          Welcome back
        </Typography>
        <Typography color="text.secondary" sx={{ mb: 3 }}>
          Sign in to continue
        </Typography>

        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}

        <TextField
          fullWidth
          label="Email"
          type="email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          sx={{ mb: 2 }}
        />
        <TextField
          fullWidth
          label="Password"
          type="password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          sx={{ mb: 3 }}
        />

        <Button fullWidth variant="contained" size="large" onClick={handleLogin}>
          Login
        </Button>

        <Typography variant="body2" sx={{ mt: 2, textAlign: "center" }}>
          No account?{" "}
          <Link component={RouterLink} to="/register">
            Register
          </Link>
        </Typography>
      </Paper>
    </Box>
  );
};

export default LoginPage;
