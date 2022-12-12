package facades;

import dtos.AccountDTO;
import dtos.ExerciseDTO;
import entities.Account;
import entities.Exercise;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityNotFoundException;
import java.sql.Timestamp;

public class ExerciseFacade {
    private static ExerciseFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private ExerciseFacade() {}


    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static ExerciseFacade getExerciseFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ExerciseFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ExerciseDTO create(ExerciseDTO exerciseDTO) {

        Exercise exercise = new Exercise(exerciseDTO.getName(), exerciseDTO.getDescription(), exerciseDTO.getDuration(), exerciseDTO.getReps(), exerciseDTO.getWeight(), exerciseDTO.getAccount());

        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(exercise);
            em.getTransaction().commit();
        } finally {
            em.close();
        }

        return new ExerciseDTO(exercise);
    }

    public ExerciseDTO getByExerciseId(Long id) throws EntityNotFoundException {
        EntityManager em = getEntityManager();
        Exercise e = em.find(Exercise.class, id);
        if (e == null)
            throw new EntityNotFoundException("The Person entity with ID: " + id + " Was not found");
        return new ExerciseDTO(e);
    }



}
