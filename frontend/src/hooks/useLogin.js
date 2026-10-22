import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../context/auth/AuthContext";
import { buildApiUrl } from "../apiConfig";
import CryptoJS from "crypto-js";

export function useLogin() {
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");
  const { setUser, clearSession } = useContext(AuthContext);
  const navigate = useNavigate();

  const login = async (username, password) => {
    setIsLoading(true);
    setError("");

    if (!username.trim() || !password.trim()) {
      setError("El correo y la contraseña son obligatorios");
      setIsLoading(false);
      return { success: false };
    }

    try {
      // La contraseña viaja como MD5 (el back la compara con el hash guardado en BD).
      const hashedPassword = CryptoJS.MD5(password).toString();
      const loginData = { username: username.trim(), password: hashedPassword };

      // POST /api/users/login (misma aplicación) -> devuelve el perfil del usuario, sin tokens.
      const response = await fetch(buildApiUrl("USERS", "/api/users/login"), {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(loginData)
      });

      if (!response.ok) {
        if (response.status === 401) {
          setError("Credenciales incorrectas");
        } else {
          setError(`Error ${response.status}`);
        }
        setIsLoading(false);
        return { success: false };
      }

      const userData = await response.json();
      setUser(userData);

      setIsLoading(false);
      return { success: true };
    } catch (err) {
      console.error("Error en login:", err);
      setError("Error en el servidor. Inténtalo de nuevo.");
      setIsLoading(false);
      return { success: false };
    }
  };

  const logout = () => {
    clearSession();
    navigate("/");
  };

  return {
    login,
    logout,
    isLoading,
    error,
    clearError: () => setError("")
  };
}
