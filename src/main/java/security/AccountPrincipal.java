package security;

import entities.Account;
import entities.User;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AccountPrincipal implements Principal {
  private Long id;
  private String username;
  private List<String> roles = new ArrayList<>();

  /* Create a UserPrincipal, given the Entity class User*/
  public AccountPrincipal(Account account) {
    this.username = account.getUsername();
    this.roles = account.getRolesAsStrings();
  }

  public AccountPrincipal(String username, String[] roles) {
    super();
    this.username = username;
    this.roles = Arrays.asList(roles);
  }

  @Override
  public String getName() {
    return username;
  }

  public Long getId() {
    return id;
  }

  public boolean isUserInRole(String role) {
    return this.roles.contains(role);
  }
}