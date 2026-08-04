import { useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";


const UploadResumePage = () => {

  const [file, setFile] = useState<File | null>(null);

  const [message, setMessage] = useState<string>("");

  const navigate = useNavigate();


  const handleUpload = async () => {

    if (!file) {
      setMessage("Please select a PDF file");
      return;
    }


    const formData = new FormData();

    formData.append(
      "file",
      file
    );


    try {

      const response = await api.post(
        "/resume/upload",
        formData,
        {
          headers: {
            "Content-Type": "multipart/form-data",
          },
        }
      );


      console.log(response.data);


      // Save resume id for AI chat
      localStorage.setItem(
        "resumeId",
        response.data.resumeId
      );


      setMessage(
        response.data.message
      );


      // Go to chat page
      navigate("/chat");


    } catch (error) {

      console.error(error);

      setMessage(
        "Upload failed"
      );

    }

  };


  const handleFileChange = (
    e: React.ChangeEvent<HTMLInputElement>
  ) => {

    if (e.target.files && e.target.files.length > 0) {

      setFile(
        e.target.files[0]
      );

    }

  };


  return (

    <div>

      <h1>
        Upload Resume
      </h1>


      <input
        type="file"
        accept=".pdf"
        onChange={handleFileChange}
      />


      <br />


      <button
        onClick={handleUpload}
      >
        Upload
      </button>


      <p>
        {message}
      </p>

    </div>

  );

};


export default UploadResumePage;