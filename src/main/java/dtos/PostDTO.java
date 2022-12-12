package dtos;


import entities.Account;
import entities.Comment;
import entities.Post;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link Post} entity
 */
public class PostDTO implements Serializable {
    private  Long id;
    private  String title;
    private  Timestamp timestamp;
    private  String picture;
    private  EventDTO event;
    private  WorkoutDTO workout;
    private  Set<CommentDTO> comments;
    private  AccountDTO accountDTO;
    private Set<AccountDTO> accountsLiked;

    public PostDTO(Long id, String title, Timestamp timestamp, String picture, EventDTO event, WorkoutDTO workout, Set<CommentDTO> comments, Set<AccountDTO> accountsLiked) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.picture = picture;
        this.event = event;
        this.workout = workout;
        this.comments = comments;
        this.accountsLiked = accountsLiked;
    }

    public PostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.timestamp = post.getTimestamp();
        this.picture = post.getPicture();
        if (post.getEvent() != null)
            this.event = new EventDTO(post.getEvent());
        if (post.getAccount() != null)
            this.accountDTO = new AccountDTO(post.getAccount());
        if (post.getWorkout() != null)
            this.workout = new WorkoutDTO(post.getWorkout());
        this.comments = new HashSet<>();
        for (Comment comment : post.getComments()) {
            this.comments.add(new CommentDTO(comment));
        }
        this.accountsLiked = new HashSet<>();
        for (Account account : post.getAccountsLiked()) {
            this.accountsLiked.add(new AccountDTO(account));
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getPicture() {
        return picture;
    }

    public EventDTO getEvent() {
        return event;
    }

    public WorkoutDTO getWorkout() {
        return workout;
    }

    public Set<CommentDTO> getComments() {
        return comments;
    }

    public Set<AccountDTO> getAccountsLiked() {
        return accountsLiked;
    }

    public AccountDTO getAccountDTO() {
        return accountDTO;
    }

    public void setAccountDTO(AccountDTO accountDTO) {
        this.accountDTO = accountDTO;
    }

    public void setEvent(EventDTO event) {
        this.event = event;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PostDTO entity = (PostDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.title, entity.title) &&
                Objects.equals(this.picture, entity.picture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, timestamp, picture, event, workout, comments, accountsLiked);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "title = " + title + ", " +
                "timestamp = " + timestamp + ", " +
                "picture = " + picture + ", " +
                "event = " + event + ", " +
                "workout = " + workout + ", " +
                "comments = " + comments + ", " +
                "accountsLiked = " + accountsLiked + ")";
    }
}
