import { useState, useRef, useEffect } from "react";
import ReactMarkdown from "react-markdown";
import {
  Box,
  Button,
  TextField,
  Typography,
  Paper,
  CircularProgress,
  Alert,
} from "@mui/material";
import { askQuestion } from "../services/chatApi";

interface Message {
  role: "user" | "ai";
  text: string;
}

const ChatPage = () => {
  const [question, setQuestion] = useState("");
  const [messages, setMessages] = useState<Message[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const chatEndRef = useRef<HTMLDivElement | null>(null);

  const suggestedQuestions = [
    "What are my technical skills?",
    "Summarize my experience.",
    "Which company am I currently working for?",
    "How many years of experience do I have?",
    "What are my achievements?",
  ];

  useEffect(() => {
    chatEndRef.current?.scrollIntoView({ behavior: "smooth" });
  }, [messages]);

  const ask = async () => {
    if (!question.trim()) {
      return;
    }

    const resumeId = localStorage.getItem("resumeId");
    if (!resumeId) {
      setError("Please upload a resume first before chatting.");
      return;
    }

    const userMessage: Message = {
      role: "user",
      text: question.trim(),
    };

    setMessages((prev) => [...prev, userMessage]);
    setQuestion("");
    setError("");
    setLoading(true);

    try {
      const response = await askQuestion({
        resumeId,
        question: userMessage.text,
      });

      setMessages((prev) => [
        ...prev,
        {
          role: "ai",
          text: response.data.answer,
        },
      ]);
    } catch (err) {
      console.error(err);
      setMessages((prev) => [
        ...prev,
        {
          role: "ai",
          text: "Something went wrong while getting an AI response.",
        },
      ]);
    } finally {
      setLoading(false);
    }
  };

  const askSuggestedQuestion = (text: string) => {
    setQuestion(text);
  };

  const clearChat = () => {
    setMessages([]);
    setError("");
  };

  return (
    <Box
      sx={{
        maxWidth: 900,
        margin: "auto",
        padding: 3,
      }}
    >
      <Typography variant="h4" gutterBottom>
        Chat With Resume
      </Typography>

      <Typography variant="body1" color="text.secondary" sx={{ mb: 2 }}>
        Ask questions about your uploaded resume. Answers are grounded in the
        resume context retrieved from ChromaDB.
      </Typography>

      {error && (
        <Alert severity="warning" sx={{ mb: 2 }}>
          {error}
        </Alert>
      )}

      <Box
        sx={{
          display: "flex",
          gap: 1,
          flexWrap: "wrap",
          mb: 2,
        }}
      >
        {suggestedQuestions.map((item) => (
          <Button
            key={item}
            variant="outlined"
            size="small"
            onClick={() => askSuggestedQuestion(item)}
          >
            {item}
          </Button>
        ))}
      </Box>

      <Button variant="outlined" onClick={clearChat} sx={{ mb: 2 }}>
        Clear Chat
      </Button>

      <Paper
        elevation={3}
        sx={{
          height: 500,
          overflowY: "auto",
          padding: 2,
          mb: 2,
        }}
      >
        {messages.length === 0 && !loading && (
          <Typography color="text.secondary">
            Ask a question about your resume to get started.
          </Typography>
        )}

        {messages.map((msg, index) => (
          <Box
            key={index}
            sx={{
              display: "flex",
              justifyContent: msg.role === "user" ? "flex-end" : "flex-start",
              mb: 2,
            }}
          >
            <Paper
              elevation={2}
              sx={{
                padding: 2,
                maxWidth: "75%",
                "& p": { margin: 0 },
                "& ul": { paddingLeft: 3 },
              }}
            >
              <Typography variant="subtitle2" sx={{ mb: 1 }}>
                {msg.role === "user" ? "You" : "AI"}
              </Typography>

              {msg.role === "ai" ? (
                <ReactMarkdown>{msg.text}</ReactMarkdown>
              ) : (
                <Typography>{msg.text}</Typography>
              )}
            </Paper>
          </Box>
        ))}

        {loading && (
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              gap: 1,
            }}
          >
            <CircularProgress size={22} />
            <Typography>AI is thinking...</Typography>
          </Box>
        )}

        <div ref={chatEndRef} />
      </Paper>

      <Box sx={{ display: "flex", gap: 2 }}>
        <TextField
          fullWidth
          label="Question"
          value={question}
          onChange={(e) => setQuestion(e.target.value)}
          placeholder="What are my skills?"
          onKeyDown={(e) => {
            if (e.key === "Enter" && !e.shiftKey) {
              e.preventDefault();
              ask();
            }
          }}
        />

        <Button variant="contained" onClick={ask} disabled={loading}>
          Ask
        </Button>
      </Box>
    </Box>
  );
};

export default ChatPage;
