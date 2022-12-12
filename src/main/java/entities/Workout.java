package entities;

import dtos.EventDTO;
import dtos.ExerciseDTO;
import dtos.WorkoutDTO;
import javax.persistence.*;
import java.util.*;

@Table(name = "workout")
@Entity
@NamedQuery(name = "Workout.deleteAllRows", query = "DELETE FROM Workout")
public class Workout {
    //I am unsure if these attribues are correct so please correct them if need be


    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private int breakTime;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    public Workout(WorkoutDTO workoutDTO) {
        this.id = workoutDTO.getId();
        this.breakTime = workoutDTO.getBreakTime();
        this.description = workoutDTO.getDescription();
        this.exercises = new HashSet<>();
        for (ExerciseDTO exerciseDTO: workoutDTO.getExercises()){
            Exercise exercise = new Exercise(exerciseDTO);
            exercise.getWorkouts().add(this);
            this.exercises.add(exercise);
        }
    }

    public Workout() {

    }

    public Workout(String description, int breakTime, Set<Exercise> exercises) {
        this.description = description;
        this.breakTime = breakTime;
        this.exercises = exercises;
    }

    public Workout(Long id, String description, int breakTime) {
        this.id = id;
        this.description = description;
        this.breakTime = breakTime;
    }



    @ManyToMany
    @JoinTable(
            name = "exercise_workout",
            joinColumns = @JoinColumn(name = "workout_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private Set<Exercise> exercises = new LinkedHashSet<>();

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public Set<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(Set<Exercise> exercises) {
        this.exercises = exercises;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Workout)) return false;
        Workout workout = (Workout) o;
        return Objects.equals(getDescription(), workout.getDescription()) && Objects.equals(getBreakTime(), workout.getBreakTime());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, breakTime);
    }

    @Override
    public String toString() {
        return "Workout{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", breakTime=" + breakTime +
                '}';
    }
}
