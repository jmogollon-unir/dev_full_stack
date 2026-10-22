import { useState, useEffect } from "react";
import { mockBookDetail as BOOK_DATA } from "../utils/mocks/mockBookDetail";
import { getBookById } from "../services/api";

export function useBook(bookID) {
  const [book, setBook] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    let ignore = false;

    const fetchBook = async () => {
      setLoading(true);

      try {
        const apiBook = await getBookById(bookID);
        if (ignore) return;
        setBook(apiBook);
        setError("");
      } catch {
        const bookData = BOOK_DATA[bookID];
        if (ignore) return;

        if (bookData) {
          setBook(bookData);
          setError("No se pudo cargar el libro desde el backend. Mostrando datos locales.");
        } else {
          setBook(null);
          setError("Libro no encontrado");
        }
      } finally {
        if (!ignore) {
          setLoading(false);
        }
      }
    };

    fetchBook();
    return () => {
      ignore = true;
    };
  }, [bookID]);

  return { book, loading, error };
}
