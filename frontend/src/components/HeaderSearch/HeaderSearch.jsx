import "./HeaderSearch.css";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { GlobalContext } from "../../context/global/GlobalContext";
import { AuthContext } from "../../context/auth/AuthContext.jsx";
import logo from "../../assets/logo.png";
import {useLogin} from "../../hooks/useLogin.js";

export default function HeaderSearch() {
  const navigate = useNavigate();
  const { searchTerm, setSearchTerm } = useContext(GlobalContext);
  const { user } = useContext(AuthContext);
  const { logout } = useLogin();
  const handleLogoClick = () => {
    setSearchTerm(""); // clear search input
    navigate("/");
  };

  return (
    <header className="main-header-search">
      <div className="header-content">
        {/* Logo section */}
        <button className="brand-button" type="button" onClick={handleLogoClick}>
          <img src={logo} alt="Relatos de Papel" className="logo" />
          <div className="brand-text">
            <p className="text-title">Relatos de Papel</p>
            <p className="text-slogan">Tu próxima historia comienza aquí</p>
          </div>
        </button>
        {/* Search section */}
        <div className="header-search">
          <div className="search-box">
            <i className="bi-search search-icon" />
            <input type="text" placeholder="Buscar por título, autor o género..." value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)} className="search-input-field"
            />
          </div>
        </div>
        {/* Login and register section */}
        <div className="header-actions">
          {user ? (

              <>
                <button
                    type="button"
                    className="btn-user"
                    onClick={() => navigate("/profile")}
                    aria-label="Ir a perfil"
                    title="Perfil"
                >
                  <i className="bi bi-person"></i>
                </button>
                <button
                    type="button"
                    className="btn-user"
                    onClick={logout}
                    aria-label="Cerrar sesión"
                    title="Cerrar sesión"
                >
                  <i className="bi bi-box-arrow-right"></i>
                </button>
              </>
          ) : (
            <>
              <button onClick={() => navigate("/login")} className="btn btn-secondary">Iniciar Sesión</button>
              <button onClick={() => navigate("/register")} className="btn btn-primary">Registrarse</button>
            </>
          )}
        </div>
      </div>
    </header>
  );
}