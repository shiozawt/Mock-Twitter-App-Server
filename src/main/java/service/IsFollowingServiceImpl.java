package service;

import dao.AuthDAO;
import dao.FollowDAO;
import domain.AuthToken;
import service.proxyInterface.IIsFollowingService;
import service.request.IsFollowingRequest;
import service.response.IsFollowingResponse;

public class IsFollowingServiceImpl implements IIsFollowingService {
    @Override
    public IsFollowingResponse isFollowing(IsFollowingRequest request) {
        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        String follower = request.getCurUser().getAlias();
        String followee = request.getOtherUser().getAlias();
        boolean result = getFollowDAO().isFollowing(follower, followee);

        return new IsFollowingResponse(result);
    }

    FollowDAO getFollowDAO() {
        return new FollowDAO();
    }
}
