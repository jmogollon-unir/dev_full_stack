import "./BookDetail.css";
import React from "react";
import { useContext } from "react";
import { useLocation, useNavigate, useParams, Link } from "react-router-dom";
import { AuthContext } from "../../context/auth/AuthContext.jsx";
import useCart from "../../hooks/useCart";
import { useBook } from "../../hooks/useBook";

export default function BookDetail() {
    const { bookId } = useParams();
    const location = useLocation();
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);
    const { addItem } = useCart();
    const { book, loading, error } = useBook(bookId);

    const handleAddToCart = () => {
        if (!book || book.stock <= 0) return;

        addItem(book, 1);
    };

    const handleLoginRedirect = () => {
        const from = `${location.pathname}${location.search}${location.hash}`;
        navigate("/login", { state: { from }, replace: true });
    };

    if (loading) {
        return (
            <div className="book-detail">
                <div className="loading-container">
                    <p className="loading-message">Cargando libro...</p>
                </div>
            </div>
        );
    }

    if (error && !book) {
        return (
            <div className="book-detail">
                <div className="error-container">
                    <h2>Libro no encontrado</h2>
                    <p>No se pudo encontrar el libro solicitado.</p>
                    <Link to="/" className="back-link">← Volver a página principal</Link>
                </div>
            </div>
        );
    }

    const renderStars = (rating) => {
        return (
            <div className="rating-stars">
                {[...Array(5)].map((_, i) => (
                    <span key={i} className={`star ${i < Math.floor(rating) ? "filled" : ""}`}>★</span>
                ))}
            </div>
        );
    };

    return (
        <div className="book-detail">
            {error && <p className="fetch-error">{error}</p>}
            {book && (
                <>
                    {/* Book detail section*/}
                    <div className="book-detail-content">
                        <div className="book-image-section">
                            <div className="main-image">
                                <img src={book.cover} alt={book.title} />
                            </div>
                            <div className="price-badge">€{book.price}</div>
                        </div>
                        <div className="book-info">
                            <div className="book-header">
                                <span className="book-genre">{book.genre}</span>
                            </div>
                            <h1 className="book-title-detail">{book.title}</h1>
                            <p className="book-author">por {book.author}</p>
                            <div className="book-stock-status">
                                {book.stock > 0 ? (
                                    <>
                                        <i className="bi bi-check-circle stock-icon"></i>
                                        <span className="stock-text">En stock ({book.stock} disponibles)</span>
                                    </>
                                ) : (<span className="out-of-stock-text">Agotado</span>)}
                            </div>
                            <div className="book-rating">
                                {renderStars(book.review_score)}
                                <span className="rating-text">({book.review_score} / 5 - {book.reviews.length} reseñas)</span>
                            </div>
                            <div className="book-description-section">
                                <h3>Descripción</h3>
                                <p className="book-description">{book.description}</p>
                            </div>
                            <div className="product-info">
                                <h3>Información del producto</h3>
                                <div className="info-grid">
                                    <div className="info-item">
                                        <span className="info-label">Formato:</span>
                                        <span className="info-value">{book.formato}</span>
                                    </div>
                                    <div className="info-item">
                                        <span className="info-label">Disponibilidad:</span>
                                        <span className={`info-value ${book.stock > 0 ? "in-stock" : "out-of-stock"}`}>
                                            {book.stock > 0 ? "En stock" : "Agotado"}
                                        </span>
                                    </div>
                                    <div className="info-item">
                                        <span className="info-label">Idioma:</span>
                                        <span className="info-value">{book.lenguaje}</span>
                                    </div>
                                </div>
                            </div>
                            {/* Action (add to cart or no available) section*/}
                            {book.stock > 0 ? (
                                user ? (
                                    <button
                                        type="button"
                                        className="add-to-cart-btn"
                                        onClick={handleAddToCart}
                                    >
                                        <i className="bi bi-cart"></i>
                                        Agregar al carrito
                                    </button>
                                ) : (
                                    <button
                                        type="button"
                                        className="btn-sesion add-to-cart-btn"
                                        onClick={handleLoginRedirect}
                                    >
                                        <i className="bi bi-cart"></i>
                                        Inicia Sesión para Comprar
                                    </button>
                                )
                            ) : (
                                <button type="button" className="add-to-cart-btn" disabled>
                                    <i className="bi bi-cart"></i>
                                    No disponible
                                </button>
                            )}
                        </div>
                    </div>
                    {/* Reviews section*/}
                    <div className="reviews-section">
                        <h2 className="reviews-title">
                            <i className="bi bi-chat-left reviews"></i>
                            Reseñas de Clientes
                        </h2>
                        {book.reviews.length > 0 ? (
                            <div className="reviews-list">
                                {book.reviews.map((review, index) => (
                                    <div key={index} className="review-item">
                                        <div className="review-header">
                                            <div className="reviewer-info">
                                                <h4 className="reviewer-name">{review.name}</h4>
                                            </div>
                                        </div>
                                        <p className="review-date">{review.date}</p>
                                        <p className="review-comment">{review.comment}</p>
                                    </div>
                                ))}
                            </div>
                        ) : (
                            <div className="no-reviews">
                                <i className="bi bi-chat-left"></i>
                                <p>Aún no hay reseñas para este libro.</p>
                            </div>
                        )}
                    </div>
                </>
            )}
        </div>
    );
}