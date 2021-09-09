package service.request;


public class LoginRequest{

    private String username;
    private String password;

    public LoginRequest() {}


    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getPassword() {
        return password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
