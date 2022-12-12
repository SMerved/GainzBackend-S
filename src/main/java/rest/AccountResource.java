package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.PostDTO;
import dtos.ExerciseDTO;
import dtos.PostDTO;
import entities.Exercise;
import facades.AccountFacade;
import facades.PostFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("account")
public class AccountResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final AccountFacade FACADE = AccountFacade.getAccountFacade(EMF);
    private static final PostFacade postFacade = PostFacade.getPostFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public String demo() {
        return "{\"msg\":\"Hello World\"}";
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createAccount(String content) {
        AccountDTO accountDTO = GSON.fromJson(content, AccountDTO.class);
        AccountDTO newAccountDTO = FACADE.create(accountDTO);
        return Response.ok().entity(GSON.toJson(newAccountDTO)).build();
    }

    @GET
    @Path("/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") String id) throws EntityNotFoundException {
        System.out.println("id: " + id);
        AccountDTO accountDTO = FACADE.getById(id);
        System.out.println("accountDTO: " + accountDTO);
        return Response.ok().entity(GSON.toJson(accountDTO)).build();
    }

    @GET
    @Path("posts/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getPostsFromAccountId(@PathParam("id") String id) throws EntityNotFoundException {
        List<PostDTO> postDTOsFromAccount= postFacade.getPostsFromAccountId(id);
        System.out.println("IMPORTANT - postDTOs: " + postDTOsFromAccount);
        System.out.println("id: " + id);
        return Response.ok().entity(GSON.toJson(postDTOsFromAccount)).build();
    }

    @GET
    @Path("exercises/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllExercises(@PathParam("id") String id) {
        List<ExerciseDTO> exerciseDTOs= FACADE.getAllExercises(id);
        return Response.ok().entity(GSON.toJson(exerciseDTOs)).build();
    }



}