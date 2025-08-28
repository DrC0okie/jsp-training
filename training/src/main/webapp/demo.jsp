<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Démo Validation</title>
    <link rel="stylesheet" href="<c:url value='/static/app.css'/>"/>
    <style>
        .form-card {
            max-width: 680px;
            margin: 2rem auto;
            background: #fff;
            border: 1px solid #e5e7eb;
            border-radius: .75rem;
            padding: 1rem
        }

        .row {
            display: flex;
            gap: .75rem;
            flex-wrap: wrap
        }

        .row > div {
            flex: 1;
            min-width: 240px
        }

        .field-error {
            color: #b91c1c;
            font-size: .9rem;
            margin-top: .25rem
        }

        .input-error {
            border-color: #fecaca;
            background: #fff7f7
        }

        .flash {
            background: #ecfdf5;
            border: 1px solid #86efac;
            padding: .6rem .8rem;
            border-radius: .5rem;
            color: #166534;
            margin: 1rem auto;
            max-width: 680px
        }
    </style>
    <script>
        // petite verif live pour password confirm
        document.addEventListener('DOMContentLoaded', () => {
            const pwd = document.querySelector('input[name=password]');
            const cfm = document.querySelector('input[name=confirm]');
            if (pwd && cfm) {
                const check = () => {
                    if (cfm.value && pwd.value !== cfm.value) {
                        cfm.setCustomValidity("La confirmation ne correspond pas");
                    } else {
                        cfm.setCustomValidity("");
                    }
                };
                pwd.addEventListener('input', check);
                cfm.addEventListener('input', check);
            }
        });
    </script>
</head>
<body>
<c:if test="${not empty sessionScope.flash}">
    <div class="flash">${sessionScope.flash}</div>
    <c:remove var="flash" scope="session"/>
</c:if>

<c:set var="errors" value="${requestScope.errors}"/>
<c:set var="form" value="${requestScope.form}"/>

<div class="form-card">
    <h2>Démo Validation (front + back)</h2>
    <form action="<c:url value='/demo/submit'/>" method="post" novalidate>
        <div class="row">
            <div>
                <label>Email</label>
                <input type="email" name="email" required
                       value="${form.email}"
                       class="${errors['email'] != null ? 'input-error' : ''}">
                <c:if test="${errors['email'] != null}">
                    <div class="field-error">${errors['email']}</div>
                </c:if>
            </div>
            <div>
                <label>Âge</label>
                <input type="number" name="age" min="18" max="120"
                       value="${form.age}"
                       class="${errors['age'] != null ? 'input-error' : ''}">
                <c:if test="${errors['age'] != null}">
                    <div class="field-error">${errors['age']}</div>
                </c:if>
            </div>
        </div>

        <div class="row">
            <div>
                <label>Mot de passe</label>
                <input type="password" name="password" required minlength="8"
                       class="${errors['password'] != null ? 'input-error' : ''}">
                <c:if test="${errors['password'] != null}">
                    <div class="field-error">${errors['password']}</div>
                </c:if>
            </div>
            <div>
                <label>Confirmation</label>
                <input type="password" name="confirm" required
                       class="${errors['confirm'] != null ? 'input-error' : ''}">
                <c:if test="${errors['confirm'] != null}">
                    <div class="field-error">${errors['confirm']}</div>
                </c:if>
            </div>
        </div>

        <div class="row">
            <div>
                <label>Site web</label>
                <input type="url" name="website" placeholder="https://exemple.com"
                       value="${form.website}"
                       class="${errors['website'] != null ? 'input-error' : ''}">
                <c:if test="${errors['website'] != null}">
                    <div class="field-error">${errors['website']}</div>
                </c:if>
            </div>
            <div>
                <label>Téléphone</label>
                <input type="tel" name="phone" placeholder="+41 79 123 45 67" pattern="[0-9 +().-]{7,20}"
                       value="${form.phone}"
                       class="${errors['phone'] != null ? 'input-error' : ''}">
                <c:if test="${errors['phone'] != null}">
                    <div class="field-error">${errors['phone']}</div>
                </c:if>
            </div>
        </div>

        <div class="row">
            <div>
                <label>Date (futur)</label>
                <input type="date" name="date"
                       value="${form.date}"
                       class="${errors['date'] != null ? 'input-error' : ''}">
                <c:if test="${errors['date'] != null}">
                    <div class="field-error">${errors['date']}</div>
                </c:if>
            </div>
        </div>

        <div style="margin-top:1rem; display:flex; gap:.5rem;">
            <button class="btn btn-primary" type="submit">Valider</button>
            <a class="btn btn-ghost" href="<c:url value='/demo'/>">Réinitialiser</a>
        </div>
    </form>
</div>
</body>
</html>
