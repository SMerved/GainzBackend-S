package facades;

import dtos.AccountDTO;
import dtos.WorkoutDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WorkoutFacadeTest {

    private static EntityManagerFactory emf;
    private static WorkoutFacade facade;

    private Workout w1, w2, w3;
    private Exercise e1, e2, e3;
    private Set<Exercise> exerciseList = new LinkedHashSet<>();

    public WorkoutFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = WorkoutFacade.getWorkoutFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the code below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Comment.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("Exercise.deleteAllRows").executeUpdate();
            em.createNamedQuery("Event.deleteAllRows").executeUpdate();
            em.createNamedQuery("Workout.deleteAllRows").executeUpdate();
            em.createNamedQuery("Post.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("Account.deleteAllRows").executeUpdate();

            e1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5));
            e2 = (new Exercise("Arm Presses", "Default", 30, 0, 3));
            e3 = (new Exercise("Dance Jumps", "DANCE", 60, 0, 0));


            exerciseList.add(e1);
            exerciseList.add(e2);
            exerciseList.add(e3);

            w1 = new Workout("THIS IS AWESOME", 15, exerciseList);

            em.persist(e1);
            em.persist(e2);
            em.persist(e3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

//    @Test
//    public void testCreate() throws Exception {
//
//        System.out.println("Testing create workout from Facade");
//        Workout expected = w1;
//        WorkoutDTO workoutDTO = facade.create(new WorkoutDTO(w1));
//
//        Workout actual = new Workout(workoutDTO.getId(), workoutDTO.getDescription(), workoutDTO.getBreakTime());
//
//        assertEquals(expected, actual);
//
//
//    }
}
