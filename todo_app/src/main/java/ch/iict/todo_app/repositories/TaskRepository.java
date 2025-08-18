package ch.iict.todo_app.repositories;

import ch.iict.todo_app.entities.Task;
import com.sun.tools.javac.comp.Todo;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TaskRepository {

    private final EntityManager em;

    public TaskRepository(EntityManager em) { this.em = em; }

    public List<Task> findAll() {
        return em.createQuery("select t from Task t order by t.id", Task.class).getResultList();
    }

    public Task save(Task t) {
        if (t.getId() == null) em.persist(t); else t = em.merge(t);
        return t;
    }

    public void delete(Long id) {
        Todo ref = em.find(Todo.class, id);
        if (ref != null) em.remove(ref);
    }
}