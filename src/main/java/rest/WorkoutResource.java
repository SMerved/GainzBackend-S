package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.ExerciseDTO;
import dtos.WorkoutDTO;
import facades.PostFacade;
import facades.WorkoutFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("workout")
public class WorkoutResource {
    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();

    private static final WorkoutFacade FACADE =  WorkoutFacade.getWorkoutFacade(EMF);
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @POST
    @Produces({MediaType.APPLICATION_JSON})
    @Consumes({MediaType.APPLICATION_JSON})
    public Response createExercise(String content) {
        WorkoutDTO workoutDTO = GSON.fromJson(content, WorkoutDTO.class);
        WorkoutDTO newWorkoutDTO = FACADE.create(workoutDTO);
        return Response.ok().entity(GSON.toJson(newWorkoutDTO)).build();
    }

}
