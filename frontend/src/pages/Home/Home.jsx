import "./Home.css";
import { useState, useContext } from "react";
import { useNavigate } from "react-router-dom";
import { useBooks } from "../../hooks/useBooks";
import { GlobalContext } from "../../context/global/GlobalContext";
import home_img from "../../assets/home_img.jpg";

export default function Home() {
    const navigate = useNavigate();
    const { books, loading, error: fetchError } = useBooks();
    const { searchTerm, setSearchTerm } = useContext(GlobalContext);
    const [showFilters, setShowFilters] = useState(false);
    const [selectedGenre, setSelectedGenre] = useState("Todos");
    const [sortBy, setSortBy] = useState("default");
    const [priceRange, setPriceRange] = useState({ min: 0, max: 100 });

    const uniqueGenres = ["Todos", ...Array.from(new Set(books.map((book) => book.genre).filter(Boolean)))];

    // Filter books
    let filteredBooks = books.filter((book) => {
        const matchesSearch =
            book.title.toLowerCase().includes(searchTerm.toLowerCase()) ||
            book.author.toLowerCase().includes(searchTerm.toLowerCase())
        const matchesGenre = selectedGenre === "Todos" || book.genre === selectedGenre;
        const matchesPrice = book.price >= priceRange.min && book.price <= priceRange.max;
        return matchesSearch && matchesGenre && matchesPrice;
    });

    // Sort books
    if (sortBy === "price-asc") {
        filteredBooks = [...filteredBooks].sort((a, b) => a.price - b.price);
    } else if (sortBy === "price-desc") {
        filteredBooks = [...filteredBooks].sort((a, b) => b.price - a.price);
    } else if (sortBy === "date") {
        filteredBooks = [...filteredBooks].sort((a, b) => parseInt(b.date) - parseInt(a.date),);
    } else if (sortBy === "popularity") {
        filteredBooks = [...filteredBooks].sort((a, b) => b.popularity - a.popularity);
    }

    // Detect if filters are applied
    const hasFiltersApplied =
        selectedGenre !== "Todos" ||
        sortBy !== "default" ||
        priceRange.min !== 0 ||
        priceRange.max !== 100 ||
        searchTerm.trim() !== "";

    // Determine which books to show
    const catalogTitle = hasFiltersApplied ? "Resultados de búsqueda" : "Catálogo Completo";
    const booksToDisplay = hasFiltersApplied ? { [catalogTitle]: filteredBooks } : { [catalogTitle]: filteredBooks };

    return (
        <div className="home-container">
            {/* Banner Section */}
            <div className="banner-section">
                <img src={home_img} alt="Home Librería Relatos de Papel" className="banner-background" />
                <div className="div-banner-title"></div>
                <div className="banner-content">
                    <h1 className="banner-title">Encuentra tu próxima historia</h1>
                    <p className="banner-subtitle">Explora nuestra colección de libros físicos y digitales</p>
                </div>
            </div>
            {/* Filters Section */}
            <div className="filters-section">
                <div className="filters-container">
                    <div className="filters-header">
                        <div className="filters-header" style={{ display: 'flex', flexWrap: 'wrap', gap: '10px' }}>
                            <h2 className="filters-title">{filteredBooks.length} libros encontrados</h2>
                            <button onClick={() => setShowFilters(!showFilters)} className="filters-btn">
                                <i className="bi-sliders"></i>
                                {showFilters ? "Ocultar Filtros" : "Mostrar Filtros"}
                            </button>
                            {showFilters && (
                                <button className="clear-filters-btn" onClick={() => {
                                    setSearchTerm("");
                                    setSelectedGenre("Todos");
                                    setSortBy("default");
                                    setPriceRange({ min: 0, max: 100 });
                                }}>Limpiar Filtros</button>
                            )}
                        </div>
                    </div>
                    {showFilters && (
                        <div className="filters-content">
                            {/* Genre Filter */}
                            <div className="filter-group">
                                <label className="filter-label">Género</label>
                                <select className="filter-select" value={selectedGenre}
                                    onChange={(e) => setSelectedGenre(e.target.value)}>
                                    {uniqueGenres.map((genre) => (<option key={genre} value={genre}>{genre}</option>))}
                                </select>
                            </div>
                            {/* Sort Filter */}
                            <div className="filter-group">
                                <label className="filter-label">Ordenar por</label>
                                <select className="filter-select" value={sortBy} onChange={(e) => setSortBy(e.target.value)}>
                                    <option value="default">Predeterminado</option>
                                    <option value="price-asc">Precio: Menor a Mayor</option>
                                    <option value="price-desc">Precio: Mayor a Menor</option>
                                    <option value="date">Más Recientes</option>
                                    <option value="popularity">Más Populares</option>
                                </select>
                            </div>
                            {/* Price Range */}
                            <div className="filter-group">
                                <label className="filter-label"> Rango de Precio: ${priceRange.min} - ${priceRange.max}</label>
                                <div className="price-inputs">
                                    <input type="number" value={priceRange.min} placeholder="Mín" className="filter-input"
                                        onChange={(e) =>
                                            setPriceRange({
                                                ...priceRange,
                                                min: Number(e.target.value),
                                            })
                                        } />
                                    <input type="number" className="filter-input" value={priceRange.max} placeholder="Máx"
                                        onChange={(e) =>
                                            setPriceRange({
                                                ...priceRange,
                                                max: Number(e.target.value),
                                            })
                                        } />
                                </div>
                            </div>
                        </div>
                    )}
                </div>
            </div>
            {/* Main Content */}
            <main className="books-section">
                {loading && <p className="loading-message">Cargando productos...</p>}
                {fetchError && <p className="fetch-error">{fetchError}</p>}
                {!loading && filteredBooks.length === 0 ? (
                    <div className="no-results">
                        <div className="no-results-icon">
                            <i className="bi bi-book"></i>
                        </div>
                        <h2 className="no-results-title">No se encontraron libros</h2>
                        <p className="no-results-text"> Intenta ajustar tus filtros o búsqueda</p>
                        <button className="clear-filters-btn" onClick={() => {
                            setSearchTerm("");
                            setSelectedGenre("Todos");
                            setSortBy("default");
                            setPriceRange({ min: 0, max: 100 });
                        }}>Limpiar Filtros</button>
                    </div>
                ) : (
                    Object.entries(booksToDisplay).map(([genre, books]) => { // group by genre and iterate books
                        { loading && <p className="loading-message">Cargando productos...</p> }
                        { fetchError && <p className="fetch-error">{fetchError}</p> }
                        if (books.length === 0) return null;
                        return (
                            <div key={genre} className="mb-16">
                                <h2 className="text-3xl font-bold text-gray-900 mb-8 text-center title-stock">{genre}</h2>
                                <div className={`books-grid ${!hasFiltersApplied ? 'books-grid-small' : ''}`}>
                                    {books.map((book) => (
                                        <div key={book.id} className="book-card" onClick={() => navigate(`/books/${book.id}`)}>
                                            <div className="book-card-image">
                                                <img src={book.cover} alt={book.title} />
                                                {!book.isAvailable && (
                                                    <div className="book-card-out-of-stock">
                                                        <span className="out-of-stock-badge">Agotado</span>
                                                    </div>)}
                                            </div>
                                            <h3 className="book-title">{book.title}</h3>
                                            <p className="book-author">{book.author}</p>
                                            <span className="book-price">€{book.price}</span>
                                        </div>
                                    ))}
                                </div>
                            </div>
                        );
                    })
                )}
            </main>
        </div>
    );
}
