import { useState } from "react";
import ReactMarkdown from "react-markdown";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  Stack,
} from "@mui/material";
import PictureAsPdfIcon from "@mui/icons-material/PictureAsPdf";
import api from "../services/api";
import { exportContentAsPdf } from "../services/resumeApi";

const ImproveResumePage = () => {
  const [section, setSection] = useState("");
  const [response, setResponse] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [exporting, setExporting] = useState(false);

  const improveResume = async () => {
    if (!section.trim()) return;

    const resumeId = localStorage.getItem("resumeId");
    if (!resumeId) {
      setError("Please upload a resume first.");
      return;
    }

    setLoading(true);
    setError("");
    setResponse("");

    try {
      const result = await api.post("/resume/improve", {
        resumeId,
        section: section.trim(),
      });
      setResponse(result.data.answer);
    } catch (err) {
      console.error(err);
      setError("Something went wrong while improving the section.");
    } finally {
      setLoading(false);
    }
  };

  const handleExportPdf = async () => {
    if (!response) return;
    setExporting(true);
    try {
      await exportContentAsPdf(response, "Improved Resume Section");
    } catch (err) {
      console.error(err);
      alert("Failed to export PDF.");
    } finally {
      setExporting(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 900, mx: "auto" }}>
      <Typography variant="h4" gutterBottom>
        Improve Resume With AI
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        Enter a resume section to rewrite with ATS-friendly, impact-driven bullet
        points grounded in your resume context.
      </Typography>

      {error && (
        <Alert severity="warning" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <TextField
        fullWidth
        multiline
        rows={5}
        label="Section to improve"
        value={section}
        onChange={(e) => setSection(e.target.value)}
        placeholder="Example: Improve my experience at OpenText"
        sx={{ mb: 2 }}
      />

      <Button variant="contained" onClick={improveResume} disabled={loading}>
        {loading ? "Improving..." : "Improve Resume"}
      </Button>

      {loading && <CircularProgress sx={{ mt: 3 }} />}

      {response && (
        <Paper elevation={2} sx={{ mt: 4, p: 3 }}>
          <Stack
            direction="row"
            sx={{
              justifyContent: "space-between",
              alignItems: "center",
              mb: 2,
            }}
          >
            <Typography variant="h6">AI Suggestion</Typography>
            <Button
              variant="outlined"
              startIcon={<PictureAsPdfIcon />}
              onClick={handleExportPdf}
              disabled={exporting}
            >
              {exporting ? "Exporting..." : "Export as PDF"}
            </Button>
          </Stack>

          <Box
            sx={{
              "& p": { margin: 0, mb: 1 },
              "& ul": { pl: 3 },
            }}
          >
            <ReactMarkdown>{response}</ReactMarkdown>
          </Box>
        </Paper>
      )}
    </Box>
  );
};

export default ImproveResumePage;
