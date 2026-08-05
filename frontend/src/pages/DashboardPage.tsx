import { useNavigate } from "react-router-dom";

const DashboardPage = () => {

  const navigate = useNavigate();

  return (
    <div className="dashboard-container">


      <div className="dashboard-header">

        <h1>
          Resume AI Copilot
        </h1>

        <p>
          Upload your resume and get AI-powered insights.
        </p>

      </div>




      <div className="dashboard-actions">



        <div className="card">

          <h2>
            Upload Resume
          </h2>

          <p>
            Upload your resume PDF and generate AI insights.
          </p>


          <button
            onClick={() => navigate("/upload")}
          >
            Upload Resume
          </button>

        </div>





        <div className="card">

          <h2>
            Chat With Resume
          </h2>


          <p>
            Ask AI questions based on your resume.
          </p>


          <button
            onClick={() => navigate("/chat")}
          >
            Open Chat
          </button>

        </div>





        <div className="card">

          <h2>
            ATS Analysis
          </h2>


          <p>
            Get resume score, strengths, weaknesses and suggestions.
          </p>


          <button
            onClick={() => navigate("/analysis")}
          >
            Analyze Resume
          </button>


        </div>





        <div className="card">

          <h2>
            Improve Resume
          </h2>


          <p>
            Improve your experience sections using AI.
          </p>


          <button
            onClick={() => navigate("/improve")}
          >
            Improve Resume
          </button>


        </div>



      </div>


    </div>
  );
};


export default DashboardPage;