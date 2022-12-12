package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.ExerciseDTO;
import dtos.PostDTO;
import entities.*;
import facades.AccountFacade;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;
import io.restassured.RestAssured;

import static io.restassured.RestAssured.given;

import io.restassured.parsing.Parser;

import java.net.URI;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static io.restassured.RestAssured.post;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class AccountResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static AccountFacade facade;

    private Account a1, a2, a3, a4, a5;
    private Comment c1, c2, c3, c4;
    private Event ev1, ev2, ev3, ev4;
    private Exercise ex1, ex2, ex3, ex4;
    private Post p1, p2, p3, p4, p5, p6, p7, p8, p9;
    private Role r1;
    private Workout w1, w2, w3, w4;

    public AccountResourceTest() {
    }

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;

        facade = AccountFacade.getAccountFacade(emf);

    }


    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
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
            a1 = (new Account("Bob", "111",  new Timestamp(System.currentTimeMillis()), "Bob@gmail.com"));
            a2 = (new Account("Hans1", "222",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com"));
            a3 = (new Account("Jens", "abe",  new Timestamp(System.currentTimeMillis()), "Jens@gmail.com", posts));
            a4 = (new Account("John", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
            a5 = (new Account("Hans", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
            p1 = new Post(1L, "p1", timestamp, "picture", new Event(), new Workout(), comments, likes);
            p2 = new Post(2L, "p2", timestamp, "picture", new Event(), new Workout(), comments, likes);
            p3 = new Post(3L, "p3", timestamp, "picture", new Event(), new Workout(), comments, likes);
            p4 = new Post(4L, "p4", timestamp, "picture", new Event(), new Workout(), comments, likes);
            p5 = new Post(5L, "p5", timestamp, "picture", new Event(), new Workout(), comments, likes);
            p6=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            p7=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            p8=new Post(1L, "Post1", timestamp , "picture", null, new Workout(), comments, likes);
            p9=new Post(1L, "Post1", timestamp , "picture", null, null, comments, likes);
            ex1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5));
            ex2 = (new Exercise("Arm Presses", "Default", 30, 0, 3));
            ex3 = (new Exercise("Dance Jumps", "DANCE", 60, 0, 0));

            a4.getPosts().add(p6);
            a4.getPosts().add(p7);
            a5.getPosts().add(p8);
            a5.getPosts().add(p9);
            p6.setAccount(a4);
            p7.setAccount(a4);
            p8.setAccount(a5);
            p9.setAccount(a5);
            a3.getFollowedAccounts().add(a4);
            a3.getFollowedAccounts().add(a5);

            p1.setAccount(a1);
            p2.setAccount(a2);
            p3.setAccount(a1);
            p4.setAccount(a1);
            p5.setAccount(a1);

            ex1.setAccount(a2);
            ex2.setAccount(a2);
            ex3.setAccount(a2);

            //Role not persisted
            em.persist(p6);
            em.persist(p7);
            em.persist(p8);
            em.persist(p9);
            em.persist(a3);
            em.persist(a4);
            em.persist(a5);

            em.persist(a1);
            em.persist(a2);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.persist(p4);
            em.persist(p5);


            em.persist(ex1);
            em.persist(ex2);
            em.persist(ex3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    @Test
    public void testCreate() throws Exception {
        Account account = new Account("Bob45", "111", new Timestamp(System.currentTimeMillis()), "Bob@gmail.com");

        AccountDTO accountDTO = new AccountDTO(account);
        String requestBody = GSON.toJson(accountDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/account")
                .then()
                .assertThat()
                .statusCode(200)
                .body("username", equalTo("Bob45"))
                .body("password", equalTo("111"))
                .body("email", equalTo("Bob@gmail.com"));

    }

    @Test
    public void testGetById() {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id",h1.getId()).when()
                .get("account/{id}", a1.getUsername())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("username", equalTo(a1.getUsername()))
                .body("password", equalTo(a1.getPassword()));
    }

    @Test
    public void testPostsFromAccountId() {
        List<PostDTO> postListDTO;

        postListDTO = given()
                .contentType("application/json")
                .when()
                .get("/account/posts/{id}", a1.getUsername())
                .then()
                .extract().body().jsonPath().getList("", PostDTO.class);

        assertThat(postListDTO, containsInAnyOrder(new PostDTO(p1), new PostDTO(p3), new PostDTO(p4), new PostDTO(p5)));
    }
    
    @Test
    public void testGetAllExercises() {
        List<ExerciseDTO> exerciselistDTO;

        exerciselistDTO = given()
                .contentType("application/json")
                .when()
                .get("/account/exercises/{id}", a2.getUsername())
                .then()
                .extract().body().jsonPath().getList("", ExerciseDTO.class);


        System.out.println(exerciselistDTO.toString());
        assertThat(exerciselistDTO, containsInAnyOrder(new ExerciseDTO(ex1), new ExerciseDTO(ex2), new ExerciseDTO(ex3)));

    }

}
