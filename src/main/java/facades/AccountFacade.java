package facades;

import dtos.AccountDTO;
import dtos.PostDTO;
import dtos.RenameMeDTO;
import entities.*;
import dtos.ExerciseDTO;
import dtos.PostDTO;
import dtos.RenameMeDTO;
import entities.Account;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class AccountFacade {
    private static AccountFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private AccountFacade() {

    }

    public static AccountFacade getAccountFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AccountFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public AccountDTO getById(String id) //throws AccountNotFoundException
    {
        EntityManager em = emf.createEntityManager();
        Account account = em.find(Account.class, id);
        //if (account.getId() == null)
        //    throw AccountNotFoundException("The account entity with ID: "+id+" Was not found");
        return new AccountDTO(account);
    }
    public AccountDTO create(AccountDTO accountDTO) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Account account = new Account(accountDTO.getUsername(), accountDTO.getPassword(), timestamp, accountDTO.getEmail());
        //account.encryptPassword();
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(account);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AccountDTO(account);
    }

    public ArrayList<PostDTO> getAllFollowedPosts(String id){
        EntityManager em = getEntityManager();
        Account account = em.find(Account.class, id);
        Set<Account> followedAccounts = account.getFollowedAccounts();
        String accountId;
        List<Post> allFollowedPosts = new ArrayList<>();
        for (Account acc: followedAccounts) {
            accountId = acc.getUsername();
            TypedQuery<Post> query = (TypedQuery<Post>) em.createNativeQuery("SELECT * FROM post WHERE account_id = ?", Post.class);
            query.setParameter(1, accountId);
            List<Post> posts = query.getResultList();
            allFollowedPosts.addAll(posts);
        }

        System.out.println("allFollowedPosts" + allFollowedPosts);
        ArrayList<PostDTO> postDtos = new ArrayList<>();
        for (Post post: allFollowedPosts) {
            postDtos.add(new PostDTO(post));
        }
        System.out.println("postDtos" + postDtos);
        return postDtos;
    }

    public AccountDTO updateFollows(String accountId, String followId) { //throws RenameMeNotFoundException {
        EntityManager em = getEntityManager();
        Account account;
        Account follow;
        Account a;
        boolean removedFollow;
        try {
            em.getTransaction().begin();
            account = em.find(Account.class, accountId);
            follow = em.find(Account.class, followId);
            if (account != null && follow != null) {
                removedFollow = account.getFollowedAccounts().removeIf(acc -> Objects.equals(acc.getUsername(), follow.getUsername()));
                if (!removedFollow){
                    account.getFollowedAccounts().add(follow);
                }}

            a = em.merge(account);
            System.out.println(a.getFollowedAccounts());
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new AccountDTO(a);
    }
    public static List<ExerciseDTO> getAllExercises(String accountID){
        EntityManager em = emf.createEntityManager();
        TypedQuery<Exercise> queryListExercises = (TypedQuery<Exercise>) em.createNativeQuery("SELECT * FROM exercise where account_id = ? ", Exercise.class);
        queryListExercises.setParameter(1, accountID);
        List<Exercise> listExercises = queryListExercises.getResultList();
        List<ExerciseDTO> exercises = new ArrayList<>();
        for (int i = 0; i < listExercises.size(); i++) {
            exercises.add(new ExerciseDTO(listExercises.get(i)));
        }
        return exercises;
    }

    public Account getVeryfiedAccount(String username, String password) throws AuthenticationException {
        EntityManager em = emf.createEntityManager();
        Account account;
        try {
            account = em.find(Account.class, username);
            account.setRoleList(new ArrayList<>());
            if (account == null || !account.verifyPassword(password)) {
                throw new AuthenticationException("Invalid user name or password");
            }
        } finally {
            em.close();
        }
        return account;
    }

}