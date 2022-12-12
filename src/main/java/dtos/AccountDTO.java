package dtos;

import entities.Account;
import entities.Role;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;
import java.util.*;

/**
 * A DTO for the {@link entities.Account} entity
 */
public class AccountDTO implements Serializable {
    private Long id;
    @NotNull
    private String username;
    @NotNull
    @Size(min = 1, max = 255)
    private String password;

    private List<Role> roleList = new ArrayList<>();
    @NotNull
    private Timestamp timestamp;
    @NotNull
    private String email;

    private Set<InnerAccountDTO> followedAccounts = new HashSet<>();

    public AccountDTO(String username, String password, Timestamp timestamp, String email) {
        this.username = username;
        this.password = password;
        this.timestamp = timestamp;
        this.email = email;
    }

    public AccountDTO(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public AccountDTO(Account account) {
        if (account.getUsername() != null)
            this.username = account.getUsername();
        this.password = account.getPassword();
        this.roleList = new ArrayList<>();
        this.roleList.addAll(account.getRoleList());
        this.timestamp = account.getTimestamp();
        this.email = account.getEmail();
        this.followedAccounts = new HashSet<>();
        for (Account acc : account.getFollowedAccounts()) {
            this.followedAccounts.add(new InnerAccountDTO(acc));
        }
    }

    public AccountDTO(InnerAccountDTO account){

        if (account.getUsername() != null)
            this.username = account.getUsername();
        this.password = account.getPassword();

        this.timestamp = account.getTimestamp();
        this.email = account.getEmail();


    }


    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getEmail() {
        return email;
    }

    public List<Role> getRoleList() {
        return roleList;
    }

    public Set<AccountDTO> getFollowedAccounts() {
        Set<AccountDTO> accountDTOS = new HashSet<>();
        if (followedAccounts!=null)
        for (InnerAccountDTO innerAccountDTO: followedAccounts){
            accountDTOS.add( new AccountDTO(innerAccountDTO));
        }
        return accountDTOS;
    }


    private static class InnerAccountDTO {
        private String username;
        private String password;
        private String role;
        private Timestamp timestamp;
        private String email;

       // private Set<InnerAccountDTO> followedAccounts;

        public InnerAccountDTO(Account account) {
            if (account.getUsername() != null)
                this.username = account.getUsername();
            this.password = account.getPassword();

            this.timestamp = account.getTimestamp();
            this.email = account.getEmail();
            /*
            this.followedAccounts = new HashSet<>();
            for (Account acc : account.getFollowedAccounts()) {
                this.followedAccounts.add(new AccountDTO(acc));
            }*/
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRole() {
            return role;
        }

        public void setRole(String role) {
            this.role = role;
        }

        public Timestamp getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(Timestamp timestamp) {
            this.timestamp = timestamp;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "password = " + password + ", " +
                "timestamp = " + timestamp + ", " +
                "email = " + email + ")";
    }
}