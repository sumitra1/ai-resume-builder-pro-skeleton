import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  Box,
  Button,
  Typography,
  Paper,
  Alert,
} from "@mui/material";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import api from "../services/api";

const UploadResumePage = () => {
  const [file, setFile] = useState<File | null>(null);
  const [message, setMessage] = useState("");
  const [error, setError] = useState("");
  const [uploading, setUploading] = useState(false);
  const navigate = useNavigate();

  const handleUpload = async () => {
    if (!file) {
      setError("Please select a PDF file.");
      return;
    }

    const formData = new FormData();
    formData.append("file", file);

    setUploading(true);
    setError("");
    setMessage("");

    try {
      const response = await api.post("/resume/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      localStorage.setItem("resumeId", response.data.resumeId);
      setMessage(response.data.message);
      navigate("/chat");
    } catch (err) {
      console.error(err);
      setError("Upload failed. Please try again.");
    } finally {
      setUploading(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 600, mx: "auto" }}>
      <Typography variant="h4" gutterBottom>
        Upload Resume
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        Upload a PDF resume to enable chat, analysis, job match, and improvements.
      </Typography>

      <Paper sx={{ p: 3 }}>
        {error && (
          <Alert severity="error" sx={{ mb: 2 }}>
            {error}
          </Alert>
        )}
        {message && (
          <Alert severity="success" sx={{ mb: 2 }}>
            {message}
          </Alert>
        )}

        <Box
          sx={{
            border: "2px dashed",
            borderColor: "divider",
            borderRadius: 2,
            p: 4,
            textAlign: "center",
            mb: 2,
          }}
        >
          <UploadFileIcon sx={{ fontSize: 48, color: "primary.main", mb: 1 }} />
          <Typography gutterBottom>
            {file ? file.name : "Select a PDF file"}
          </Typography>
          <Button variant="outlined" component="label">
            Choose File
            <input
              type="file"
              accept=".pdf"
              hidden
              onChange={(e) => {
                if (e.target.files?.[0]) {
                  setFile(e.target.files[0]);
                  setError("");
                }
              }}
            />
          </Button>
        </Box>

        <Button
          fullWidth
          variant="contained"
          size="large"
          onClick={handleUpload}
          disabled={uploading || !file}
        >
          {uploading ? "Uploading..." : "Upload & Continue to Chat"}
        </Button>
      </Paper>
    </Box>
  );
};

export default UploadResumePage;
