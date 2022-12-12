package entities;

import dtos.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "post")
@NamedQuery(name = "Post.deleteAllRows", query = "DELETE from Post")
public class Post {


    public Post() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String title;

    private Timestamp timestamp;

    private String picture;

    @OneToOne(mappedBy = "post", cascade = CascadeType.PERSIST)
    //@Column(name = "workoutID")//TODO: rename WorkoutID
    private Event event;

    @OneToOne(mappedBy = "post", cascade = CascadeType.PERSIST)
    //@Column(name = "workoutID")//TODO: rename WorkoutID
    private Workout workout;

    @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST)
    private Set<Comment> comments;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id"))
    private Set<Account> accountsLiked = new LinkedHashSet<>();


    public Post(Long id, String title, Timestamp timestamp, String picture, Event event, Workout workout, Set<Comment> comments, Set<Account> accountsLiked) {
        this.id = id;
        this.title = title;
        this.timestamp = timestamp;
        this.picture = picture;
        this.event = event;
        this.workout = workout;
        this.comments = comments;
        this.accountsLiked = accountsLiked;
    }


    public Post(String title, String picture, Event event, Workout workout, Set<Comment> comments) {
        this.title = title;
        this.picture = picture;
        this.event = event;
        this.workout = workout;
        this.comments = comments;
    }

    public Post(PostDTO postDTO) {
        this.id = postDTO.getId();
        this.title = postDTO.getTitle();
        this.timestamp = postDTO.getTimestamp();
        this.picture = postDTO.getPicture();
        if (postDTO.getEvent() != null) {
            this.event = new Event(postDTO.getEvent());
            this.event.setPost(this);
        }
        if (postDTO.getWorkout() != null) {
            this.workout = new Workout(postDTO.getWorkout());
            this.workout.setPost(this);
        }
        this.comments = new HashSet<>();
        if(postDTO.getComments()!=null)
        for (CommentDTO commentDTO : postDTO.getComments()) {
            Comment comment = new Comment(commentDTO);
            comment.setPost(this);
            this.comments.add(comment);
        }
        if (postDTO.getAccountDTO() != null){
            this.account = new Account(postDTO.getAccountDTO());
            this.account.getPosts().add(this);
        }
        this.accountsLiked = new HashSet<>();
        if (postDTO.getAccountsLiked()!=null)
        for (AccountDTO accountDTOs : postDTO.getAccountsLiked()) {
            Account acc = new Account(accountDTOs);
            acc.getLikedPosts().add(this);
            this.accountsLiked.add(acc);
        }
    }
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public Workout getWorkout() {
        return workout;
    }

    public void setWorkout(Workout workout) {
        this.workout = workout;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Account> getAccountsLiked() {
        return accountsLiked;
    }

    public void setAccountsLiked(Set<Account> accountsLiked) {
        this.accountsLiked = accountsLiked;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", timestamp=" + timestamp +
                ", picture='" + picture + '\'' +
                ", event=" + event +
                ", workout=" + workout +
                ", comments=" + comments +
                ", account=" + account +
                ", accountsLiked=" + accountsLiked +
                '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Post)) return false;
        Post post = (Post) o;


        return Objects.equals(getTitle(), post.getTitle()) && Objects.equals(getPicture(), post.getPicture()) && Objects.equals(getComments(), post.getComments()) && Objects.equals(getAccount(), post.getAccount());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getPicture(), getEvent(), getWorkout(), getComments(), getAccount());
    }
}
