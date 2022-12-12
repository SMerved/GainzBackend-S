package facades;

import dtos.AccountDTO;
import dtos.ExerciseDTO;
import dtos.WorkoutDTO;
import entities.Account;
import entities.Exercise;
import entities.Workout;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.util.LinkedHashSet;
import java.util.Set;

public class WorkoutFacade {

    private static WorkoutFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private WorkoutFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static WorkoutFacade getWorkoutFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new WorkoutFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public WorkoutDTO create(WorkoutDTO workoutDTO) {
        Set<Exercise> exerciseSets = new LinkedHashSet<>();
        for(ExerciseDTO e: workoutDTO.getExercises()) {
            exerciseSets.add(new Exercise(e));
        }

        Workout workout = new Workout(workoutDTO.getDescription(), workoutDTO.getBreakTime(), exerciseSets);

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(workout);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new WorkoutDTO(workout);
    }


}
