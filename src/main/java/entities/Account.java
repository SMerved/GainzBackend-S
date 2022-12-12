package entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import dtos.AccountDTO;
import org.mindrot.jbcrypt.BCrypt;

@Entity
@Table(name = "account")
@NamedQuery(name = "Account.deleteAllRows", query = "DELETE FROM Account ")
public class Account implements Serializable {


    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "username", length = 25)
    private String username;

    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "password")
    private String password;

    @JoinTable(name = "account_roles", joinColumns = {
            @JoinColumn(name = "username", referencedColumnName = "username")}, inverseJoinColumns = {
            @JoinColumn(name = "rolename", referencedColumnName = "rolename")})
    @ManyToMany
    private List<Role> roleList = new ArrayList<>();

    @NotNull
    @Column(name = "timestamp")
    private Timestamp timestamp;

    @NotNull
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "account")
    private List<Exercise> exercises = new ArrayList<>();

    @OneToMany(mappedBy = "account")
    private List<Post> posts = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "likes",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> likedPosts = new LinkedHashSet<>();

    @ManyToMany
    @JoinTable(
            name = "follows",
            joinColumns = @JoinColumn(name = "account_id"),
            inverseJoinColumns = @JoinColumn(name = "follow_id"))
    private Set<Account> followedAccounts = new LinkedHashSet<>();

    public Account() {}

    //TODO Change when password is hashed
   // public boolean verifyPassword(String pw) {
   //     return BCrypt.checkpw(pw, password);
   //}

    public boolean verifyPassword(String pw) {
        return pw.equals(password);
    }
    public String encryptPassword(){
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        return password;
    }

    public Account(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Account(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public Account(String username, String password, Timestamp timestamp, String email) {
        this.username = username;
        this.password = password;
        this.timestamp = timestamp;
        this.email = email;
    }

    public Account(AccountDTO accountDTO) {
        this.username = accountDTO.getUsername();
        this.password = accountDTO.getPassword();
        this.timestamp = accountDTO.getTimestamp();
        this.roleList = new ArrayList<>();
        if(accountDTO.getRoleList()!=null)
        for (Role r : accountDTO.getRoleList()) {
            this.roleList.add(r);
        }

        this.email = accountDTO.getEmail();
        this.followedAccounts = new HashSet<>();
        for (AccountDTO accDTO : accountDTO.getFollowedAccounts()) {
            this.followedAccounts.add(new Account(accDTO));
        }
    }

    public Account(String username, String password, Timestamp timestamp, String email, List <Post> posts) {
        this.username = username;
        this.password = password;
        this.timestamp = timestamp;
        this.email = email;
        this.posts = posts;
    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public Set<Account> getFollowedAccounts() {
        return followedAccounts;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Set<Post> getLikedPosts() {
        return likedPosts;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    public void setLikedPosts(Set<Post> likedPosts) {
        this.likedPosts = likedPosts;

    }
    public List<Exercise> getExercises() {
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;

    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }

    public void addRole(Role userRole) {
        roleList.add(userRole);
    }

    public List<String> getRolesAsStrings() {
        if (roleList.isEmpty()) {
            return null;
        }
        List<String> rolesAsStrings = new ArrayList<>();
        roleList.forEach((role) -> {
            rolesAsStrings.add(role.getRoleName());
        });
        return rolesAsStrings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        if (!(Objects.equals(getPassword(), account.getPassword())|| BCrypt.checkpw(getPassword(),account.getPassword())))
            return false;
        return Objects.equals(getUsername(), account.getUsername()) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername(), getPassword(), timestamp, getEmail());
    }

    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", roleList=" + roleList +
                ", timestamp=" + timestamp +
                ", email='" + email + '\'' +
                '}';
    }
}