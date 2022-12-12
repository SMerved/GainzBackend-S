package entities;

import dtos.EventDTO;
import javax.persistence.*;
import java.util.Date;

@Table(name = "event")
@Entity
@NamedQuery(name = "Event.deleteAllRows", query = "DELETE FROM Event")
public class Event {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "id")
    private Post post;

    @Column(name = "address")
    private String address;

    @Column(name = "zipcode")
    private String zipcode;

    @Column(name = "city")
    private String city;

    @Column(name = "description")
    private String description;

    @Column(name = "lat" , precision=8, scale=2)
    private Float lat;

    @Column(name = "lon", precision=8, scale=2)
    private Float lon;

    @Column(name = "date")
    private String date;

    public Event(){    }

    public Event(String address, String zipcode, String city, String description, String date) {
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.description = description;
        this.date = date;
    }

    public Event(EventDTO eventDTO) {
        if (eventDTO.getId()!=null)
            this.id = eventDTO.getId();
        this.address = eventDTO.getAddress();
        this.zipcode = eventDTO.getZipcode();
        this.city = eventDTO.getCity();
        if (eventDTO.getLat()!= null || eventDTO.getLon()!= null){
            this.lon = eventDTO.getLon();
            this.lat = eventDTO.getLat();
        }
        this.description = eventDTO.getDescription();
        this.date = eventDTO.getDate();
    }

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


