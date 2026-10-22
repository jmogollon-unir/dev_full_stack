import "./Logo.css";
import { useNavigate } from "react-router-dom";
import logoImg from "../../assets/logo.png";

export default function Logo() {
    const navigate = useNavigate();

    return (
        <div className="brand-container" onClick={() => navigate("/")}>
            <img src={logoImg} alt="Relatos de Papel" className="logo-img" />
            <div className="brand-text-group">
                <p className="brand-title">Relatos de Papel</p>
                <p className="brand-slogan">Tu próxima historia comienza aquí</p>
            </div>
        </div>
    );
}