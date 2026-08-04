import { useNavigate } from "react-router-dom";

const DashboardPage = () => {

  const navigate = useNavigate();

  return (
    <div className="dashboard-container">

      <div className="dashboard-header">
        <h1>Resume AI Copilot</h1>
        <p>
          Upload your resume and get AI-powered insights.
        </p>
      </div>


      <div className="dashboard-actions">


        <div className="card">

          <h2>Upload Resume</h2>

          <p>
            Upload your existing resume PDF and let AI analyze it.
          </p>

          <button
            onClick={() => navigate("/upload")}
          >
            Upload Resume
          </button>

        </div>



        <div className="card">

          <h2>Chat With Resume</h2>

          <p>
            Ask AI questions about your uploaded resume.
          </p>

          <button
            onClick={() => navigate("/chat")}
          >
            Open Chat
          </button>

        </div>



        <div className="card">

          <h2>Job Match</h2>

          <p>
            Compare your resume with a job description.
          </p>

          <button
            onClick={() => navigate("/job-match")}
          >
            Match Job
          </button>

        </div>



        <div className="card">

          <h2>History</h2>

          <p>
            View previous AI analysis and chats.
          </p>

          <button
            onClick={() => navigate("/history")}
          >
            View History
          </button>

        </div>


      </div>

    </div>
  );
};


export default DashboardPage;