package service;

import dao.AuthDAO;
import dao.FollowDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.User;
import service.proxyInterface.IFollowButtonService;
import service.request.FollowButtonRequest;
import service.response.FollowButtonResponse;

public class FollowButtonServiceImpl implements IFollowButtonService {
    @Override
    public FollowButtonResponse follow(FollowButtonRequest request) {
        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        String follower = request.getCurUser().getAlias();
        String followee = request.getOtherUser().getAlias();

        // return true meaning follow action, return false meaning unfollow action
        boolean result = getFollowDAO().follow(follower, followee);

        // depends on the successful action above, we update the user table and return the integer back to client
        int delta;
        if (result) {
            // meaning follower follows the followee
            // add 1 to followingCount on follower
            // add 1 to followerCount on followee
            delta = 1;
        }
        else {
            // meaning follower UNfollows the followee
            // substract 1 to followingCount on follower
            // substract 1 to followerCount on followee
            delta = -1;
        }
        User userFollower = getUserDAO().getUser(follower);
        int preFollowingCount = userFollower.getFollowingCount();

        User userFollowee = getUserDAO().getUser(followee);
        int preFollowerCount = userFollowee.getFollowerCount();

        getUserDAO().updateUserFollowingCount(follower, String.valueOf(preFollowingCount + delta));
        getUserDAO().updateUserFollowerCount(followee, String.valueOf(preFollowerCount + delta));

        return new FollowButtonResponse(userFollowee.getFollowingCount(), preFollowerCount + delta);
    }

    public UserDAO getUserDAO() {
        return new UserDAO();
    }

    public FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
