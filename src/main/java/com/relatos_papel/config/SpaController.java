package com.relatos_papel.config;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * El front (React Router) se sirve desde el propio JAR. Al recargar o abrir directamente una ruta
 * del SPA (/books, /login, ...), se devuelve index.html para que el router del front la resuelva.
 * Si añades rutas nuevas en frontend/src/routes/AppRoutes.jsx, inclúyelas aquí.
 */
@Controller
public class SpaController {

    @GetMapping({"/", "/login", "/books", "/books/{bookId}", "/checkout", "/profile", "/register"})
    public String index() {
        return "forward:/index.html";
    }
}
