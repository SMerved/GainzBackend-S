package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.EventDTO;
import dtos.PostDTO;
import entities.Post;
import facades.AccountFacade;
import facades.PostFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

//Todo Remove or change relevant parts before ACTUAL use
@Path("feed")
public class PostResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final PostFacade FACADE =  PostFacade.getPostFacade(EMF);

    private static final AccountFacade accountFacade = AccountFacade.getAccountFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @GET
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllPosts() {
        List<PostDTO> postDTOs= FACADE.getAllPosts();
        System.out.println("IMPORTANT - postDTOs: " + postDTOs);
        return Response.ok().entity(GSON.toJson(postDTOs)).build();
    }

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createPost(String content) throws Exception {
        PostDTO postDTO = GSON.fromJson(content, PostDTO.class);
        if (postDTO.getEvent()!=null)
           postDTO = FACADE.createEvenDTO(postDTO);
        PostDTO newPostDTO = FACADE.createPost(postDTO);
        return Response.ok().entity(GSON.toJson(newPostDTO)).build();
    }


    @PUT
    @Path("/like/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response likePost (@PathParam("id") String accountId, String content) {
        System.out.println("content:" + content);
        Long postId = GSON.fromJson(content, Long.class);
        PostDTO postDTO = FACADE.updateLikes(accountId, postId);
        System.out.println(postDTO);
        return Response.ok().entity(GSON.toJson(postDTO)).build();
    }


    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getAllFollowedPosts(@PathParam("id") String accountId) {
        List<PostDTO> postDTOs = accountFacade.getAllFollowedPosts(accountId);
        System.out.println("IMPORTANT - postDTOs: " + postDTOs);
        return Response.ok().entity(GSON.toJson(postDTOs)).build();
    }
    @PUT
    @Path("/follow/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response followAccount (@PathParam("id") String followId, String content) {
        System.out.println("content :" + content);
        String accountId = GSON.fromJson(content, String.class);
        AccountDTO accountDTO = accountFacade.updateFollows(accountId, followId);
        System.out.println(accountDTO);
        return Response.ok().entity(GSON.toJson(accountDTO)).build();
    }

    @GET
    @Path("/post/{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getById(@PathParam("id") long id) throws EntityNotFoundException {
        System.out.println("id: " + id);
        PostDTO postDTO = FACADE.getPostDTOById(id);
        System.out.println("accountDTO: " + postDTO);
        return Response.ok().entity(GSON.toJson(postDTO)).build();

    }

}
