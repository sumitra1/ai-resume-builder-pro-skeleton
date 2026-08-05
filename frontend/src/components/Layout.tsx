import { useLocation, useNavigate } from "react-router-dom";
import {
  AppBar,
  Box,
  Button,
  Container,
  Toolbar,
  Typography,
  Stack,
} from "@mui/material";
import { useAuth } from "../context/AuthContext";
import { APP_NAME } from "../constants/app";

const navItems = [
  { label: "Dashboard", path: "/dashboard" },
  { label: "Upload", path: "/upload" },
  { label: "Chat", path: "/chat" },
  { label: "Analysis", path: "/analysis" },
  { label: "Job Match", path: "/job-match" },
  { label: "Improve", path: "/improve" },
  { label: "History", path: "/history" },
];

interface LayoutProps {
  children: React.ReactNode;
}

const Layout = ({ children }: LayoutProps) => {
  const { token, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const isAuthPage =
    location.pathname === "/login" || location.pathname === "/register";

  const handleLogout = () => {
    logout();
    localStorage.removeItem("resumeId");
    navigate("/login");
  };

  return (
    <Box sx={{ minHeight: "100vh", bgcolor: "background.default" }}>
      {token && !isAuthPage && (
        <AppBar
          position="sticky"
          elevation={0}
          sx={{
            bgcolor: "white",
            color: "text.primary",
            borderBottom: "1px solid",
            borderColor: "divider",
          }}
        >
          <Toolbar sx={{ gap: 2, flexWrap: "wrap" }}>
            <Typography
              variant="h6"
              onClick={() => navigate("/dashboard")}
              sx={{
                fontWeight: 800,
                color: "primary.main",
                mr: 2,
                cursor: "pointer",
              }}
            >
              {APP_NAME}
            </Typography>

            <Stack direction="row" spacing={1} sx={{ flexGrow: 1, flexWrap: "wrap" }}>
              {navItems.map((item) => (
                <Button
                  key={item.path}
                  size="small"
                  variant={
                    location.pathname === item.path ? "contained" : "text"
                  }
                  onClick={() => navigate(item.path)}
                >
                  {item.label}
                </Button>
              ))}
            </Stack>

            <Button color="inherit" onClick={handleLogout}>
              Logout
            </Button>
          </Toolbar>
        </AppBar>
      )}

      <Container maxWidth="lg" sx={{ py: 4 }}>
        {children}
      </Container>
    </Box>
  );
};

export default Layout;
