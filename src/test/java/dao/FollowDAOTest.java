package dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import service.request.FollowerRequest;
import service.request.FollowingRequest;

import java.util.List;

public class FollowDAOTest{


    @Test
    public void followDAOIntegrationTest(){

        FollowDAO followDAO= new FollowDAO();

        List<String> allFollowList = followDAO.getAllFollowers("@server_test_user");
        System.out.print(allFollowList.size());

        Assertions.assertEquals(2, allFollowList.size());
        Assertions.assertEquals("@test_follower", allFollowList.get(0));
        Assertions.assertEquals("@test_follower2", allFollowList.get(1));

        FollowerRequest followerRequest = new FollowerRequest("@server_test_user", 10, null, "token");
        List<String> followerList = followDAO.getFollowers(followerRequest);
        System.out.print(followerList.size());

        Assertions.assertEquals(2, followerList.size());
        Assertions.assertEquals("@test_follower", followerList.get(0));
        Assertions.assertEquals("@test_follower2", followerList.get(1));

        FollowingRequest followingRequest = new FollowingRequest("@server_test_user", 10, null, "token");
        List<String> followingList = followDAO.getFollowees(followingRequest);
        System.out.print(followingList.size());

        Assertions.assertEquals(0, followingList.size());

        FollowingRequest followingRequest2 = new FollowingRequest("@test_follower", 10, null, "token");
        List<String> followingList2 = followDAO.getFollowees(followingRequest2);

        Assertions.assertEquals("@server_test_user", followingList2.get(0));

    }
}