import { useState } from "react";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  Chip,
  Stack,
  LinearProgress,
} from "@mui/material";
import { analyzeJobMatch } from "../services/jobMatchApi";

const JobMatchPage = () => {
  const [jobDescription, setJobDescription] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [result, setResult] = useState<{
    matchScore: number;
    matchedSkills: string[];
    missingSkills: string[];
    notes: string;
  } | null>(null);

  const analyze = async () => {
    const resumeId = localStorage.getItem("resumeId");
    if (!resumeId) {
      setError("Please upload a resume first.");
      return;
    }
    if (!jobDescription.trim()) {
      setError("Please paste a job description.");
      return;
    }

    setLoading(true);
    setError("");
    setResult(null);

    try {
      const response = await analyzeJobMatch({
        resumeId,
        jobDescription: jobDescription.trim(),
      });
      setResult(response.data);
    } catch (err) {
      console.error(err);
      setError("Job match analysis failed. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <Box sx={{ maxWidth: 900, mx: "auto" }}>
      <Typography variant="h4" gutterBottom>
        Job Match Analyzer
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        Compare your resume against a job description to see fit score,
        matched skills, and gaps.
      </Typography>

      {error && (
        <Alert severity="warning" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <TextField
        fullWidth
        multiline
        rows={8}
        label="Job Description"
        placeholder="Paste the full job description here..."
        value={jobDescription}
        onChange={(e) => setJobDescription(e.target.value)}
        sx={{ mb: 2 }}
      />

      <Button variant="contained" onClick={analyze} disabled={loading}>
        {loading ? "Analyzing..." : "Analyze Match"}
      </Button>

      {loading && <CircularProgress sx={{ mt: 3 }} />}

      {result && (
        <Paper elevation={2} sx={{ mt: 4, p: 3 }}>
          <Typography variant="h6" gutterBottom>
            Match Score: {result.matchScore}%
          </Typography>
          <LinearProgress
            variant="determinate"
            value={Math.min(result.matchScore, 100)}
            sx={{ mb: 3, height: 10, borderRadius: 5 }}
          />

          <Typography variant="subtitle1" gutterBottom>
            Matched Skills
          </Typography>
          <Stack
            direction="row"
            spacing={1}
            useFlexGap
            sx={{ mb: 2, flexWrap: "wrap" }}
          >
            {result.matchedSkills?.length
              ? result.matchedSkills.map((skill) => (
                  <Chip key={skill} label={skill} color="success" size="small" />
                ))
              : <Typography variant="body2" color="text.secondary">None identified</Typography>}
          </Stack>

          <Typography variant="subtitle1" gutterBottom>
            Missing Skills
          </Typography>
          <Stack
            direction="row"
            spacing={1}
            useFlexGap
            sx={{ mb: 2, flexWrap: "wrap" }}
          >
            {result.missingSkills?.length
              ? result.missingSkills.map((skill) => (
                  <Chip key={skill} label={skill} color="warning" size="small" />
                ))
              : <Typography variant="body2" color="text.secondary">None identified</Typography>}
          </Stack>

          <Typography variant="subtitle1" gutterBottom>
            Summary
          </Typography>
          <Typography>{result.notes}</Typography>
        </Paper>
      )}
    </Box>
  );
};

export default JobMatchPage;
