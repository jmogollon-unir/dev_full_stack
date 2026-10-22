import "./HeaderBookDetails.css";
import { useNavigate } from "react-router-dom";
import logo from "../../assets/logo.png";

export default function HeaderBookDetails() {
    const navigate = useNavigate();
    return (
        <header className="header-details">
            <div className="header-content-details">
                {/* Home page section */}
                <div className="breadcrumb-header">
                    <button className="back-button" type="button" onClick={() => navigate("/books")}>
                        <i className="bi bi-arrow-left"></i>
                        <span className="back-text-arrow">Volver al catálogo</span>
                    </button>
                </div>
                {/* Logo section */}
                <button className="back-button" type="button" onClick={() => navigate("/")}>
                    <img src={logo} alt="Relatos de Papel" className="logo-header-details" />
                    <div className="back-text">
                        <p className="text-title-header-details">Relatos de Papel</p>
                        <p className="text-slogan-header-details">Tu próxima historia comienza aquí</p>
                    </div>
                </button>
            </div>
        </header>
    );
}