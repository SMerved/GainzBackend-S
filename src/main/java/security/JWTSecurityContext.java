package security;
import java.security.Principal;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.SecurityContext;
public class JWTSecurityContext implements SecurityContext {
   AccountPrincipal account;
   ContainerRequestContext request;

   public JWTSecurityContext(AccountPrincipal account,ContainerRequestContext request) {
       this.account = account;
       this.request = request;
   }
   @Override
   public boolean isUserInRole(String role) {
       return account.isUserInRole(role);
   }
   @Override
   public boolean isSecure() {
       return request.getUriInfo().getBaseUri().getScheme().equals("https");
   }
   @Override
   public Principal getUserPrincipal() {
       return account;
   }
   @Override
   public String getAuthenticationScheme() {
       return "JWT"; //Only for INFO
   }
}