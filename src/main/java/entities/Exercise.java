package entities;

import dtos.EventDTO;
import dtos.ExerciseDTO;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "exercise")
@NamedQuery(name = "Exercise.deleteAllRows", query = "DELETE FROM Exercise")

public class Exercise {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private long duration;

    private int reps;

    private float weight;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToMany
    @JoinTable(
            name = "exercise_workout",
            joinColumns = @JoinColumn(name = "exercise_id"),
            inverseJoinColumns = @JoinColumn(name = "workout_id"))
    private Set<Workout> workouts = new LinkedHashSet<>();

    public Exercise() {
    }

    public Exercise(String name, String description, long duration, int reps, float weight) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
    }

    public Exercise(String name, String description, long duration, int reps, float weight, Account account) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
        this.account = account;
    }

    public Exercise(Long id, String name, String description, long duration, int reps, float weight) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
    }

    public Exercise(Long id, String name, String description, long duration, int reps, float weight, Account account) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.reps = reps;
        this.weight = weight;
    }

    public Exercise(Long id, String name, long duration) {
        this.id = id;
        this.name = name;
        this.duration = duration;
    }

    public Exercise(ExerciseDTO exerciseDTO) {
        if (exerciseDTO.getId()!=null)
            this.id = exerciseDTO.getId();
        this.name = exerciseDTO.getName();
        this.description = exerciseDTO.getDescription();
        this.duration = exerciseDTO.getDuration();
        this.reps = exerciseDTO.getReps();
        this.weight = exerciseDTO.getWeight();
        if (exerciseDTO.getAccount() != null) {
            this.account = exerciseDTO.getAccount();
            this.account.addExercise(this);
        }

    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public int getReps() {
        return reps;
    }

    public void setReps(int reps) {
        this.reps = reps;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Set<Workout> getWorkouts() {
        return workouts;
    }

    public void setWorkouts(Set<Workout> workouts) {
        this.workouts = workouts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise)) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(getName(), exercise.getName()) && Objects.equals(getDuration(), exercise.getDuration());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, duration, reps, weight);
    }


    @Override
    public String toString() {
        return "Exercise{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", reps=" + reps +
                ", weight=" + weight +
                '}';
    }
}