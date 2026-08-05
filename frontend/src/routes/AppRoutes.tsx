import { Routes, Route, Navigate } from "react-router-dom";

import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import DashboardPage from "../pages/DashboardPage";
import UploadResumePage from "../pages/UploadResumePage";
import ChatPage from "../pages/ChatPage";
import HistoryPage from "../pages/HistoryPage";
import JobMatchPage from "../pages/JobMatchPage";
import NotFoundPage from "../pages/NotFoundPage";
import PrivateRoute from "./PrivateRoute";
import AnalysisPage from "../pages/AnalysisPage";
import ImproveResumePage from "../pages/ImproveResumePage";

const AppRoutes = () => {
  return (
    <Routes>
      <Route path="/" element={<Navigate to="/login" replace />} />

      {/* Public Routes */}
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />

      {/* Protected Routes */}
      <Route
        path="/dashboard"
        element={
          <PrivateRoute>
            <DashboardPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/upload"
        element={
          <PrivateRoute>
            <UploadResumePage />
          </PrivateRoute>
        }
      />

      <Route
        path="/chat"
        element={
          <PrivateRoute>
            <ChatPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/history"
        element={
          <PrivateRoute>
            <HistoryPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/analysis"
        element={
          <PrivateRoute>
            <AnalysisPage />
          </PrivateRoute>
        }
      />

      <Route
        path="/job-match"
        element={
          <PrivateRoute>
            <JobMatchPage />
          </PrivateRoute>
        }
      />

      
      <Route
        path="/improve"
        element={
          <PrivateRoute>
            <ImproveResumePage />
          </PrivateRoute>
        }
      />

      <Route path="*" element={<NotFoundPage />} />
    </Routes>
  );
};

export default AppRoutes;
