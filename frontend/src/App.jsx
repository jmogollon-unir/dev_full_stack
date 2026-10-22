import { BrowserRouter, useLocation } from "react-router-dom";
import "bootstrap-icons/font/bootstrap-icons.css";
import AppRoutes from "./routes/AppRoutes";
import Header from "./components/Header/Header";
import HeaderBookDetail from "./components/HeaderBookDetails/HeaderBookDetails";
import HeaderSearch from "./components/HeaderSearch/HeaderSearch";
import Footer from "./components/Footer/Footer";
import { AuthProvider } from "./context/auth/AuthProvider";
import { GlobalProvider } from "./context/global/GlobalProvider";
import { CartProvider } from "./context/cart/CartProvider";

function AppContent() {
  const location = useLocation();
  const noHeaderRoutes = ["/","/login"];
  const hideHeader = noHeaderRoutes.includes(location.pathname);
  const isHeaderSearchRoute = location.pathname === "/books";
  const headersRoutes = ["/checkout", "/profile", "/register"];
  const isHeaderRoute = headersRoutes.includes(location.pathname);
  const isHeaderBookDetailRoute =
    location.pathname.startsWith("/books/") || location.pathname === "/profile";

  const noFooterRoutes = ["/login"];
  const hideFooter = noFooterRoutes.includes(location.pathname);

  return (
    <>
      {!hideHeader &&
        (isHeaderSearchRoute ? (
          <HeaderSearch />
        ) : isHeaderBookDetailRoute ? (
          <HeaderBookDetail />
        ) : isHeaderRoute ? (
          <Header />
        ) : (
          <HeaderBookDetail />
        ))}
      <AppRoutes />
      {!hideFooter && <Footer />}
    </>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <GlobalProvider>
          <CartProvider>
            <AppContent />
          </CartProvider>
        </GlobalProvider>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
