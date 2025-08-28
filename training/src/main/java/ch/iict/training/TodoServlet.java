package ch.iict.training;

import ch.iict.training.entities.Task;
import ch.iict.training.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

// On route les URL vers ce servlet
@WebServlet(urlPatterns = {"/tasks", "/add", "/delete", "/toggle", "/edit", "/update"})
public class TodoServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // On récupère l'entityManager
        EntityManager em = (EntityManager) req.getAttribute("em");
        var repo = new TaskRepository(em);

        // Place la tache à éditer dans les attibuts
        String path = req.getServletPath();
        if ("/edit".equals(path)) {
            // Récupère le paramètre id
            Long id = Long.valueOf(req.getParameter("id"));

            // Trouve la tache à éditer
            Task editing = repo.findById(id);

            // Place la tache à éditer dans les attributs
            req.setAttribute("editingTask", editing);
        }
        // Place la liste de toutes les taches dans les attributs
        req.setAttribute("tasks", repo.findAll());

        // On passe la requete avec les données (attributs) à la JSP
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        // On récupère l'EM envoyé par le filtre
        EntityManager em = (EntityManager) req.getAttribute("em");
        var repo = new TaskRepository(em);
        String path = req.getServletPath();

        // En pratique, il faudrait déléguer ça à une classe, potentiellement command pattern
        switch (path) {
            case "/add" -> {
                String title = req.getParameter("title");
                String description = req.getParameter("description");
                LocalDate due = parseDate(req.getParameter("dueDate"));

                var errors = validateTaskParams(title, description, due);
                if (!errors.isEmpty()) {
                    // Remettre les valeurs pour ré-affichage
                    Map<String,String> form = new LinkedHashMap<>();
                    form.put("title", title);
                    form.put("description", description);
                    form.put("dueDate", req.getParameter("dueDate"));

                    req.setAttribute("errors", errors);
                    req.setAttribute("form", form);
                    
                    // Besoin aussi de la liste pour re-rendre la page
                    var repo2 = new TaskRepository(em);
                    req.setAttribute("tasks", repo2.findAll());
                    req.getRequestDispatcher("/index.jsp").forward(req, resp);
                    return;
                }

                Task t = new Task();
                t.setTitle(title);
                t.setDescription(description);
                t.setDone(false);
                t.setDueDate(due);
                repo.save(t);
            }
            case "/delete" -> {
                Long id = Long.valueOf(req.getParameter("id"));
                repo.delete(id);
            }
            case "/toggle" -> {
                Long id = Long.valueOf(req.getParameter("id"));
                Task t = repo.findById(id);
                if (t != null) {
                    t.setDone(!t.isDone());
                    repo.save(t);
                }
            }
            case "/update" -> {
                Long id = Long.valueOf(req.getParameter("id"));
                Task t = repo.findById(id);
                if (t != null) {
                    t.setTitle(req.getParameter("title"));
                    t.setDescription(req.getParameter("description"));
                    t.setDueDate(parseDate(req.getParameter("dueDate")));
                    t.setDone("on".equals(req.getParameter("done")) || "true".equalsIgnoreCase(req.getParameter("done")));
                    repo.save(t);
                }
            }
        }

        // Redirection: Demande au client de faire une nouvelle requete sur /tasks
        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private LocalDate parseDate(String iso) {
        if (iso == null || iso.isBlank()) return null;
        return LocalDate.parse(iso);
    }

    private Map<String,String> validateTaskParams(String title, String description, LocalDate due) {
        Map<String,String> errors = new LinkedHashMap<>();
        if (title == null || title.isBlank()) {
            errors.put("title", "Le titre est obligatoire.");
        } else if (title.length() > 100) {
            errors.put("title", "Maximum 100 caractères.");
        }
        else if (title.length() < 2) {
            errors.put("title", "Minimum 1 caractère.");
        }
        if (description != null && description.length() > 254) {
            errors.put("description", "Maximum 254 caractères.");
        }
        if (due != null && due.isBefore(LocalDate.now())) {
            errors.put("dueDate", "La date ne peut pas être dans le passé.");
        }
        return errors;
    }
}


/*
*


case "/update" -> {
    Long id = Long.valueOf(req.getParameter("id"));
    Task t = repo.findById(id);
    if (t != null) {
        String title = req.getParameter("title");
        String description = req.getParameter("description");
        LocalDate due = parseDate(req.getParameter("dueDate"));
        boolean done = "on".equals(req.getParameter("done")) ||
                       "true".equalsIgnoreCase(req.getParameter("done"));

        var errors = validateTaskParams(title, description, due);
        if (!errors.isEmpty()) {
            // Préparer un objet d'édition rempli avec les valeurs saisies
            Task edited = new Task();
            edited.setId(t.getId());
            edited.setTitle(title);
            edited.setDescription(description);
            edited.setDueDate(due);
            edited.setDone(done);

            req.setAttribute("editingTask", edited);
            req.setAttribute("errors", errors);

            // et la liste des tâches
            req.setAttribute("tasks", repo.findAll());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        t.setTitle(title);
        t.setDescription(description);
        t.setDueDate(due);
        t.setDone(done);
        repo.save(t);
    }
}

* */