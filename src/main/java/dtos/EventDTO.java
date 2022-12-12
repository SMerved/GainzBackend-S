package dtos;

import entities.Event;

import javax.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link Event} entity
 */
public class EventDTO implements Serializable {
    private final Long id;
    private String address;
    private String zipcode;
    private String city;

    private String description;

    private String date;
    private Float lat;
    private Float lon;

    public EventDTO(Long id) {
        this.id = id;
    }

    public EventDTO(Event event) {
        this.id = event.getId();
        this.address = event.getAddress();
        this.lon = event.getLon();
        this.lat = event.getLat();
        this.city = event.getCity();
        this.zipcode = event.getZipcode();
        this.description = event.getDescription();
        this.date = event.getDate();
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventDTO entity = (EventDTO) o;
        return Objects.equals(this.id, entity.id);
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "id = " + id + ")";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}