package ch.iict.training.repositories;

import ch.iict.training.entities.Task;
import jakarta.persistence.EntityManager;

import java.util.List;

public class TaskRepository {

    private final EntityManager em;

    public TaskRepository(EntityManager em) { this.em = em; }

    public List<Task> findAll() {
        return em.createQuery("select t from Task t order by t.id", Task.class).getResultList();
    }

    public Task findById(Long id) {
        return em.find(Task.class, id);
    }

    public Task save(Task t) {
        if (t.getId() == null) em.persist(t); else t = em.merge(t);
        return t;
    }

    public void delete(Long id) {
        Task ref = em.find(Task.class, id);
        if (ref != null) em.remove(ref);
    }
}
