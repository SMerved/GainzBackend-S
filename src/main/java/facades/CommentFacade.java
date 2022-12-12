package facades;

import dtos.AccountDTO;
import dtos.CommentDTO;
import entities.Account;
import entities.Comment;
import entities.Post;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Timestamp;

public class CommentFacade {
    private static CommentFacade instance;
    private static EntityManagerFactory emf;

    public static CommentFacade getCommentFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CommentFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public CommentDTO create(CommentDTO commentDTO, Long id) {
        Comment comment = new Comment(commentDTO.getText());
        EntityManager em = getEntityManager();
        try {
            Post post = em.find(Post.class, id);
            post.getComments().add(comment);
            comment.setPost(post);
            em.getTransaction().begin();
            em.persist(comment);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return new CommentDTO(comment);
    }

}
