package ch.iict.training.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@WebFilter("/*")
public class CorsFilter implements Filter {

    private static final Set<String> ALLOWED_ORIGINS = Set.of(
            "http://localhost:4200"         // Angular dev
    );

    private static final boolean ALLOW_CREDENTIALS = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String origin = req.getHeader("Origin");
        boolean originAllowed = origin != null && (ALLOWED_ORIGINS.contains(origin));

        if (originAllowed) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            if (ALLOW_CREDENTIALS) res.setHeader("Access-Control-Allow-Credentials", "true");
            // Pour que les caches proxy différencient selon l’origine/headers
            res.addHeader("Vary", "Origin");
            res.addHeader("Vary", "Access-Control-Request-Method");
            res.addHeader("Vary", "Access-Control-Request-Headers");
        }

        // preflight
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())
                && req.getHeader("Access-Control-Request-Method") != null) {

            if (originAllowed) {
                res.setHeader("Access-Control-Allow-Methods", "GET,POST,PUT,PATCH,DELETE,OPTIONS");
                // Autorise au minimum Content-Type
                String reqHeaders = req.getHeader("Access-Control-Request-Headers");
                res.setHeader("Access-Control-Allow-Headers",
                        reqHeaders != null ? reqHeaders : "Content-Type");
                res.setHeader("Access-Control-Max-Age", "86400"); // 24h
            }
            res.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204
            return;
        }

        // Requête “normale” → passe à la suite
        chain.doFilter(request, response);
    }
}
