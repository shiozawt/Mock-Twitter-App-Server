package service;

import dao.AuthDAO;
import dao.FollowDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.IFollowingService;
import service.request.FollowingRequest;
import service.response.FollowingResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains the business logic for getting the users a user is following.
 */
public class FollowingServiceImpl implements IFollowingService {

    @Override
    public FollowingResponse getFollowees(FollowingRequest request) {
        // Authenticating the api call
        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        List<String> followeeAlias = getFollowDAO().getFollowees(request);
        List<User> followee = new ArrayList<User>();

        // query the user table to make user objects
        for (int i = 0; i < followeeAlias.size(); i++) {
            User user = getUserDAO().getUser(followeeAlias.get(i));
            followee.add(user);
        }

        if (followeeAlias.size() != 0) {
            return new FollowingResponse(followee, true); // how to decide
        }
        else {
            return new FollowingResponse(followee, false); // how to decide
        }
    }

    public FollowDAO getFollowDAO() {
        return new FollowDAO();
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }
}
