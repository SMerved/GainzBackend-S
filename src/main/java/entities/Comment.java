package entities;

import dtos.CommentDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "comment")
@NamedQuery(name = "Comment.deleteAllRows", query = "DELETE FROM Comment")
public class Comment {
    //I am unsure if these attribues are correct so please correct them if need be



    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    public Comment() {
    }
    public Comment(CommentDTO commentDTO) {
        this.id = commentDTO.getId();
    }

    @NotNull
    @Column(name = "text")
    private String text;


    @ManyToOne
    @JoinColumn(name = "post_id", referencedColumnName = "id") //, nullable = false)
    private Post post;

    public Comment(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public Comment(String text) {
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Comment)) return false;
        Comment comment = (Comment) o;
        return getText().equals(comment.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText());
    }


    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", post=" + post +
                '}';
    }
}
