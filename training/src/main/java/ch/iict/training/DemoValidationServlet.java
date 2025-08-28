package ch.iict.training;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/demo", "/demo/submit"})
public class DemoValidationServlet extends HttpServlet {

    @Override protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/demo.jsp").forward(req, resp);
    }

    @Override protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");
        String ageStr = req.getParameter("age");
        String website = req.getParameter("website");
        String phone = req.getParameter("phone");
        String dateStr = req.getParameter("date");

        Map<String,String> errors = new LinkedHashMap<>();
        Map<String,String> form = new LinkedHashMap<>();

        // bind form (pour réafficher les valeurs)
        form.put("email", email);
        form.put("website", website);
        form.put("phone", phone);
        form.put("age", ageStr);
        form.put("date", dateStr);

        // Validations simples
        if (email == null || email.isBlank()) errors.put("email", "Email obligatoire.");
        else if (!email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) errors.put("email", "Format email invalide.");

        if (password == null || password.length() < 8)
            errors.put("password", "Mot de passe: minimum 8 caractères.");
        if (confirm == null || !confirm.equals(password))
            errors.put("confirm", "La confirmation ne correspond pas.");

        if (ageStr != null && !ageStr.isBlank()) {
            try {
                int age = Integer.parseInt(ageStr);
                if (age < 18 || age > 120) errors.put("age", "Âge entre 18 et 120.");
            } catch (NumberFormatException e) {
                errors.put("age", "Âge invalide.");
            }
        }

        if (website != null && !website.isBlank()) {
            // check très simple (tu peux faire mieux avec un pattern)
            if (!website.matches("^(https?://).+")) errors.put("website", "URL doit commencer par http:// ou https://");
        }

        if (phone != null && !phone.isBlank()) {
            if (!phone.matches("^[0-9 +().-]{7,20}$")) errors.put("phone", "Téléphone invalide.");
        }

        if (dateStr != null && !dateStr.isBlank()) {
            try {
                LocalDate d = LocalDate.parse(dateStr);
                if (d.isBefore(LocalDate.now())) errors.put("date", "La date doit être aujourd’hui ou dans le futur.");
            } catch (Exception e) {
                errors.put("date", "Date invalide (yyyy-MM-dd).");
            }
        }

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.setAttribute("form", form);
            req.getRequestDispatcher("/demo.jsp").forward(req, resp);
            return;
        }

        // On met l'attribut "flash" dans la session de la requete pour qu'il survive au redirect
        req.getSession().setAttribute("flash", "Formulaire valide");
        resp.sendRedirect(req.getContextPath() + "/demo");
    }
}
