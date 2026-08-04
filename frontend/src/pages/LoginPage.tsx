import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";
import { useAuth } from "../context/AuthContext";
import axios from "axios";


const LoginPage = () => {
  const navigate = useNavigate();
  const { login: saveToken } = useAuth();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

 const handleLogin = async () => {
  try {
    const res = await login(email, password);

    console.log("Login Success:", res);

    saveToken(res.token);

    navigate("/dashboard");
  } catch (err: unknown) {
    if (axios.isAxiosError(err)) {
      console.log("Status:", err.response?.status);
      console.log("Data:", err.response?.data);
      console.log("Full Error:", err);

      alert(JSON.stringify(err.response?.data));
    } else {
      console.error("Unknown error:", err);
    }
  }
};
  return (
    <div style={{ padding: 40 }}>
      <h2>Login</h2>

      <input
        placeholder="Email"
        value={email}
        onChange={(e) => setEmail(e.target.value)}
      />

      <br />
      <br />

      <input
        type="password"
        placeholder="Password"
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />

      <br />
      <br />

      <button onClick={handleLogin}>Login</button>
    </div>
  );
};

export default LoginPage;