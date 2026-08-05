import api from "./api";
import { downloadBlob } from "../utils/download";

export const downloadResumePdf = async (resumeId: string) => {
  const response = await api.get(`/resume/download/${resumeId}`, {
    responseType: "blob",
  });
  downloadBlob(response.data, "resume.pdf");
};

export const exportContentAsPdf = async (content: string, title?: string) => {
  const response = await api.post(
    "/resume/export-pdf",
    { content, title },
    { responseType: "blob" }
  );
  downloadBlob(response.data, "resume-export.pdf");
};
