package service;

import dao.AuthDAO;
import domain.AuthToken;
import service.proxyInterface.ILogoutService;
import service.request.LogoutRequest;
import service.response.LogoutResponse;

public class LogoutServiceImpl implements ILogoutService {
    @Override
    public LogoutResponse logout(LogoutRequest request) {
        Boolean success = getAuthDAO().logout(new AuthToken(request.getToken()));
        if(success){
            return new LogoutResponse(true, "");
        }
        else{
            return new LogoutResponse(false, "Failed to Log you Out, Please try again");
        }
    }
    AuthDAO getAuthDAO() {
        return new AuthDAO();
    }
}
