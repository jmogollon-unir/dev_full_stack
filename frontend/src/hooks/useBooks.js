import { useState, useEffect } from "react";
import { getBooks, getFallbackBooks } from "../services/api";

export function useBooks() {
  const [books, setBooks] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let ignore = false;

    const fetchBooks = async () => {
      setLoading(true);

      try {
        const apiBooks = await getBooks();
        if (ignore) return;
        setBooks(apiBooks);
        setError("");
      } catch {
        if (ignore) return;
        setBooks(getFallbackBooks());
        setError("No se pudo cargar el catalogo desde el backend. Mostrando datos locales.");
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    };

    fetchBooks();
    return () => {
      ignore = true;
    };
  }, []);

  return { books, loading, error };
}
