package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.CommentDTO;
import facades.CommentFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("post/comment")
public class CommentResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final CommentFacade FACADE = CommentFacade.getCommentFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @Path("/{id}")
    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createComment(@PathParam("id") Long id, String content) {
        System.out.println("id: " + id);
        System.out.println("content: " + content);
        CommentDTO commentDTO = GSON.fromJson(content, CommentDTO.class);
        System.out.println(commentDTO);
        CommentDTO newCommentDTO = FACADE.create(commentDTO, id);
        System.out.println(newCommentDTO);
        return Response.ok().entity(GSON.toJson(newCommentDTO)).build();
    }

//    @GET
//    @Path("/{id}")
//    @Produces({MediaType.APPLICATION_JSON})
//    public Response getById(@PathParam("id") int id) throws EntityNotFoundException {
//        System.out.println("id: " + id);
//        AccountDTO accountDTO = FACADE.getById(id);
//        System.out.println("accountDTO: " + accountDTO);
//        return Response.ok().entity(GSON.toJson(accountDTO)).build();
//
//    }

}