import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Card,
  CardActions,
  CardContent,
  Grid,
  Typography,
  Alert,
} from "@mui/material";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import ChatIcon from "@mui/icons-material/Chat";
import AnalyticsIcon from "@mui/icons-material/Analytics";
import WorkIcon from "@mui/icons-material/Work";
import AutoFixHighIcon from "@mui/icons-material/AutoFixHigh";
import HistoryIcon from "@mui/icons-material/History";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import { downloadResumePdf } from "../services/resumeApi";
import { APP_TAGLINE } from "../constants/app";

const features = [
  {
    title: "Upload Resume",
    description: "Upload your resume PDF and generate AI embeddings.",
    path: "/upload",
    icon: <UploadFileIcon color="primary" />,
    action: "Upload",
  },
  {
    title: "Chat With Resume",
    description: "Ask AI questions grounded in your resume context.",
    path: "/chat",
    icon: <ChatIcon color="primary" />,
    action: "Open Chat",
  },
  {
    title: "ATS Analysis",
    description: "Get score, strengths, weaknesses, and suggestions.",
    path: "/analysis",
    icon: <AnalyticsIcon color="primary" />,
    action: "Analyze",
  },
  {
    title: "Job Match",
    description: "Compare your resume against a job description.",
    path: "/job-match",
    icon: <WorkIcon color="primary" />,
    action: "Match Job",
  },
  {
    title: "Improve Resume",
    description: "Rewrite sections with ATS-friendly AI suggestions.",
    path: "/improve",
    icon: <AutoFixHighIcon color="primary" />,
    action: "Improve",
  },
  {
    title: "Chat History",
    description: "Review past questions and AI answers.",
    path: "/history",
    icon: <HistoryIcon color="primary" />,
    action: "View History",
  },
];

const DashboardPage = () => {
  const navigate = useNavigate();
  const resumeId = localStorage.getItem("resumeId");

  const handleDownload = async () => {
    if (!resumeId) return;
    try {
      await downloadResumePdf(resumeId);
    } catch (error) {
      console.error(error);
      alert("Failed to download resume PDF.");
    }
  };

  return (
    <Box>
      <Box sx={{ mb: 4 }}>
        <Typography color="text.secondary">
          {APP_TAGLINE}
        </Typography>
      </Box>

      {!resumeId && (
        <Alert severity="info" sx={{ mb: 3 }}>
          Upload a resume first to unlock chat, analysis, job match, and export.
        </Alert>
      )}

      <Grid container spacing={3}>
        {features.map((feature) => (
          <Grid key={feature.title} size={{ xs: 12, sm: 6, md: 4 }}>
            <Card sx={{ height: "100%" }}>
              <CardContent>
                <Box sx={{ mb: 1 }}>{feature.icon}</Box>
                <Typography variant="h6" gutterBottom>
                  {feature.title}
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {feature.description}
                </Typography>
              </CardContent>
              <CardActions>
                <Button onClick={() => navigate(feature.path)}>
                  {feature.action}
                </Button>
              </CardActions>
            </Card>
          </Grid>
        ))}

        <Grid size={{ xs: 12, sm: 6, md: 4 }}>
          <Card sx={{ height: "100%" }}>
            <CardContent>
              <Box sx={{ mb: 1 }}>
                <PictureAsPdfIcon color="primary" />
              </Box>
              <Typography variant="h6" gutterBottom>
                Download PDF
              </Typography>
              <Typography variant="body2" color="text.secondary">
                Download your originally uploaded resume as a PDF file.
              </Typography>
            </CardContent>
            <CardActions>
              <Button onClick={handleDownload} disabled={!resumeId}>
                Download
              </Button>
            </CardActions>
          </Card>
        </Grid>
      </Grid>
    </Box>
  );
};

export default DashboardPage;
