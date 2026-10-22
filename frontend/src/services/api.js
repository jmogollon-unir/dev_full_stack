import { API_BASE_URL } from "../apiConfig";
import { mockBooks } from "../utils/mocks/mockBooks";

const PLACEHOLDER_COVER = "https://via.placeholder.com/320x460?text=Relatos+de+Papel";

async function request(path, options = {}) {
  const response = await fetch(`${API_BASE_URL}${path}`, {
    ...options,
    headers: {
      "Content-Type": "application/json",
      ...options.headers
    }
  });

  if (!response.ok) {
    const detail = await response.text();
    throw new Error(detail || `Request failed with status ${response.status}`);
  }

  if (response.status === 204) {
    return null;
  }

  return response.json();
}

export function mapBookFromApi(book) {
  const id = book.bookId ?? book.id;
  const cover = book.coverUrl || book.cover || PLACEHOLDER_COVER;
  const publicationDate = book.publicationDate || book.date || "";
  const rating = book.rating || book.review_score || 4;

  return {
    ...book,
    id,
    bookId: id,
    cover,
    image: cover,
    images: [cover],
    genre: book.genreName || book.genre || "Catalogo",
    genreName: book.genreName || book.genre || "Catalogo",
    formato: book.format || book.formato || "Fisico",
    type: book.format || book.type || "Fisico",
    lenguaje: book.language || book.lenguaje || "Espanol",
    date: publicationDate,
    publicationDate,
    price: Number(book.price) || 0,
    stock: Number(book.stock) || 0,
    popularity: Number(book.popularity) || 0,
    isAvailable: book.isAvailable ?? Number(book.stock) > 0,
    review_score: rating,
    reviews: book.reviews || []
  };
}

export async function getBooks() {
  const books = await request("/api/books");
  return books.map(mapBookFromApi);
}

export async function getBookById(bookId) {
  const book = await request(`/api/books/${bookId}`);
  return mapBookFromApi(book);
}

export async function createOrder({ userId, items }) {
  return request("/api/orders", {
    method: "POST",
    body: JSON.stringify({
      userId,
      items: items.map((item) => ({
        bookId: item.bookId ?? item.id,
        quantity: item.quantity
      }))
    })
  });
}

export async function getOrdersByUser(userId) {
  return request(`/api/orders/user/${userId}`);
}

export function getFallbackBooks() {
  return mockBooks.map(mapBookFromApi);
}
