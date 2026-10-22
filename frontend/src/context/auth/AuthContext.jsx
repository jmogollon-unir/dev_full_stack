import React, { createContext, useState, useEffect } from "react";

export const AuthContext = createContext();

const STORAGE_USER = "rp_user";

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(() => {
    try {
      const raw = localStorage.getItem(STORAGE_USER);
      return raw ? JSON.parse(raw) : null;
    } catch {
      return null;
    }
  });

  useEffect(() => {
    if (user) {
      localStorage.setItem(STORAGE_USER, JSON.stringify(user));
    } else {
      localStorage.removeItem(STORAGE_USER);
    }
  }, [user]);

  const clearSession = () => {
    setUser(null);
    localStorage.removeItem(STORAGE_USER);
  };

  return (
    <AuthContext.Provider value={{ user, setUser, clearSession }}>
      {children}
    </AuthContext.Provider>
  );
};
