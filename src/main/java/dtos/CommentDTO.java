package dtos;

import entities.Comment;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Comment} entity
 */
public class CommentDTO implements Serializable {
    private final int id;

    @NotNull
    private String text;

    public CommentDTO(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public CommentDTO(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
    }

    public int getId() {
        return id;
    }

    public String getText() {
        return text;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommentDTO)) return false;
        CommentDTO that = (CommentDTO) o;
        return getId() == that.getId() && getText().equals(that.getText());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getText());
    }

    @Override
    public String toString() {
        return "CommentDTO{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}