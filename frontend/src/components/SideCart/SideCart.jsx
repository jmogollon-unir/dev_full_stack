import React from 'react'
import { Link } from 'react-router-dom'
import './SideCart.css'
import useCart from '../../hooks/useCart'

export default function SideCart() {
  const {
    cart,
    isCartOpen,
    openCart,
    closeCart,
    removeItem,
    updateQuantity,
    totalAmount,
    totalItems
  } = useCart()

  return (
    <>
      {isCartOpen && <div className="side-cart-overlay" onClick={closeCart}></div>}

      <button
        type="button"
        className={`side-cart-fab ${isCartOpen ? 'shifted' : ''}`}
        onClick={openCart}
        aria-label={`Ver carrito${totalItems > 0 ? ` (${totalItems} productos)` : ''}`}
      >
        <i className="bi bi-cart"></i>
        {totalItems > 0 && <span className="side-cart-badge">{totalItems}</span>}
      </button>

      <aside className={`side-cart ${isCartOpen ? 'open' : ''}`}>
        <div className="side-cart-header">
          <h2>Carrito de Compras</h2>
          <button className="close-btn" onClick={closeCart} aria-label="Cerrar carrito">✕</button>
        </div>

        <div className="side-cart-content">
          {cart.length > 0 ? (
            <>
              <div className="cart-items">
                {cart.map((item) => (
                  <div key={item.id} className="cart-item">
                    <img src={item.image} alt={item.title} className="cart-item-image" />
                    <div className="cart-item-body">
                      <div className="cart-item-header">
                        <h4>{item.title}</h4>
                        <button
                          className="remove-icon-btn"
                          onClick={() => removeItem(item.id)}
                          aria-label="Eliminar del carrito"
                          title="Eliminar"
                        >
                          <i className="bi bi-trash"></i>
                        </button>
                      </div>
                      <p>{item.author}</p>
                      <p className="cart-item-meta">{item.editorial} · {item.type}</p>
                      <div className="cart-item-footer">
                        <span className="cart-item-price">€{Number(item.price).toFixed(2)}</span>
                        <div className="cart-qty-controls">
                          <button
                            className="qty-btn"
                            onClick={() => updateQuantity(item.id, item.quantity - 1)}
                            aria-label="Disminuir cantidad"
                          >
                            -
                          </button>
                          <span className="qty-value">{item.quantity}</span>
                          <button
                            className="qty-btn"
                            onClick={() => updateQuantity(item.id, item.quantity + 1)}
                            aria-label="Aumentar cantidad"
                          >
                            +
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                ))}
              </div>

              <div className="side-cart-footer">
                <div className="side-cart-total">
                  <span>Total:</span>
                  <strong>€{totalAmount.toFixed(2)}</strong>
                </div>
                <Link to="/checkout" className="checkout-btn" onClick={closeCart}>
                  Proceder al Pago
                </Link>
              </div>
            </>
          ) : (
            <div className="empty-cart checkout-alert empty">
              <p>Tu carrito está vacío</p>
            </div>
          )}
        </div>
      </aside>
    </>
  )
}
