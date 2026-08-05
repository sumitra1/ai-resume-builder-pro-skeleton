import { useEffect, useState } from "react";
import {
  Box,
  Button,
  Typography,
  Paper,
  CircularProgress,
  Alert,
  IconButton,
  Divider,
} from "@mui/material";
import DeleteIcon from "@mui/icons-material/Delete";
import {
  fetchChatHistory,
  fetchChatHistoryByResume,
  deleteChatHistoryItem,
  type ChatHistoryItem,
} from "../services/historyApi";

const HistoryPage = () => {
  const [items, setItems] = useState<ChatHistoryItem[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadHistory = async () => {
    setLoading(true);
    setError("");
    try {
      const resumeId = localStorage.getItem("resumeId");
      const response = resumeId
        ? await fetchChatHistoryByResume(resumeId)
        : await fetchChatHistory();
      setItems(response.data);
    } catch (err) {
      console.error(err);
      setError("Failed to load chat history.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadHistory();
  }, []);

  const handleDelete = async (id: number) => {
    try {
      await deleteChatHistoryItem(id);
      setItems((prev) => prev.filter((item) => item.id !== id));
    } catch (err) {
      console.error(err);
      alert("Failed to delete history item.");
    }
  };

  if (loading) {
    return (
      <Box sx={{ display: "flex", justifyContent: "center", mt: 8 }}>
        <CircularProgress />
      </Box>
    );
  }

  return (
    <Box sx={{ maxWidth: 900, mx: "auto" }}>
      <Typography variant="h4" gutterBottom>
        Chat History
      </Typography>
      <Typography color="text.secondary" sx={{ mb: 3 }}>
        Past questions and answers from Resume Chat.
      </Typography>

      {error && (
        <Alert severity="error" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      {items.length === 0 ? (
        <Paper sx={{ p: 3 }}>
          <Typography color="text.secondary">
            No chat history yet. Start chatting with your resume to see entries
            here.
          </Typography>
        </Paper>
      ) : (
        items.map((item) => (
          <Paper key={item.id} sx={{ p: 3, mb: 2 }}>
            <Box
              sx={{
                display: "flex",
                justifyContent: "space-between",
                alignItems: "flex-start",
                gap: 2,
              }}
            >
              <Box>
                <Typography variant="caption" color="text.secondary">
                  {new Date(item.createdAt).toLocaleString()} · Resume{" "}
                  {item.resumeId}
                </Typography>
                <Typography variant="subtitle2" sx={{ mt: 1 }}>
                  Question
                </Typography>
                <Typography sx={{ mb: 1 }}>{item.question}</Typography>
                <Divider sx={{ my: 1 }} />
                <Typography variant="subtitle2">Answer</Typography>
                <Typography color="text.secondary">{item.answer}</Typography>
              </Box>
              <IconButton
                color="error"
                aria-label="delete"
                onClick={() => handleDelete(item.id)}
              >
                <DeleteIcon />
              </IconButton>
            </Box>
          </Paper>
        ))
      )}

      <Button variant="outlined" onClick={loadHistory} sx={{ mt: 2 }}>
        Refresh
      </Button>
    </Box>
  );
};

export default HistoryPage;
