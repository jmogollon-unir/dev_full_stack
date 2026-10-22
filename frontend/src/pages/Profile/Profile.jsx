import React, { useContext, useEffect, useState } from "react";
import "./Profile.css";
import { AuthContext } from "../../context/auth/AuthContext.jsx";
import { useLogin } from "../../hooks/useLogin.js";
import { useNavigate } from "react-router-dom";
import { getOrdersByUser } from "../../services/api";

export default function Profile() {
  const { user } = useContext(AuthContext);
  const { logout } = useLogin();
  const navigate = useNavigate();
  const [orders, setOrders] = useState([]);
  const [ordersLoading, setOrdersLoading] = useState(false);
  const [ordersError, setOrdersError] = useState("");

  useEffect(() => {
    if (!user?.userId) return;

    let ignore = false;

    const loadOrders = async () => {
      setOrdersLoading(true);
      setOrdersError("");

      try {
        const apiOrders = await getOrdersByUser(user.userId);
        if (!ignore) {
          setOrders(apiOrders);
        }
      } catch {
        if (!ignore) {
          setOrders([]);
          setOrdersError("No se pudieron cargar los pedidos desde el backend. Mostrando datos locales.");
        }
      } finally {
        if (!ignore) {
          setOrdersLoading(false);
        }
      }
    };

    loadOrders();
    return () => {
      ignore = true;
    };
  }, [user?.userId]);

  if (!user) {
    return null;
  }

  const sourceOrders = orders.length > 0 ? orders : (user.recentOrders || []);
  const recentOrders = sourceOrders.slice(0, 5);
  const totalOrders = sourceOrders.length;
  const totalBooks = sourceOrders.reduce(
    (sum, order) => sum + order.items.reduce((acc, item) => acc + item.quantity, 0),
    0
  );
  const totalSpent = sourceOrders.reduce((sum, order) => sum + Number(order.total || 0), 0);

  const getStatusClass = (status) => {
    switch (status) {
      case "entregado":
      case "delivered":
        return "status-completed";
      case "en_proceso":
      case "processing":
      case "shipped":
        return "status-processing";
      case "pendiente":
      case "pending":
        return "status-pending";
      default:
        return "status-default";
    }
  };

  const getStatusText = (status) => {
    switch (status) {
      case "entregado":
      case "delivered":
        return "Completado";
      case "en_proceso":
      case "processing":
        return "En proceso";
      case "shipped":
        return "Enviado";
      case "pendiente":
      case "pending":
        return "Pendiente";
      default:
        return status;
    }
  };

  const formatDate = (dateString) =>
    new Date(dateString).toLocaleDateString("es-ES", {
      year: "numeric",
      month: "long",
      day: "numeric"
    });

  const formatPrice = (price) =>
    new Intl.NumberFormat("es-ES", {
      style: "currency",
      currency: "EUR"
    }).format(price);

  return (
    <div className="profile-page">
      <h1 className="profile-title">Mi Perfil</h1>

      <div className="profile-layout">
        <aside className="profile-sidebar">
          <section className="profile-card">
            <div className="profile-user">
              <div className="avatar">
                <i className="bi bi-person"></i>
              </div>
              <div>
                <h2>{user.company.name}</h2>
                <span>{user.company.sector}</span>
              </div>
            </div>

            <div className="profile-info">
              <div className="info-row">
                <i className="bi bi-envelope"></i>
                <div>
                  <small>Email</small>
                  <p>{user.company.email}</p>
                </div>
              </div>
              <div className="info-row">
                <i className="bi bi-telephone"></i>
                <div>
                  <small>Teléfono</small>
                  <p>{user.company.phone}</p>
                </div>
              </div>
              <div className="info-row">
                <i className="bi bi-geo-alt"></i>
                <div>
                  <small>Dirección</small>
                  <p>{user.company.address}</p>
                </div>
              </div>
            </div>

            <div className="profile-actions">
              <button type="button" className="outline-btn">
                <i className="bi bi-truck"></i>
                Seguimiento de Pedidos
              </button>
              <button type="button" className="outline-btn">
                <i className="bi bi-chat-dots"></i>
                Soporte
              </button>
              <button type="button" className="outline-btn">
                <i className="bi bi-gear"></i>
                Configuración
              </button>


              <button
                type="button"
                className="primary-btn"
                onClick={() => navigate("/books")}
              >
                Ir al Catálogo
              </button>


              <button type="button" className="secondary-btn" onClick={logout}>
                Cerrar Sesión
              </button>
            </div>
          </section>

          <section className="stats-card">
            <h3>Estadísticas</h3>
            <div className="stat-row">
              <span>Total de pedidos</span>
              <strong>{totalOrders}</strong>
            </div>
            <div className="stat-row">
              <span>Libros comprados</span>
              <strong>{totalBooks}</strong>
            </div>
            <div className="stat-row">
              <span>Total gastado</span>
              <strong>{formatPrice(totalSpent)}</strong>
            </div>
          </section>
        </aside>

        <section className="profile-orders">
          <div className="orders-header">
            <i className="bi bi-box"></i>
            <h2>Últimos 5 Pedidos</h2>
          </div>

          <div className="orders-list">
            {ordersLoading ? (
              <div className="empty-orders">Cargando pedidos...</div>
            ) : recentOrders.length === 0 ? (
              <div className="empty-orders">No hay pedidos recientes</div>
            ) : (
              recentOrders.map((order, index) => (
                <article key={order.orderId || order.id} className="order-card">
                  <div className="order-card-header">
                    <div className="order-icon">
                      <i className="bi bi-box-seam"></i>
                    </div>
                    <div className="order-details">
                      <h4>Pedido #{order.orderId || String(index + 1).padStart(3, "0")}</h4>
                      <span>
                        <i className="bi bi-calendar"></i>
                        {formatDate(order.orderDate || order.date)}
                      </span>
                    </div>
                    <div className="order-meta">
                      <span className={`status-pill ${getStatusClass(order.status_name || order.status)}`}>
                        {getStatusText(order.status_name || order.status)}
                      </span>
                      <strong>{formatPrice(Number(order.total || 0))}</strong>
                    </div>
                  </div>

                  <div className="order-items">
                    {order.items.map((item, idx) => (
                      <div key={idx} className="order-item">
                        <div>
                          <p className="item-title">{item.bookTitle || item.name || `Libro #${item.bookId}`}</p>
                          <span className="item-author">{item.author || ""}</span>
                        </div>
                        <div className="item-qty">{item.quantity} x {formatPrice(Number(item.price || 0))}</div>
                      </div>
                    ))}
                  </div>
                </article>
              ))
            )}
            {ordersError && <div className="empty-orders">{ordersError}</div>}
          </div>
        </section>
      </div>
    </div>
  );
}
