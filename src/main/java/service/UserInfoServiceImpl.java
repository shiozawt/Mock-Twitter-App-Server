package service;

import dao.AuthDAO;
import dao.FollowDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.IUserInfoService;
import service.request.UserInfoRequest;
import service.response.UserInfoResponse;

public class UserInfoServiceImpl implements IUserInfoService {

    @Override
    public UserInfoResponse getUserInfo(UserInfoRequest request) {
        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        // get firstname, lastname, alias, imageURL, followerCount, followingCount
        User user = getUserInfoDAO().getUser(request.getAlias());

        return new UserInfoResponse(user);
    }

    UserDAO getUserInfoDAO() {
        return new UserDAO();
    }
}
