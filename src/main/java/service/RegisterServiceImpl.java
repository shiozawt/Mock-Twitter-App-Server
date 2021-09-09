package service;

import dao.AuthDAO;
import dao.PasswordDAO;
import dao.S3DAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.IRegisterService;
import service.request.RegisterRequest;
import service.response.LoginResponse;

public class RegisterServiceImpl implements IRegisterService {
    @Override
    public LoginResponse register(RegisterRequest request) {
        if (getPasswordDAO().register(request.getUsername(), request.getPassword())){
            String imageURL = null;
            if (request.getB64Pic() != null) {
                imageURL = getS3DAO().upload(request.getUsername(), request.getB64Pic());
            }
            AuthToken at = getAuthDAO().login();
            User u = getUserDao().putUser(request.getUsername(), request.getFirstname(), request.getLastname(), imageURL);
            if(u != null && at != null){
                return new LoginResponse(u, at);
            }
            throw new RuntimeException("500 : Database Failure");
        }
        else{
            throw new RuntimeException("401 : Username already taken");
        }
    }
    S3DAO getS3DAO(){return new S3DAO();}
    UserDAO getUserDao(){return new UserDAO();}
    AuthDAO getAuthDAO() {return new AuthDAO();}
    PasswordDAO getPasswordDAO(){return new PasswordDAO();}
}