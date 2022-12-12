package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.EventDTO;
import dtos.PostDTO;
import entities.*;
import io.restassured.http.ContentType;
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

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class PostResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    private Post p1, p2, p3, p4;
    private Account a1, a2, a3, a4, a5;
    private Role r1;

    private static Event e1;

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
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Set<Comment> comments = new HashSet<Comment>();
        Set<Account> likes = new HashSet<>();
        List<Post> posts = new ArrayList<>();

        p1 = new Post(1L, "Post1", timestamp, "picture", null, null, comments, likes);
        p2 = new Post(2L, "Post2", timestamp, "picture", null, null, comments, likes);
        p3 = new Post(3L, "Post3", timestamp, "picture", null, null, comments, likes);
        p4 = new Post(4L, "PosterChild", timestamp, "picture", null, null, comments, likes);
        a1=new Account("username", "password",  timestamp, "email@gmail.com");
        a3 = (new Account("Jens", "abe",  new Timestamp(System.currentTimeMillis()), "Jens@gmail.com", posts));
        a4 = (new Account("John", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
        a5 = (new Account("Hans", "abe",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com", posts));
        e1 = new Event("Geelsmark%209", "2840", "Copenhagen", "description", "date");

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
            em.persist(a3);
            em.persist(a4);
            em.persist(a5);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @Test
    public void testServerIsUp() {
        given().when().get("/xxx").then().statusCode(200);
    }


    @Test
    public void testGetAllPosts() throws Exception {
        List<PostDTO> postDTOS;

        postDTOS = given()
                .contentType("application/json")
                .when()
                .get("/feed")
                .then()
                .extract().body().jsonPath().getList("", PostDTO.class);

        assertThat(postDTOS, containsInAnyOrder(new PostDTO(p1), new PostDTO(p2), new PostDTO(p3), new PostDTO(p4)));


    }

    @Test
    public void testGetById() {
        given()
                .contentType(ContentType.JSON)
//                .pathParam("id",h1.getId()).when()
                .get("feed/post/{id}", p1.getId())
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK_200.getStatusCode())
                .body("id", equalTo(Integer.parseInt(p1.getId().toString())))
                .body("title", equalTo(p1.getTitle()))
                .body("picture", equalTo(p1.getPicture()));
    }

    @Test
    public void testUpdateLikes() {
        p1.getAccountsLiked().add(a1);
        PostDTO postDTO = new PostDTO(p1);
        String requestBody = GSON.toJson(postDTO.getId());
        PostDTO updatedPostDTO;

        updatedPostDTO = given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("feed/like/" + a1.getUsername())
                .then()
                .extract().body().jsonPath().getObject("", PostDTO.class);
        assertEquals(updatedPostDTO.getAccountsLiked().size(), postDTO.getAccountsLiked().size());
    }

    @Test
    public void testGetAllFollowedPosts() throws Exception {
        List<PostDTO> postDTOS;

        postDTOS = given()
                .contentType("application/json")
                .when()
                .get("/feed/" + a3.getUsername())
                .then()
                .extract().body().jsonPath().getList("", PostDTO.class);

        assertThat(postDTOS, containsInAnyOrder(new PostDTO(p1), new PostDTO(p2), new PostDTO(p3), new PostDTO(p4)));
    }
    @Test
    public void testUpdateFollows() {
        a1.getFollowedAccounts().add(a3);
        AccountDTO accountDTO = new AccountDTO(a1);
        String requestBody = GSON.toJson(accountDTO.getUsername());
        AccountDTO updatedAccountDTO;

        updatedAccountDTO = given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .put("feed/follow/" + a3.getUsername())
                .then()
                .extract().body().jsonPath().getObject("", AccountDTO.class);
        assertEquals(updatedAccountDTO.getFollowedAccounts().size(), accountDTO.getFollowedAccounts().size());
    }

    @Test
    public void testMakePostWithoutWorkoutOrExercise() {


        PostDTO postDTO = new PostDTO(p4);
        String requestBody = GSON.toJson(postDTO);
        PostDTO updatedPostDTO;

        updatedPostDTO = given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .post("feed/")
                .then()
                .extract().body().jsonPath().getObject("", PostDTO.class);
        System.out.println(updatedPostDTO);

        assertEquals(p4, new Post(updatedPostDTO));

    }

    @Test
    public void testMakePostWithEvent() {

        p4.setEvent(e1);
        PostDTO postDTO = new PostDTO(p4);
        String requestBody = GSON.toJson(postDTO);
        PostDTO updatedPostDTO;

        updatedPostDTO = given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .post("feed/")
                .then()
                .extract().body().jsonPath().getObject("", PostDTO.class);
        System.out.println(updatedPostDTO);

        assertEquals(p4, new Post(updatedPostDTO));

    }

    //Doesnt work TODO: make this work lol
    public void testMakePostWithEvent1() {


        Event e2 = new Event("Santa's home", "000000", "", "", "");
        p4.setEvent(e2);
        PostDTO postDTO = new PostDTO(p4);
        String requestBody = GSON.toJson(postDTO);
        PostDTO updatedPostDTO;

        given()
                .header("Content-type", ContentType.JSON)
                .body(requestBody)
                .when()
                .post("feed/")
                .then()
                .assertThat()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR_500.getStatusCode())
                .body("code", equalTo(500))
                .body("message", equalTo("Internal Server Error"));



    }

}

