package facades;

import dtos.AccountDTO;
import dtos.CommentDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CommentFacadeTest {
    private static EntityManagerFactory emf;
    private static CommentFacade facade;

    private Account a1, a2, a3, a4;
    private Comment c1, c2, c3, c4;
    private Event ev1, ev2, ev3, ev4;
    private Exercise ex1, ex2, ex3, ex4;
    private Post p1, p2, p3, p4;
    private Role r1;
    private Workout w1, w2, w3, w4;

    public CommentFacadeTest() {
    }
    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = CommentFacade.getCommentFacade(emf);
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
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            Set<Comment> comments = new HashSet<>();
            Set<Account> likes = new HashSet<>();
            r1 = new Role("user");

            p1=new Post(1L, "Post1", timestamp , "picture", new Event(), new Workout(), comments, likes);

            a1 = (new Account("Bob", "123", new Timestamp(System.currentTimeMillis()), "BobH@gmail.com"));
            a2 = (new Account("Hans", "abe", new Timestamp(System.currentTimeMillis()), "Hans@gmail.com"));


            c1 = new Comment(1, "What a cool post dude!");
            c2 = new Comment(2, "Im a comment! Yay, me!");

            em.persist(a1);
            em.persist(p1);
            em.persist(c1);

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

        System.out.println("Testing create comment from Facade");
        Comment expected = c2;
        CommentDTO commentDTO = facade.create(new CommentDTO(c2), p1.getId());

        Comment actual = new Comment(commentDTO.getId(), commentDTO.getText());

        assertEquals(expected, actual);

    }

}