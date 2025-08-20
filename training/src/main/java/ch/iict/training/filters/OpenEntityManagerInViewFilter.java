package ch.iict.training.filters;

import ch.iict.training.bootstrap.JpaBootstrap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;

import java.io.IOException;

@WebFilter("/*")
public class OpenEntityManagerInViewFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        EntityManagerFactory emf = (EntityManagerFactory)
                req.getServletContext().getAttribute(JpaBootstrap.EMF_KEY);

        EntityManager em = emf.createEntityManager();
        req.setAttribute("em", em);

        em.getTransaction().begin();
        try {
            chain.doFilter(req, res);   // laisse passer vers la servlet
            em.getTransaction().commit();
        } catch (Throwable t) {
            if (em.getTransaction().isActive()) em.getTransaction().rollback();
            throw t;
        } finally {
            em.close();
        }
    }
}
