import api from "./api";

export interface ChatHistoryItem {
  id: number;
  resumeId: string;
  question: string;
  answer: string;
  createdAt: string;
}

export const fetchChatHistory = () =>
  api.get<ChatHistoryItem[]>("/chat/history");

export const fetchChatHistoryByResume = (resumeId: string) =>
  api.get<ChatHistoryItem[]>(`/chat/history/${resumeId}`);

export const deleteChatHistoryItem = (id: number) =>
  api.delete(`/chat/history/${id}`);
