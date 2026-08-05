import api from "./api";

export interface JobMatchRequest {
  resumeId: string;
  jobDescription: string;
}

export interface JobMatchResponse {
  matchScore: number;
  matchedSkills: string[];
  missingSkills: string[];
  notes: string;
}

export const analyzeJobMatch = (data: JobMatchRequest) =>
  api.post<JobMatchResponse>("/job-match", data);
