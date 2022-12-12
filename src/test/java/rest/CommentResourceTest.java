package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.CommentDTO;
import dtos.PostDTO;
import entities.*;
import facades.AccountFacade;
import facades.CommentFacade;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.processor.core.ColumnOrderDependent;
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

public class CommentResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static CommentFacade facade;

    private Account a1, a2, a3, a4;
    private Comment c1, c2, c3, c4;
    private Event ev1, ev2, ev3, ev4;
    private Exercise ex1, ex2, ex3, ex4;
    private Post p1, p2, p3, p4;
    private Role r1;
    private Workout w1, w2, w3, w4;

    public CommentResourceTest() {
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

        facade = CommentFacade.getCommentFacade(emf);

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

            a1 = (new Account("Bob", "111",  new Timestamp(System.currentTimeMillis()), "Bob@gmail.com"));
            a2 = (new Account("Hans", "222",  new Timestamp(System.currentTimeMillis()), "Hans@gmail.com"));
            a3 = (new Account("Yvonne", "333",  new Timestamp(System.currentTimeMillis()), "Yvonne@gmail.com"));

            c1 = new Comment("Omg, what a terrible post");

            p1=new Post(1L, "Post1", timestamp , "picture", new Event(), new Workout(), comments, likes);

            em.persist(a1);
            em.persist(a2);
            em.persist(a3);
            em.persist(p1);

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
        CommentDTO commentDTO = new CommentDTO(c1);
        String requestBody = GSON.toJson(commentDTO);
        System.out.println(c1);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/post/comment/" + p1.getId())
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("text", equalTo(c1.getText()));

    }

//    @Test
//    public void testGetAllComments() {
//        List<CommentDTO> commentDTOS;
//
//        commentDTOS = given()
//                .contentType("application/json")
//                .when()
//                .get("/hobby/allhobbies")
//                .then()
//                .extract().body().jsonPath().getList("", HobbyDTO.class);
//        List<Hobby> hobbiesActual = new ArrayList<>();
//
//        for (HobbyDTO hobbyDTO : hobbies) {
//            hobbiesActual.add(HobbyDTO.createEntity(hobbyDTO));
//        }
//
//        assertThat(hobbiesActual, containsInAnyOrder(h1, h2, h3, h4));
//    }

//    @Test
//    public void testGetById() {
//        given()
//                .contentType(ContentType.JSON)
////                .pathParam("id",h1.getId()).when()
//                .get("account/{id}", a1.getId())
//                .then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("id", equalTo(Integer.parseInt(a1.getId().toString())))
//                .body("username", equalTo(a1.getUsername()))
//                .body("password", equalTo(a1.getPassword()));
//    }
}
