import "./Header.css";
import { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { AuthContext } from "../../context/auth/AuthContext.jsx";
import { useLogin } from "../../hooks/useLogin.js";
import logo from "../../assets/logo.png";

export default function Header() {
    const navigate = useNavigate();
    const { user } = useContext(AuthContext);
    const { logout } = useLogin();

    const handleLogoClick = () => {
        navigate("/");
    };

    return (
        <header className="main-header">
            <div className="header-content">
                {/* Logo section */}
                <button className="brand-button" type="button" onClick={handleLogoClick}>
                    <img src={logo} alt="Relatos de Papel" className="logo" />
                    <div className="brand-text">
                        <p className="text-title">Relatos de Papel</p>
                        <p className="text-slogan">Tu próxima historia comienza aquí</p>
                    </div>
                </button>
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