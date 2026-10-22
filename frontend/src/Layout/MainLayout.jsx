import React from 'react'
import { Outlet } from 'react-router-dom'
import Header from '../components/Header/Header'
import Footer from '../components/Footer/Footer'
import SideCart from '../components/SideCart/SideCart'

export default function MainLayout() {
  return (
    <>
      <SideCart />
      <main>
        <Outlet />
      </main>
    </>
  )
}
