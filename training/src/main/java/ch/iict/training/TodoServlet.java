package ch.iict.training;

import ch.iict.training.entities.Task;
import ch.iict.training.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;

@WebServlet(urlPatterns = {"/tasks", "/add", "/delete", "/toggle", "/edit", "/update"})
public class TodoServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        EntityManager em = (EntityManager) req.getAttribute("em");
        var repo = new TaskRepository(em);

        String path = req.getServletPath();
        if ("/edit".equals(path)) {
            Long id = Long.valueOf(req.getParameter("id"));
            Task editing = repo.findById(id);
            req.setAttribute("editingTask", editing);
        }

        req.setAttribute("tasks", repo.findAll());
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        EntityManager em = (EntityManager) req.getAttribute("em");
        var repo = new TaskRepository(em);
        String path = req.getServletPath();

        switch (path) {
            case "/add" -> {
                Task t = new Task();
                t.setTitle(req.getParameter("title"));
                t.setDescription(req.getParameter("description"));
                t.setDone(false);
                t.setDueDate(parseDate(req.getParameter("dueDate")));
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

        resp.sendRedirect(req.getContextPath() + "/tasks");
    }

    private LocalDate parseDate(String iso) {
        if (iso == null || iso.isBlank()) return null;
        return LocalDate.parse(iso);
    }
}
