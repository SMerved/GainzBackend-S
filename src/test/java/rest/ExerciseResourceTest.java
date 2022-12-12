package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dtos.AccountDTO;
import dtos.ExerciseDTO;
import entities.*;
import facades.AccountFacade;
import facades.ExerciseFacade;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.sql.Timestamp;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class ExerciseResourceTest {
    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();


    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;
    private static ExerciseFacade facade;

    private Account a1, a2;
    private Exercise e1, e2, e3;

    public ExerciseResourceTest() {
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

        facade = ExerciseFacade.getExerciseFacade(emf);

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

            a1 = (new Account("Yvonne", "124",  new Timestamp(System.currentTimeMillis()), "Yvonne@gmail.com"));
            e1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5));
            em.persist(a1);
            em.persist(e1);

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

//    @Test
//    public void testCreate() throws Exception {
//
//        Exercise exercise = new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5);
//
//        ExerciseDTO exerciseDTO = new ExerciseDTO(exercise);
//        String requestBody = GSON.toJson(exerciseDTO);
//
//        given()
//                .header("Content-type", ContentType.JSON)
//                .and()
//                .body(requestBody)
//                .when()
//                .post("/exercise")
//                .then()
//                .assertThat()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("name", equalTo("Leg Presses"))
//                .body("duration", equalTo(45));
//
//    }

    @Test
    public void testCreateWithAccount() throws Exception {

        Exercise exercise = new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5, a1);

        ExerciseDTO exerciseDTO = new ExerciseDTO(exercise);
        String requestBody = GSON.toJson(exerciseDTO);

        given()
                .header("Content-type", ContentType.JSON)
                .and()
                .body(requestBody)
                .when()
                .post("/exercise")
                .then()
                .assertThat()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Leg Presses"))
                .body("duration", equalTo(45));

    }

}
