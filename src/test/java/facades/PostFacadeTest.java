package facades;

import dtos.PostDTO;
import entities.*;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PostFacadeTest {

    private static EntityManagerFactory emf;
    private static PostFacade facade;
    private static Post testPost1, testPost2, testPost3;

    private Role r1;
    private static Event e1;
    private static Post p1, p2, p3, p4, p5;
    private static Account a1, a2, a3;

    public PostFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PostFacade.getPostFacade(emf);
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

            r1 = new Role("user");
            testPost1 = new Post("Post1","pic1", null,null,new HashSet<>());
            testPost2 = new Post("Post2","pic2", null,null,new HashSet<>());
            e1 = new Event("Geelsmark%209", "2840", "Copenhagen", "We invite all seniors to our senior workout, were we will be fingerdinkering for 2-3 hours", "14. December 12:30");
            a1 = new Account("username1", "password", timestamp, "email@gmail.com");
            a2 = new Account("username2", "password",  timestamp, "email@gmail.com");
            a3 = new Account("username3", "password", timestamp, "email@gmail.com");
            p1 = new Post(1L, "p1", timestamp, "picture", null, null, comments, likes);
            p2 = new Post(2L, "p2", timestamp, "picture", null, null, comments, likes);
            p3 = new Post(3L, "p3", timestamp, "picture", null, null, comments, likes);
            p4 = new Post(4L, "p4", timestamp, "picture", null, null, comments, likes);
            p5 = new Post(5L, "p5", timestamp, "picture", null, null, comments, likes);
            p1.setAccount(a1);
            p2.setAccount(a2);
            p3.setAccount(a1);
            p4.setAccount(a1);
            p5.setAccount(a1);

            em.persist(testPost1);
            em.persist(testPost2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);
            em.persist(a1);
            em.persist(a2);
            em.persist(a3);

            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method
    @Test
    public void testGetAll() throws Exception {

        assertEquals(7, PostFacade.getAllPosts().size(), "Expects two rows in the database");

    }

    @Test
    public void createPostWithoutEventOrWorkout() throws Exception {
        Post expectedPost = new Post("Amazing test Post", "Some pic", null, null, new HashSet<>() );
        expectedPost.setAccount(a2);

        System.out.println(expectedPost);
        PostDTO actualDTO = facade.createPost(new PostDTO(expectedPost));
        Post actualPost = new Post(actualDTO);
        assertEquals(expectedPost, actualPost);
    }

    @Test
    public void getPostsFromAccountId() throws Exception {

        System.out.println("Testing List of Posts from Account");
        List<PostDTO> actual = facade.getPostsFromAccountId(a1.getUsername());
        assert (actual.contains(new PostDTO(p1)));
        assert (actual.contains(new PostDTO(p3)));
        assert (actual.contains(new PostDTO(p4)));
        assert (actual.contains(new PostDTO(p5)));

    }


    @Test
    public void testUpdateLike() throws Exception {
        p1.getAccountsLiked().add(a1);
        Post expectedPost = p1;
        PostDTO actualPostDTO = facade.updateLikes(a1.getUsername(), p1.getId());
        Post actualPost = new Post(actualPostDTO);
        assertEquals(expectedPost.getAccountsLiked().size(), actualPost.getAccountsLiked().size());
    }

    @Test
    public void makePostWithEvent() throws Exception{
        testPost3 = new Post("Best test workout", "some pic", e1,null,new HashSet<>());
        PostDTO postDTO = new PostDTO(testPost3);
        if (postDTO.getEvent()!=null)
            postDTO = facade.createEvenDTO(postDTO);
        PostDTO newPostDTO = facade.createPost(postDTO);

        assertEquals(testPost3,new Post(newPostDTO) );


    }


    @Test
    public void makePostWithNotWorkingEvent() throws Exception{
        Event e2 = new Event("kekw", "lalandia", "", "", "");
        testPost3 = new Post("Best test workout", "some pic", e2,null,new HashSet<>());
        final PostDTO postDTO = new PostDTO(testPost3);

        assertThrows(Exception.class, () -> facade.createEvenDTO(postDTO));

    }

    @Test
    public void getPostById() throws Exception {

        System.out.println("Testing get Post By Id");
        Post expected = p3;
        PostDTO postDTO = facade.getPostDTOById(p3.getId());
        Post actual = new Post(postDTO);
//        Post actual = new Post(postDTO.getId(), postDTO.getTitle(), postDTO.getTimestamp(), postDTO.getPicture(), postDTO.getEvent(), postDTO.getEvent(), postDTO.getComments(), postDTO.getAccountsLiked());

        assertEquals(expected, actual);

    }




}
