package ch.iict.training.bootstrap;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

/*
* Cette classe est instanciée automatiquement au démarrage
*/
@WebListener
public class JpaBootstrap implements ServletContextListener {
    public static final String EMF_KEY = "emf";
    private EntityManagerFactory emf;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        emf = Persistence.createEntityManagerFactory("todoPU");

        // On pose l'EntityManagerFactory dans le contexte de l'application pour etre accessible partout
        sce.getServletContext().setAttribute(EMF_KEY, emf);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (emf != null && emf.isOpen()) emf.close();
    }
}
