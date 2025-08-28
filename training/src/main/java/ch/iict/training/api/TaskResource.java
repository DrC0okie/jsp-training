package ch.iict.training.api;

import ch.iict.training.dto.TaskIn;
import ch.iict.training.dto.TaskOut;
import ch.iict.training.entities.Task;
import ch.iict.training.repositories.TaskRepository;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.net.URI;
import java.util.List;

@Path("/v1/tasks")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TaskResource {

    @Context
    HttpServletRequest request; // pour récupérer l'EntityManager mis par le Filter

    private EntityManager em() {
        return (EntityManager) request.getAttribute("em");
    }

    @GET
    public Response list(@QueryParam("done") Boolean done) {
        var repo = new TaskRepository(em());
        List<Task> all = repo.findAll();
        var stream = all.stream();
        if (done != null) stream = stream.filter(t -> t.isDone() == done);
        List<TaskOut> out = stream.map(TaskMapper::toOut).toList();
        return Response.ok(out).build();
    }

    @POST
    public Response create(@Valid TaskIn in, @Context UriInfo uriInfo) {
        var repo = new TaskRepository(em());

        // TODO ajouter règles métier (ex: dueDate dans le futur)
        Task t = TaskMapper.toNewEntity(in);
        repo.save(t);

        URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(t.getId())).build();
        return Response.created(location).entity(TaskMapper.toOut(t)).build();
    }

    @GET
    @Path("{id}")
    public Response getOne(@PathParam("id") Long id) {
        var repo = new TaskRepository(em());
        Task t = repo.findById(id);
        if (t == null) throw new NotFoundException("Task " + id + " not found");
        return Response.ok(TaskMapper.toOut(t)).build();
    }

    @PUT
    @Path("{id}")
    public Response put(@PathParam("id") Long id, @Valid TaskIn in) {
        var repo = new TaskRepository(em());
        Task t = repo.findById(id);
        if (t == null) throw new NotFoundException("Task " + id + " not found");
        t.setTitle(in.getTitle());
        t.setDescription(in.getDescription());
        t.setDueDate(in.getDueDate());
        t.setDone(in.getDone() != null ? in.getDone() : false);

        repo.save(t);
        return Response.ok(TaskMapper.toOut(t)).build();
    }

    @DELETE
    @Path("{id}")
    public Response delete(@PathParam("id") Long id) {
        var repo = new TaskRepository(em());
        Task t = repo.findById(id);
        if (t == null) throw new NotFoundException("Task " + id + " not found");
        repo.delete(id);
        return Response.noContent().build(); // 204
    }

    @PATCH
    @Path("{id}/toggle")
    public Response toggle(@PathParam("id") Long id) {
        var repo = new TaskRepository(em());
        Task t = repo.findById(id);
        if (t == null) throw new NotFoundException("Task " + id + " not found");
        t.setDone(!t.isDone());
        repo.save(t);
        return Response.ok(TaskMapper.toOut(t)).build();
    }
}
