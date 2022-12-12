package dtos;

import entities.Exercise;
import entities.Workout;

import java.io.Serializable;
import java.util.*;

/**
 * A DTO for the {@link Workout} entity
 */
public class WorkoutDTO implements Serializable {
    private Long id;

    private String description;

    private int breakTime;

    private Set<ExerciseDTO> exerciseDTOs;

    public WorkoutDTO(Long id) {
        this.id = id;
    }

    public WorkoutDTO(Workout workout) {

        this.id = workout.getId();
        this.description = workout.getDescription();
        this.breakTime = workout.getBreakTime();
        this.exerciseDTOs = new LinkedHashSet<>();
        workout.getExercises().forEach((exercise)->exerciseDTOs.add(new ExerciseDTO(exercise)));
    }

    public WorkoutDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public int getBreakTime() {
        return breakTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBreakTime(int breakTime) {
        this.breakTime = breakTime;
    }

    public Set<ExerciseDTO> getExercises() {
        return exerciseDTOs;
    }

    public void setExerciseDTOS(Set<ExerciseDTO> exerciseDTOS) {
        this.exerciseDTOs = exerciseDTOS;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutDTO entity = (WorkoutDTO) o;
        return Objects.equals(this.id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}