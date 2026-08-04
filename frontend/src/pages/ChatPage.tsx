import { useState } from "react";
import api from "../services/api";

const ChatPage = () => {
  const [question, setQuestion] = useState<string>("");
  const [answer, setAnswer] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);

  const askAI = async () => {
    if (!question.trim()) {
      return;
    }

    try {
      setLoading(true);

      const resumeId = localStorage.getItem("resumeId");

      const response = await api.post("/chat", {
        resumeId: resumeId,
        question: question,
      });

      console.log(response.data);

      setAnswer(response.data.answer);
    } catch (error) {
      console.error(error);

      setAnswer("Something went wrong");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div>
      <h1>Ask AI About Your Resume</h1>

      <textarea
        rows={5}
        cols={50}
        value={question}
        onChange={(e) => setQuestion(e.target.value)}
        placeholder="Ask something about your resume..."
      />

      <br />

      <button onClick={askAI} disabled={loading}>
        {loading ? "Thinking..." : "Ask AI"}
      </button>

      <h2>AI Response</h2>

      <p>{answer}</p>
    </div>
  );
};

export default ChatPage;
