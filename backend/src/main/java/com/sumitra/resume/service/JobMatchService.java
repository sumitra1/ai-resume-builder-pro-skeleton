package com.sumitra.resume.service;

import com.sumitra.resume.dto.JobMatchRequest;
import com.sumitra.resume.dto.JobMatchResponse;

public interface JobMatchService {

    JobMatchResponse match(JobMatchRequest request, String userEmail);
}
