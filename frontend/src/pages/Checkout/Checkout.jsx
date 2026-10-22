import React, { useMemo, useState } from 'react'
import { Link } from 'react-router-dom'
import useCart from '../../hooks/useCart'
import { AuthContext } from '../../context/auth/AuthContext.jsx'
import { createOrder } from '../../services/api'
import './Checkout.css'

function normalizeText(value = '') {
  return String(value)
    .normalize('NFD')
    .replace(/\p{Diacritic}/gu, '')
    .toLowerCase()
}

const VALID_COUPONS = {
  LIBRO10: 0.1,
  BIENVENIDO: 0.1,
  LECTURA20: 0.2
}

export default function Checkout() {
  const { cart, totalAmount, clearCart } = useCart()
  const { user } = React.useContext(AuthContext)
  const [purchaseSuccess, setPurchaseSuccess] = useState(false)
  const [createdOrder, setCreatedOrder] = useState(null)
  const [isSubmitting, setIsSubmitting] = useState(false)
  const [submitError, setSubmitError] = useState('')
  const [selectedPayment, setSelectedPayment] = useState('credit')
  const [couponCode, setCouponCode] = useState('')
  const [appliedCoupon, setAppliedCoupon] = useState(null)
  const [couponError, setCouponError] = useState('')
  const [hasPhysicalItemState, setHasPhysicalItemState] = useState(false)
  const itemCount = cart.reduce((sum, item) => sum + item.quantity, 0)

  const hasPhysicalItem = useMemo(
    () => cart.some((item) => normalizeText(item.formato || item.type).includes('fisic')),
    [cart]
  )

  const discountPercentage = appliedCoupon ? VALID_COUPONS[appliedCoupon] : 0
  const discountAmount = totalAmount * discountPercentage
  const finalTotal = totalAmount - discountAmount

  const handleApplyCoupon = () => {
    setCouponError('')
    const trimmedCode = couponCode.trim().toUpperCase()

    if (!trimmedCode) {
      setCouponError('Por favor ingresa un código de cupón')
      return
    }

    if (VALID_COUPONS[trimmedCode]) {
      setAppliedCoupon(trimmedCode)
      setCouponCode('')
    } else {
      setCouponError('Cupón inválido. Intenta con LIBRO10, BIENVENIDO o LECTURA20')
    }
  }

  const handleConfirmPurchase = async () => {
    if (cart.length === 0) return

    setIsSubmitting(true)
    setSubmitError('')

    try {
      if (!user?.userId) {
        setSubmitError("Inicia sesión para finalizar la compra.");
        setIsSubmitting(false);
        return;
      }
      const order = await createOrder({
        userId: user.userId,
        items: cart
      });

      setCreatedOrder(order)
      window.alert(`¡Compra realizada con éxito!\n\nPedido #${order.orderId}\nTotal: €${Number(order.total || finalTotal).toFixed(2)}\n\nTu pedido ha sido confirmado.`)
      setPurchaseSuccess(true)
      setHasPhysicalItemState(hasPhysicalItem)
      setAppliedCoupon(null)
      setCouponCode('')
      clearCart()
    } catch {
      setSubmitError('No se pudo crear el pedido. Revisa que el backend esté levantado y que la base de datos esté disponible.')
    } finally {
      setIsSubmitting(false)
    }
  }

  return (
    <main className="checkout-page">
      <header className="checkout-header">
        <h1>Finalizar Compra</h1>
        <p>Revisa tu pedido antes de confirmar la compra.</p>
      </header>

      <div className="checkout-layout">
        <section className="checkout-form-column">
          <article className="checkout-card">
            <div className="checkout-card-title">
              <span className="checkout-icon">👤</span>
              <h2>Información del Cliente</h2>
            </div>

            <div className="field-group">
              <label className="required">Nombre Completo</label>
              <input type="text" placeholder="Juan Pérez" disabled={purchaseSuccess} required />
            </div>

            <div className="field-group">
              <label className="required">Email</label>
              <input type="email" placeholder="juan@ejemplo.com" disabled={purchaseSuccess} required />
            </div>
          </article>

          {(hasPhysicalItem || (purchaseSuccess && hasPhysicalItemState)) && (
            <article className="checkout-card">
              <div className="checkout-card-title">
                <span className="checkout-icon">📍</span>
                <h2>Dirección de Envío</h2>
              </div>

              <div className="field-group">
                <label>Dirección</label>
                <input type="text" placeholder="Calle Principal 123" disabled={purchaseSuccess} />
              </div>

              <div className="grid-2">
                <div className="field-group">
                  <label>Ciudad</label>
                  <input type="text" placeholder="Madrid" disabled={purchaseSuccess} />
                </div>
                <div className="field-group">
                  <label>Código Postal</label>
                  <input type="text" placeholder="28001" disabled={purchaseSuccess} />
                </div>
              </div>
            </article>
          )}

          <article className="checkout-card">
            <div className="checkout-card-title">
              <span className="checkout-icon">💳</span>
              <h2>Método de Pago</h2>
            </div>

            <div className="payment-options">
              <button
                type="button"
                className={`payment-option ${selectedPayment === 'credit' ? 'active' : ''}`}
                onClick={() => setSelectedPayment('credit')}
                disabled={purchaseSuccess}
              >
                Tarjeta de Crédito
              </button>
              <button
                type="button"
                className={`payment-option ${selectedPayment === 'debit' ? 'active' : ''}`}
                onClick={() => setSelectedPayment('debit')}
                disabled={purchaseSuccess}
              >
                Tarjeta de Débito
              </button>
              <button
                type="button"
                className={`payment-option ${selectedPayment === 'paypal' ? 'active' : ''}`}
                onClick={() => setSelectedPayment('paypal')}
                disabled={purchaseSuccess}
              >
                PayPal
              </button>
            </div>

            <div className="field-group">
              <label className="required">Número de Tarjeta</label>
              <input type="text" placeholder="1234 5678 9012 3456" disabled={purchaseSuccess} required />
            </div>

            <div className="grid-2">
              <div className="field-group">
                <label className="required">Fecha de Expiración</label>
                <input type="text" placeholder="MM/AA" disabled={purchaseSuccess} required />
              </div>
              <div className="field-group">
                <label className="required">CVV</label>
                <input type="text" placeholder="123" disabled={purchaseSuccess} required />
              </div>
            </div>
          </article>

          <button
            className="confirm-btn"
            onClick={handleConfirmPurchase}
            disabled={cart.length === 0 || purchaseSuccess || isSubmitting}
          >
            {isSubmitting ? 'Confirmando pedido...' : `Confirmar Pedido - €${finalTotal.toFixed(2)}`}
          </button>

          {purchaseSuccess && (
            <div className="checkout-alert success" role="alert" aria-live="polite">
              ¡Compra realizada con éxito! Tu pedido #{createdOrder?.orderId} ha sido confirmado.
            </div>
          )}

          {submitError && (
            <div className="checkout-alert empty" role="alert" aria-live="polite">
              {submitError}
            </div>
          )}

          {cart.length === 0 && !purchaseSuccess && (
            <div className="checkout-alert empty" role="alert" aria-live="polite">
              Tu carrito está vacío. Añade libros antes de finalizar la compra.
            </div>
          )}
        </section>

        <aside className="checkout-summary">
          <div className="checkout-card summary-card">
            <h2>Resumen del Pedido ({itemCount} artículos)</h2>

            <div className="coupon-box">
              <h3>Cupón de Descuento</h3>
              {appliedCoupon ? (
                <div className="coupon-applied">
                  <p className="coupon-success">Cupón {appliedCoupon} aplicado ({Math.round(discountPercentage * 100)}% desc.)</p>
                  <button
                    type="button"
                    className="coupon-remove-btn"
                    onClick={() => {
                      setAppliedCoupon(null)
                      setCouponError('')
                    }}
                  >
                    Remover cupón
                  </button>
                </div>
              ) : (
                <>
                  <div className="coupon-row">
                    <input
                      type="text"
                      placeholder="Código de cupón"
                      value={couponCode}
                      onChange={(e) => {
                        setCouponCode(e.target.value)
                        setCouponError('')
                      }}
                      disabled={purchaseSuccess}
                    />
                    <button
                      type="button"
                      onClick={handleApplyCoupon}
                      disabled={purchaseSuccess}
                    >
                      Aplicar
                    </button>
                  </div>
                  {couponError && <p className="coupon-error">{couponError}</p>}
                  <p className="coupon-help">Cupones válidos: LIBRO10, BIENVENIDO, LECTURA20</p>
                </>
              )}
            </div>

            <div className="summary-items">
              {cart.length > 0 ? (
                cart.map((item) => {
                  const cover = item.cover || item.image || 'https://via.placeholder.com/84x112?text=Libro'
                  return (
                    <div key={item.id} className="summary-item">
                      <img className="summary-cover" src={cover} alt={item.title} />
                      <div className="summary-item-info">
                        <strong>{item.title}</strong>
                        <span>{item.author}</span>
                        <span className="summary-meta">{item.formato || item.type || 'Formato no disponible'}</span>
                        <span>x{item.quantity}</span>
                      </div>
                      <strong className="summary-item-price">€{(item.price * item.quantity).toFixed(2)}</strong>
                    </div>
                  )
                })
              ) : (
                <p className="summary-empty">No hay productos en el carrito.</p>
              )}
            </div>

            {!hasPhysicalItem && cart.length > 0 && !purchaseSuccess && (
              <div className="digital-note">
                Este pedido contiene solo productos digitales. No se requiere dirección de envío.
              </div>
            )}

            <div className="summary-totals">
              <div>
                <span>Subtotal:</span>
                <strong>€{totalAmount.toFixed(2)}</strong>
              </div>
              {appliedCoupon && (
                <div className="discount-row">
                  <span>Descuento ({discountPercentage * 100}%):</span>
                  <strong className="discount-amount">-€{discountAmount.toFixed(2)}</strong>
                </div>
              )}
              <div>
                <span>Envío:</span>
                <strong>{hasPhysicalItem || (purchaseSuccess && hasPhysicalItemState) ? 'Gratis' : 'No aplica'}</strong>
              </div>
              <div className="summary-total">
                <span>Total:</span>
                <strong>€{finalTotal.toFixed(2)}</strong>
              </div>
            </div>
          </div>

          <Link to="/books" className="back-link">
            ← Volver al catálogo
          </Link>
        </aside>
      </div>
    </main>
  )
}
