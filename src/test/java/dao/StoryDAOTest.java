package dao;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.request.FeedRequest;
import service.request.StoryRequest;

import java.util.List;

public class StoryDAOTest {

    @Test
    void StoryTest() {

        StoryDAO sDao = new StoryDAO();

        StoryRequest request = new StoryRequest("@server_test_user", 10, null, "token");

        //build list
        sDao.getStory(request);

        //get list
        List<String> content = sDao.getContentList();

        Assertions.assertEquals(content.get(0), "@2");
        Assertions.assertEquals(content.get(1), "@1");
        Assertions.assertEquals(2, content.size());
    }
}
