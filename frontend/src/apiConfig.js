/**
 * Base de la API. Por defecto vacía = mismo origen:
 *  - producción: el JAR de Spring Boot sirve el front y la API en el mismo host/puerto
 *  - desarrollo (npm run dev): Vite reenvía /api a http://localhost:8080 (vite.config.js)
 * VITE_API_URL opcional, ej. http://localhost:8080
 */
export const API_BASE_URL = (import.meta.env.VITE_API_URL ?? "").replace(/\/$/, "");

/**
 * @param {string} _service reservado por compatibilidad (ignorado)
 * @param {string} path ruta absoluta del API, ej. /api/users/login
 */
export const buildApiUrl = (_service, path) => {
  const normalized = path.startsWith("/") ? path : `/${path}`;
  return `${API_BASE_URL}${normalized}`;
};
