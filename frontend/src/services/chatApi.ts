import api from "./api";

export const askQuestion = (data: {
  resumeId: string | null;
  question: string;
}) => api.post("/chat", data);
