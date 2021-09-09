package service.request;

public class LogoutRequest extends Request{

    public LogoutRequest() {
        super(null);
    }
    public LogoutRequest(String token) {
        super(token);
    }
}
