package facades;

import dtos.AccountDTO;
import dtos.ExerciseDTO;
import entities.Account;
import entities.Exercise;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ExerciseFacadeTest {
    private static EntityManagerFactory emf;
    private static ExerciseFacade facade;

    private Account a1, a2;
    private Exercise e1, e2, e3;

    public ExerciseFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ExerciseFacade.getExerciseFacade(emf);
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
            a1 = (new Account("Yvonne", "124",  new Timestamp(System.currentTimeMillis()), "Yvonne@gmail.com"));

            e1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5));

            em.persist(a1);
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
//        System.out.println("Testing create exercise from Facade");
//        Exercise expected = e1;
//        ExerciseDTO exerciseDTO = facade.create(new ExerciseDTO(e1));
//        Exercise actual = new Exercise(exerciseDTO.getId(), exerciseDTO.getName(), exerciseDTO.getDescription(), exerciseDTO.getDuration(), exerciseDTO.getReps(), exerciseDTO.getWeight());
//
//        assertEquals(expected, actual);
//
//    }

    @Test
    public void testCreateWithAccount() throws Exception {

        System.out.println("Testing create exercise with account from Facade");
        Exercise expected = new Exercise("Arm Presses", "You lay down and press the weight from hand", 45, 0, 5, a1);
        ExerciseDTO exerciseDTO = facade.create(new ExerciseDTO(expected));
        Exercise actual = new Exercise(exerciseDTO.getId(), exerciseDTO.getName(), exerciseDTO.getDescription(), exerciseDTO.getDuration(), exerciseDTO.getReps(), exerciseDTO.getWeight(), exerciseDTO.getAccount());

        assertEquals(expected, actual);

    }



}
