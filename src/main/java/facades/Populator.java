/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package facades;

import dtos.PostDTO;
import dtos.RenameMeDTO;
import entities.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import utils.EMF_Creator;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author tha
 */
public class Populator {
    public static void populate() throws Exception {
        EntityManagerFactory emf = EMF_Creator.createEntityManagerFactory();
        EntityManager em = emf.createEntityManager();
        PostFacade postFacade = PostFacade.getPostFacade(emf);
        em.getTransaction().begin();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Set<Comment> comments = new HashSet<>();
        Set<Account> likes = new HashSet<>();
        String picture = "https://thumbs.dreamstime.com/b/blurred-fitness-gym-background-banner-fitness-exercise-co-blurred-fitness-gym-background-banner-fitness-exercise-118207083.jpg";
        Set<Exercise> exerciseList = new HashSet<>();
        Set<Exercise> exerciseList2 = new HashSet<>();

        Account a1 = new Account("HardcoreYvonne", "123", timestamp, "Yvonne@gmail.com");
        Account a2 = new Account("HajKat", "123", timestamp, "Yvonne@gmail.com");
        Account a3 = new Account("KatHaj", "123", timestamp, "Yvonne@gmail.com");
        Account a4 = new Account("MichaelsFingerDingers", "123", timestamp, "Yvonne@gmail.com");
        Account a5 = new Account("SwoleSoren", "123", timestamp, "Yvonne@gmail.com");
        Account a6 = new Account("KristoferKarambit", "123", timestamp, "Yvonne@gmail.com");
        Account a7 = new Account("Hanya", "123", timestamp, "Yvonne@gmail.com");
        Account a8 = new Account("Ivo", "123", timestamp, "Yvonne@gmail.com");
        Account a9 = new Account("SofiaSlagsmal", "123", timestamp, "Yvonne@gmail.com");

        Exercise e1 = (new Exercise("Leg Presses", "You lay down and press the weight from feet", 45, 0, 5, a7));
        Exercise e2 = (new Exercise("Arm Presses", "Default", 30, 0, 3, a7));
        Exercise e3 = (new Exercise("Dance Jumps", "DANCE", 60, 0, 0, a7));

        exerciseList.add(e1);
        exerciseList.add(e2);
        exerciseList.add(e3);

        Workout w1 = new Workout("THIS IS AWESOME", 15, exerciseList);
        Event ev1 = new Event("Geelsmark%209", "2840", "Copenhagen", "We invite all seniors to our senior workout, were we will be fingerdinkering for 2-3 hours", "14. December 12:30");

        Exercise e20 = new Exercise("FingerDinger", "You gotta finger the dinger", 30, 0, 0, a4);
        a4.addExercise(e20);
        Exercise e21 = new Exercise("Arm press", "Press your arms", 30, 0, 10, a2);
        a2.addExercise(e21);
        Exercise e22 = new Exercise("Leg press", "Press your leg", 30, 0, 5, a2);
        a2.addExercise(e22);
        exerciseList2.add(e21);
        exerciseList2.add(e22);
        Workout w2 = new Workout("THIS IS NOT AWESOME", 15, exerciseList2);

        Exercise e4 = new Exercise("Finger press", "Press your finger", 30, 0, 100, a4);
        a4.addExercise(e4);
        Exercise e5 = new Exercise("Burpees", "Jump", 45, 0, 5, a2);
        a2.addExercise(e5);
        Exercise e6 = new Exercise("Mountain Climbers", "Climb", 60, 0, 0, a1);
        a1.addExercise(e6);
        Exercise e7 = new Exercise("Lunges", "lunge", 30, 0, 0, a1);
        a1.addExercise(e7);
        Exercise e8 = new Exercise("Jump", "Jump", 30, 0, 0, a1);
        a1.addExercise(e8);
        Exercise e9 = new Exercise("Dance", "dance your toes away!", 75, 0, 0, a4);
        a4.addExercise(e9);
        Exercise e10 = new Exercise("Slice", "SLICE", 30, 0, 0, a6);
        a6.addExercise(e10);
        Post p1 = new Post(1L, "Senior Workout", timestamp, "https://media.istockphoto.com/id/953518228/photo/senior-man-making-exercise-with-dumbbells.jpg?s=612x612&w=is&k=20&c=iMrLxlY665B9KLk3s0t7dAcYMlUrDvJEkxMebaEVxzE=", ev1, null, comments, likes);
        Post p2 = new Post(2L, "Arm Workout", timestamp, picture, null, w1, comments, likes);
        Post p3 = new Post(3L, "Leg Workout", timestamp, picture, null, w2, comments, likes);
        PostDTO postDTO = new PostDTO(p1);
        try {
            postDTO = postFacade.createEvenDTO(postDTO);
        }
        catch (Exception e){
            throw new Exception(e);
        }
        ev1.setLat(postDTO.getEvent().getLat());
        ev1.setLon(postDTO.getEvent().getLon());

        ev1.setPost(p1);
        w1.setPost(p2);
        w2.setPost(p3);
        a7.getPosts().add(p2);
        a2.getPosts().add(p3);
        a4.getPosts().add(p1);
        p1.setAccount(a4);
        p2.setAccount(a7);
        p3.setAccount(a2);


        em.persist(a1);
        em.persist(a2);
        em.persist(a3);
        em.persist(a4);
        em.persist(a5);
        em.persist(a6);
        em.persist(a7);
        em.persist(a8);
        em.persist(a9);

        em.persist(e1);
        em.persist(e2);
        em.persist(e3);
        em.persist(e4);
        em.persist(e5);
        em.persist(e6);
        em.persist(e7);
        em.persist(e8);
        em.persist(e9);
        em.persist(e10);
        em.persist(e20);
        em.persist(e21);
        em.persist(e22);
        em.persist(p1);
        em.persist(p2);
        em.persist(p3);

        em.getTransaction().commit();
        em.close();
    }
    
    public static void main(String[] args) throws Exception {
        try {
            populate();
        }
        catch (Exception e){
            throw new Exception("populate failed");
        }
    }
}
