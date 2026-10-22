import "./Landing.css";
import { useNavigate } from "react-router-dom";
import { useContext } from "react";
import { useBooks } from "../../hooks/useBooks";
import home_img from "../../assets/home_img.jpg";
import Logo from "../../components/Logo/Logo";
import { AuthContext } from "../../context/auth/AuthContext.jsx";
import { useLogin } from "../../hooks/useLogin.js";

export default function Landing() {
    const navigate = useNavigate();
    const { books, loading } = useBooks();
    const { user } = useContext(AuthContext);
    const { logout } = useLogin();
    // Seleccionamos los libros destacados por popularidad
    const recommendedBooks = books
        .sort((a, b) => b.popularity - a.popularity)
        .slice(0, 6);

    return (
        <div className="landing-container">
            {/* Hero Section */}
            <div className="hero-section">
                <img src={home_img} alt="Librería Relatos de Papel" className="hero-background" />
                <div className="hero-overlay"></div>

                {/* 2. Nueva estructura de navegación superior */}
                <div className="hero-navbar">
                    <Logo /> {/* Solo el logo y el texto */}

                    <div className="hero-nav-buttons">
                        {/* 6. Lógica condicional de botones */}
                        {user ? (
                            <>
                                <button
                                    className="nav-btn-user"
                                    onClick={() => navigate("/profile")}
                                    title="Ir a perfil"
                                >
                                    <i className="bi bi-person"></i>
                                </button>
                                <button
                                    className="nav-btn-logout"
                                    onClick={logout}
                                    title="Cerrar sesión"
                                >
                                    <i className="bi bi-box-arrow-right"></i>
                                </button>
                            </>
                        ) : (
                            <>
                                <button className="nav-btn-login" onClick={() => navigate("/login")}>
                                    Iniciar Sesión
                                </button>
                                <button className="nav-btn-register" onClick={() => navigate("/register")}>
                                    Registrarse
                                </button>
                            </>
                        )}
                    </div>
                </div>

                <div className="hero-content">
                    <h1 className="hero-title">Tu próxima lectura te espera</h1>
                    <p className="hero-subtitle">
                        Explora una amplia colección de libros y descubre tu próximo favorito.
                        <br />Inicia sesión para empezar a comprar.
                    </p>

                    {/* Botón que lleva a la página HOME */}
                    <button className="cta-button" onClick={() => navigate("/books")}>
                        Explorar Libros
                    </button>
                </div>
            </div>

            {/* Recommendations Section */}
            <section className="recommendations-section">
                <h2 className="section-title">Nuestras Recomendaciones</h2>
                {loading ? (
                    <p className="loading-text">Cargando recomendaciones...</p>
                ) : (
                    <div className="recommendations-grid">
                        {recommendedBooks.map((book) => (
                            <div
                                key={book.id}
                                className="rec-card"
                                onClick={() => navigate(`/books/${book.id}`)}
                            >
                                <div className="rec-card-image">
                                    <img src={book.cover} alt={book.title} />
                                </div>
                                <h3 className="rec-book-title">{book.title}</h3>
                                <p className="rec-book-author">{book.author}</p>
                            </div>
                        ))}
                    </div>
                )}
            </section>

            {/* Info Section (Essence) */}
            <section className="essence-section">
                <h2 className="essence-title">Preservando nuestra esencia en la era digital</h2>
                <p className="essence-text">
                    En respuesta a los cambios en los hábitos de consumo de literatura y la creciente demanda de experiencias de compra en línea, Relatos de Papel se embarca en la transformación digital. Nuestra misión es preservar la esencia de nuestra librería mientras nos adaptamos a las nuevas realidades del mercado, expandiendo nuestro alcance más allá de las fronteras físicas.
                </p>

                <div className="features-grid">
                    <div className="feature-item">
                        <div className="feature-icon"><i className="bi bi-book"></i></div>
                        <h3>Catálogo Extenso</h3>
                        <p>Explora nuestro vasto catálogo de libros desde cualquier ubicación, encuentra títulos clásicos y novedades del mercado.</p>
                    </div>
                    <div className="feature-item">
                        <div className="feature-icon"><i className="bi bi-tablet"></i></div>
                        <h3>Libros Digitales</h3>
                        <p>Recibe libros en formato digital al instante. Acceso inmediato a tu contenido favorito vinculado a tu cuenta de usuario.</p>
                    </div>
                    <div className="feature-item">
                        <div className="feature-icon"><i className="bi bi-globe"></i></div>
                        <h3>Compra en Línea</h3>
                        <p>Una plataforma completa y segura para explorar, comprar y recibir tus libros favoritos, tanto físicos como digitales.</p>
                    </div>
                </div>
            </section>
        </div>
    );
}
