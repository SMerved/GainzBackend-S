package dtos;

import entities.Account;
import entities.Exercise;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link entities.Exercise} entity
 */
public class ExerciseDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private long duration;
    private int reps;
    private float weight;
    private AccountDTO accountDTO;

    public ExerciseDTO() {
    }

    public ExerciseDTO(Long id, String name, String description, long duration, int reps, float weight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
    }

    public ExerciseDTO(String name, String description, long duration, int reps, float weight, AccountDTO accountDTO) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
        this.accountDTO = accountDTO;
    }

    public ExerciseDTO(Exercise exercise) {
        if (exercise.getId() != null)
            this.id = exercise.getId();
        this.name = exercise.getName();
        this.description = exercise.getDescription();
        this.duration = exercise.getDuration();
        this.reps = exercise.getReps();
        this.weight = exercise.getWeight();
        if (exercise.getAccount() != null) {
            this.accountDTO = new AccountDTO(exercise.getAccount());
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getDuration() {
        return duration;
    }

    public int getReps() {
        return reps;
    }

    public float getWeight() {
        return weight;
    }

    public Account getAccount() {
        return new Account(accountDTO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExerciseDTO entity = (ExerciseDTO) o;
        return Objects.equals(this.id, entity.id) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.description, entity.description) &&
                Objects.equals(this.duration, entity.duration) &&
                Objects.equals(this.reps, entity.reps) &&
                Objects.equals(this.weight, entity.weight);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, duration, reps, weight);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                "description = " + description + ", " +
                "duration = " + duration + ", " +
                "reps = " + reps + ", " +
                "weight = " + weight + ")";
    }
}