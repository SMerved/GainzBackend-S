package facades;

import dtos.AccountDTO;
import dtos.PostDTO;
import dtos.ExerciseDTO;

import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AccountFacadeTest {
    private static EntityManagerFactory emf;
    private static AccountFacade facade;

    private Account a1, a2, a3, a4, a5;
    private Comment c1, c2, c3, c4;
    private Event ev1, ev2, ev3, ev4;
    private Exercise ex1, ex2, ex3, ex4;
    private Post p1, p2, p3, p4;
    private Role r1;
    private Workout w1, w2, w3, w4;

    public AccountFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = AccountFacade.getAccountFacade(emf);
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
            em.createNamedQuery("Exercise.deleteAllRows").executeUpdate();
            em.createNamedQuery("Event.deleteAllRows").executeUpdate();
            em.createNamedQuery("Workout.deleteAllRows").executeUpdate();
            em.createNamedQuery("Post.deleteAllRows").executeUpdate();
            em.createNamedQuery("Role.deleteAllRows").executeUpdate();
            em.createNamedQuery("Account.deleteAllRows").executeUpdate();

            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Set<Comment> comments = new HashSet<>();
            Set<Account> likes = new HashSet<>();
            List<Post> posts = new ArrayList<>();

            r1 = new Role("user");
            a1 = (new Account("Bob", "123",  new Timestamp(System.currentTimeMillis()), "BobH@gmail.com"));
            a2 = (new Account("Hans", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com"));
            a3 = (new Account("Yvonne", "124",  new Timestamp(System.currentTimeMillis()), "Yvonne@gmail.com"));
            a4 = (new Account("John", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
            a5 = (new Account("Hans2", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
            p1=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            p2=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            p3=new Post(1L, "Post1", timestamp , "picture", null, new Workout(), comments, likes);
            p4=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            ex1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5));
            ex2 = (new Exercise("Arm Presses", "Default", 30, 0, 3));
            ex3 = (new Exercise("Dance Jumps", "DANCE", 60, 0, 0));


            a4.getPosts().add(p1);
            a4.getPosts().add(p2);
            a5.getPosts().add(p3);
            a5.getPosts().add(p4);
            p1.setAccount(a4);
            p2.setAccount(a4);
            p3.setAccount(a5);
            p4.setAccount(a5);
            a3.getFollowedAccounts().add(a4);
            a3.getFollowedAccounts().add(a5);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
        em.persist(a1);
        em.persist(a2);
        em.persist(a3);
        em.persist(a4);
        em.persist(a5);

            ex1.setAccount(a5);
            ex2.setAccount(a5);
            ex3.setAccount(a5);

            em.persist(ex1);
            em.persist(ex2);
            em.persist(ex3);

            em.persist(a2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testCreate() throws Exception {

        System.out.println("Testing create account from Facade");
        Account expected = new Account("Bob233", "123",  new Timestamp(System.currentTimeMillis()), "BobH@gmail.com");
        AccountDTO accountDTO = facade.create(new AccountDTO(expected));
        Account actual = new Account(accountDTO.getUsername(), accountDTO.getPassword(), new Timestamp(System.currentTimeMillis()), accountDTO.getEmail());


        assertEquals(expected, actual);


    }

    @Test
    public void readAccount() throws Exception {

        System.out.println("Testing get account");
        Account expected = a1;
        AccountDTO accountDTO = facade.getById(a1.getUsername());
        Account actual = new Account(accountDTO.getUsername(), accountDTO.getPassword(), new Timestamp(System.currentTimeMillis()), accountDTO.getEmail());
        System.out.println(actual);
        assertEquals(expected, actual);
    }
    @Test
    public void testGetFollowedPosts() throws Exception {

        assertEquals(4, facade.getAllFollowedPosts(a3.getUsername()).size());

    }
    @Test
    public void testUpdateLike() throws Exception {
        a1.getFollowedAccounts().add(a2);
        Account expectedAccount = a1;
        AccountDTO actualAccountDTO = facade.updateFollows(a1.getUsername(), a2.getUsername());
        Account actualAccount = new Account(actualAccountDTO);
        assertEquals(expectedAccount.getFollowedAccounts().size(), actualAccount.getFollowedAccounts().size());
    }

    @Test
    public void testGetAllExercises(){
        System.out.println("Testing");
        List<ExerciseDTO> actual = facade.getAllExercises(a5.getUsername());
        assert(actual.contains(new ExerciseDTO(ex1)));
        assert(actual.contains(new ExerciseDTO(ex2)));
        assert(actual.contains(new ExerciseDTO(ex3)));


    }

}