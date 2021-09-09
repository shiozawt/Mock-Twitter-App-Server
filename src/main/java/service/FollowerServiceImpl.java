package service;

import dao.AuthDAO;
import dao.FollowDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.IFollowerService;
import service.request.FollowerRequest;
import service.response.FollowerResponse;

import java.util.ArrayList;
import java.util.List;

public class FollowerServiceImpl implements IFollowerService {

    @Override
    public FollowerResponse getFollowers(FollowerRequest request) {
        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        List<String> followerAlias = getFollowDAO().getFollowers(request);
        List<User> follower = new ArrayList<User>();

        // query the user table to make user objects
        for (int i = 0; i < followerAlias.size(); i++) {
            User user = getUserDAO().getUser(followerAlias.get(i));
            follower.add(user);
        }

        if (followerAlias.size() != 0) {
            return new FollowerResponse(follower, true); // how to decide
        }
        else {
            return new FollowerResponse(follower, false); // how to decide
        }
    }

    public List<String> getFollowersForPostStatus(String followee) {
        return getFollowDAO().getAllFollowers(followee);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }

    UserDAO getUserDAO() {
        return new UserDAO();
    }
}
