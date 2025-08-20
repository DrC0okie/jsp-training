<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>Todos</title>
    <link rel="stylesheet" href="<c:url value='/static/app.css'/>"/>
</head>
<body>
<div class="container">

    <header>
        <h1>Mes tâches</h1>
    </header>

    <!-- Formulaire d'ajout -->
    <form class="form-add" action="<c:url value='/add'/>" method="post">
        <input name="title" type="text" placeholder="Titre" required/>
        <input name="description" type="text" placeholder="Description (optionnelle)"/>
        <input name="dueDate" type="date" />
        <div class="actions-row">
            <button class="btn btn-primary" type="submit">Ajouter</button>
        </div>
    </form>

    <!-- Formulaire d'édition (affiché seulement si /edit) -->
    <c:if test="${not empty editingTask}">
        <form class="form-edit" action="<c:url value='/update'/>" method="post">
            <input type="hidden" name="id" value="${editingTask.id}"/>
            <input name="title" type="text" value="${editingTask.title}" placeholder="Titre" required/>
            <input name="description" type="text" value="${editingTask.description}" placeholder="Description"/>
            <input name="dueDate" type="date" value="${editingTask.dueDate}"/>
            <label class="checkbox">
                <input type="checkbox" name="done" <c:if test="${editingTask.done}">checked</c:if> />
                Terminé
            </label>
            <div class="actions-row">
                <button class="btn btn-primary" type="submit">Enregistrer</button>
                <a class="btn btn-ghost" href="<c:url value='/tasks'/>">Annuler</a>
            </div>
        </form>
    </c:if>

    <!-- Deux colonnes : À faire / Terminé -->
    <div class="board">
        <!-- À faire -->
        <section class="column" data-done="false">
            <h2>À faire</h2>
            <ul class="task-list">
                <c:forEach var="t" items="${tasks}">
                    <c:if test="${not t.done}">
                        <li class="task ${t.urgencyClass}" draggable="true" data-id="${t.id}">
                            <form class="inline" action="<c:url value='/toggle'/>" method="post" title="Marquer terminé">
                                <input type="hidden" name="id" value="${t.id}"/>
                                <button class="btn btn-icon" type="submit">✔</button>
                            </form>

                            <div class="body">
                                <div class="title"><c:out value="${t.title}"/></div>
                                <c:if test="${not empty t.description}">
                                    <div class="desc"><c:out value="${t.description}"/></div>
                                </c:if>
                                <div class="meta">
                                    <c:if test="${not empty t.dueDate}">
                                        <span class="badge">Échéance : <c:out value="${t.dueDate}"/></span>
                                    </c:if>
                                </div>
                            </div>

                            <div class="actions">
                                <a class="btn btn-ghost" href="<c:url value='/edit'><c:param name='id' value='${t.id}'/></c:url>">Éditer</a>
                                <form class="inline" action="<c:url value='/delete'/>" method="post">
                                    <input type="hidden" name="id" value="${t.id}"/>
                                    <button class="btn btn-danger" type="submit">Supprimer</button>
                                </form>
                            </div>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </section>

        <!-- Terminé -->
        <section class="column" data-done="true">
            <h2>Terminé</h2>
            <ul class="task-list">
                <c:forEach var="t" items="${tasks}">
                    <c:if test="${t.done}">
                        <li class="task is-done" draggable="true" data-id="${t.id}">
                            <form class="inline" action="<c:url value='/toggle'/>" method="post" title="Renvoyer à faire">
                                <input type="hidden" name="id" value="${t.id}"/>
                                <button class="btn btn-icon" type="submit">↩</button>
                            </form>

                            <div class="body">
                                <div class="title"><c:out value="${t.title}"/></div>
                                <c:if test="${not empty t.description}">
                                    <div class="desc"><c:out value="${t.description}"/></div>
                                </c:if>
                                <div class="meta">
                                    <c:if test="${not empty t.dueDate}">
                                        <span class="badge">Échéance : <c:out value="${t.dueDate}"/></span>
                                    </c:if>
                                </div>
                            </div>

                            <div class="actions">
                                <a class="btn btn-ghost" href="<c:url value='/edit'><c:param name='id' value='${t.id}'/></c:url>">Éditer</a>
                                <form class="inline" action="<c:url value='/delete'/>" method="post">
                                    <input type="hidden" name="id" value="${t.id}"/>
                                    <button class="btn btn-danger" type="submit">Supprimer</button>
                                </form>
                            </div>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </section>
    </div>

</div>
</body>
</html>