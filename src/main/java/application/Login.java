
package application;

import database.Database;
import domain.enums.UserRole;
import domain.users.IUser;

public class Login {
    private String username;
    private boolean isAuthenticated;
    private IUser authenticatedUser;
    private final Database database;

    public Login(Database database) {
        this.database = database;
        this.isAuthenticated = false;
        this.authenticatedUser = null;
    }

    public boolean login(String username, String password) {
        this.username = username;
        IUser user = this.database.retrieveUserByUsername(username);
        if (user != null && user.getPasswordHash().equals(password)) {
            this.authenticatedUser = user;
            this.isAuthenticated = true;
            return true;
        } else {
            this.authenticatedUser = null;
            this.isAuthenticated = false;
            return false;
        }
    }

    public void logout() {
        this.isAuthenticated = false;
        this.authenticatedUser = null;
        this.username = null;
    }

    public String getUsername() {
        return this.username;
    }

    public boolean isAuthenticated() {
        return this.isAuthenticated;
    }

    public IUser getAuthenticatedUser() {
        return this.authenticatedUser;
    }

    public UserRole getRole() {
        return this.isAuthenticated && this.authenticatedUser != null ? this.authenticatedUser.getRole() : UserRole.NONE;
    }

    public void redirectUser() {
        if (this.isAuthenticated && this.authenticatedUser != null) {
            UserRole role = this.authenticatedUser.getRole();
            switch (role) {
                case USER -> System.out.println("Redirecting to User dashboard...");
                case TELLER -> System.out.println("Redirecting to Bank Teller dashboard...");
                case ADMIN -> System.out.println("Redirecting to Admin dashboard...");
                default -> System.out.println("Unknown role. Cannot redirect.");
            }

        } else {
            System.out.println("User not authenticated.");
        }
    }
}
//endd