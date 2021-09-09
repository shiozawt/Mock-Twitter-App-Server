package dao;

import domain.AuthToken;
import domain.Status;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.request.FeedRequest;
import service.response.FeedResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FeedDAOTest {

    @Test
    void FeedTest() {

        FeedDAO fDao = new FeedDAO();
        UserDAO uDao = new UserDAO();

        FeedRequest request = new FeedRequest("@server_test_user", 10, null, "token");

        //build list
        fDao.getFeed(request);

        //get list
        List<String> posterAlias = fDao.getPosterAliasList();

        Assertions.assertEquals(posterAlias.get(0), "@1");
        Assertions.assertEquals(posterAlias.get(1), "@2");
        Assertions.assertEquals(2, posterAlias.size());
    }

}
