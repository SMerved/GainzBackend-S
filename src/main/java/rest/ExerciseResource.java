package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.ExerciseDTO;
import entities.Exercise;
import facades.AccountFacade;
import facades.ExerciseFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("exercise")
public class ExerciseResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final ExerciseFacade FACADE = ExerciseFacade.getExerciseFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createExercise(String content) {
        ExerciseDTO exerciseDTO = GSON.fromJson(content, ExerciseDTO.class);
        ExerciseDTO newExerciseDTO = FACADE.create(exerciseDTO);
        return Response.ok().entity(GSON.toJson(newExerciseDTO)).build();
    }


}
