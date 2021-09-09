package service;

import dao.AuthDAO;
import dao.PasswordDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.ILoginService;
import service.request.LoginRequest;
import service.response.LoginResponse;

public class LoginServiceImpl implements ILoginService {
    @Override
    public LoginResponse login(LoginRequest request) {
        if (getPasswordDAO().login(request.getUsername(), request.getPassword())){
            AuthToken at = getAuthDAO().login();
            User u = getUserDao().getUser("@" + request.getUsername());
            return new LoginResponse(u, at);
        }
        else{
            throw new RuntimeException("401 : Invalid Username or Password");
        }
    }
    UserDAO getUserDao(){return new UserDAO();}
    AuthDAO getAuthDAO() {return new AuthDAO();}
    PasswordDAO getPasswordDAO(){return new PasswordDAO();}
}