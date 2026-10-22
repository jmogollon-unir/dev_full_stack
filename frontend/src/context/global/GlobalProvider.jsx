import React, { useState } from "react";
import { GlobalContext } from "./GlobalContext";

export function GlobalProvider({ children }) {
  const [searchTerm, setSearchTerm] = useState("");

  return (
    <GlobalContext.Provider
      value={{ searchTerm, setSearchTerm }}
    >
      {children}
    </GlobalContext.Provider>
  );
}

