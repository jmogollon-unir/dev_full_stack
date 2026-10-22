import React, {useEffect, useMemo, useState} from 'react'
import {CartContext} from './CartContext'

const STORAGE_KEY = 'unir_supplies_cart'

function readStoredCart() {
    try {
        if (typeof window === 'undefined') return []
        const raw = window.localStorage.getItem(STORAGE_KEY)
        return raw ? JSON.parse(raw) : []
    } catch {
        return []
    }
}

export function CartProvider({children}) {
    const [cart, setCart] = useState(readStoredCart)
    const [isCartOpen, setIsCartOpen] = useState(false)

    useEffect(() => {
        try {
            window.localStorage.setItem(STORAGE_KEY, JSON.stringify(cart))
        } catch {
            // solo try
        }
    }, [cart])

    const addItem = (book, quantity = 1) => {
        if (!book?.id) return

        setCart((currentCart) => {
            const existingItem = currentCart.find((item) => item.id === book.id)
            const nextQuantity = Math.max(1, quantity)
            const maxStock = Number.isFinite(book.stock) ? book.stock : Infinity

            if (existingItem) {
                return currentCart.map((item) =>
                    item.id === book.id
                        ? {...item, quantity: Math.min(item.quantity + nextQuantity, maxStock)}
                        : item
                )
            }

            return [
                ...currentCart,
                {
                    id: book.id,
                    title: book.title,
                    author: book.author,
                    editorial: book.editorial,
                    type: book.type,
                    formato: book.formato || book.type || '',
                    price: Number(book.price) || 0,
                    stock: book.stock ?? 0,
                    cover: book.cover || book.images?.[0] || '',
                    image: book.images?.[0] || book.cover || '',
                    quantity: Math.min(nextQuantity, maxStock)
                }
            ]
        })

        setIsCartOpen(true)
    }

    const removeItem = (id) => {
        setCart((currentCart) => currentCart.filter((item) => item.id !== id))
    }

    const updateQuantity = (id, quantity) => {
        const safeQuantity = Number(quantity)
        if (!Number.isFinite(safeQuantity) || safeQuantity <= 0) {
            removeItem(id)
            return
        }

        setCart((currentCart) =>
            currentCart.map((item) =>
                item.id === id
                    ? {...item, quantity: Math.min(safeQuantity, item.stock || safeQuantity)}
                    : item
            )
        )
    }

    const clearCart = () => setCart([])
    const toggleCart = () => setIsCartOpen((value) => !value)
    const openCart = () => setIsCartOpen(true)
    const closeCart = () => setIsCartOpen(false)

    const totalItems = useMemo(
        () => cart.reduce((sum, item) => sum + item.quantity, 0),
        [cart]
    )

    const totalAmount = useMemo(
        () => cart.reduce((sum, item) => sum + item.quantity * item.price, 0),
        [cart]
    )

    const value = {
        cart,
        isCartOpen,
        addItem,
        removeItem,
        updateQuantity,
        clearCart,
        toggleCart,
        openCart,
        closeCart,
        totalItems,
        totalAmount
    }

    return <CartContext.Provider value={value}>{children}</CartContext.Provider>
}

