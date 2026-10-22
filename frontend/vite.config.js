import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // En desarrollo (npm run dev) la API corre en Spring Boot (:8080); se reenvía /api para evitar CORS.
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})
