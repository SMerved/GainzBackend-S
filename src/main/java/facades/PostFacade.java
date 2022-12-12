package facades;

import com.google.gson.Gson;
import dtos.*;
import entities.Account;
import entities.Post;
import utils.HttpUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PostFacade {

    private static PostFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PostFacade() {
    }


    /**
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PostFacade getPostFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PostFacade();
        }
        return instance;
    }

    public static ArrayList<PostDTO> getAllPosts() {
        EntityManager em = emf.createEntityManager();
        TypedQuery<Post> query = em.createQuery("SELECT r FROM Post r", Post.class);
        List<Post> rms = query.getResultList();


        ArrayList<PostDTO> postDtos = new ArrayList<>();
        for (Post post : rms) {
            postDtos.add(new PostDTO(post));
        }
        System.out.println("postDtos" + postDtos);
        return postDtos;
    }

    public ArrayList<PostDTO> getPostsFromAccountId(String username) {
        EntityManager em = emf.createEntityManager();

        System.out.println(username);

//        TypedQuery<Post> query = em.createQuery("SELECT r FROM Post r WHERE Post.", Post.class);
        TypedQuery<Post> query = (TypedQuery<Post>) em.createNativeQuery("SELECT * FROM post WHERE account_id = ? ", Post.class);
        query.setParameter(1, username);
//        query.setParameter("id", id); SELECT p FROM Person p JOIN p.address a JOIN a.city c WHERE c.zipCode = :zipCode ", Person.class
        List<Post> postList = query.getResultList();
        ArrayList<PostDTO> postDtos = new ArrayList<>();
        for (Post post : postList) {
            postDtos.add(new PostDTO(post));
        }
        return postDtos;
    }

    public PostDTO createPost(PostDTO postDTO) {
        EntityManager em = emf.createEntityManager();

        Post post = new Post(postDTO);

        try {
            em.getTransaction().begin();
            post.setTimestamp(new Timestamp(System.currentTimeMillis()));
            em.persist(post);
            em.getTransaction().commit();
        } finally {
            em.close();
        }


        return new PostDTO(post);
    }

    public PostDTO getPostDTOById(long id) {
        EntityManager em = emf.createEntityManager();

        Post post = em.find(Post.class, id);

        return new PostDTO(post);
    }


    public PostDTO updateLikes(String username, Long postID) { //throws RenameMeNotFoundException {
        EntityManager em = emf.createEntityManager();
        Account account;
        Post post;
        Post p;
        boolean removedLike;
        try {
            em.getTransaction().begin();
            account = em.find(Account.class, username);
            post = em.find(Post.class, postID);
            if (account != null) {
                removedLike = post.getAccountsLiked().removeIf(acc -> Objects.equals(acc.getUsername(), account.getUsername()));
                if (!removedLike){
                post.getAccountsLiked().add(account);
            }}
            p = em.merge(post);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new PostDTO(p);
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }


    public PostDTO createEvenDTO(PostDTO postDTO) throws Exception {
        EventDTO event = postDTO.getEvent();

        String city = event.getCity();
        String postalcode = event.getZipcode();
        String street = event.getAddress(); //plus number if available
        String query = street + ",%20" + city + ",%20" + postalcode;
        String url = "http://api.positionstack.com/v1/forward?access_key=a022644b972a92a8de6e8ecfc282a7bd&query=" + query;
        System.out.println(url);
        String str = HttpUtils.fetchData(url);
        Gson gson = new Gson();
        LocationsDTO locations = gson.fromJson(str, LocationsDTO.class);
        System.out.println(locations.data.get(0));

        try {
            LocationDTO locationDTO = locations.data.get(0);
            event.setLon((float) locationDTO.longitude);
            event.setLat((float) locationDTO.latitude);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("No coordinates could be found. Address data not valæid");
        }
        postDTO.setEvent(event);
        return postDTO;
    }

}
