package service;

import dao.*;
import domain.AuthToken;
import domain.FollowerQueueBatch;
import domain.Status;
import domain.User;
import service.proxyInterface.IFeedService;
import service.request.FeedRequest;
import service.response.FeedResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FeedServiceImpl implements IFeedService {

    @Override
    public FeedResponse getFeed(FeedRequest request) {

        FeedDAO fDao = getFeedDAO();
        UserDAO uDao = getUserDAO();

        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        //build lists
        fDao.getFeed(request);

        //get lists
        List<String> content = fDao.getContentList();
        List<String> timestamp = fDao.getTimestamp();
        List<String> posterAlias = fDao.getPosterAliasList();

        List<String> date = new ArrayList<String>();
        List<String> time = new ArrayList<String>();

        List<Status> statusList = new ArrayList<Status>();

        for (int j = 0; j < timestamp.size(); j++) {
            String timeInMilli = timestamp.get(j);
            DateFormat formatter = new SimpleDateFormat("MMM dd yyyy" + ", " +  "h:mm aa");
            long milliSeconds= Long.parseLong(timeInMilli);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(milliSeconds);
            String str = formatter.format((calendar.getTime()));

            String[] args = str.split(" ");
            date.add(args[0] + " " + args[1] + " " + args[2]);
            time.add(args[3] + " " + args[4]);
        }

        //build statuses
        for (int i = 0; i < content.size(); i++) {
            User user = uDao.getUser(posterAlias.get(i));
            Status status = new Status(user, date.get(i), time.get(i), content.get(i), parseMentions(content.get(i)));
            statusList.add(status);
        }

        //create list of statuses and return
        boolean hasMorePages = true;

        if (statusList.size() < 10){
            hasMorePages  = false;
        }

        FeedResponse response = new FeedResponse(statusList, hasMorePages);
        return response;
    }

    private ArrayList<String> parseMentions(String content) {
        ArrayList<String> mentions = new ArrayList<String>();
        StringBuilder user = new StringBuilder();

        for (int i = 0; i < content.length(); i++) {
            if (user.length() == 0 && content.charAt(i) == '@') {
                user.append(content.charAt(i));
            }
            else if (user.length() > 0 && content.charAt(i) != '@' && content.charAt(i) != ' ') {
                user.append(content.charAt(i));
            }
            else if (user.length() > 0 && content.charAt(i) == ' ') {
                //TODO: make a user table request to check if the alias exists
                User possibleUser = getUserDAO().getUser(user.toString());
                if (possibleUser != null) {
                    mentions.add(user.toString());
                }

                // clear the builder
                user = new StringBuilder();
            }
            else if (user.length() > 0 && content.charAt(i) == '@') {
                //TODO: make a user table request
                User possibleUser = getUserDAO().getUser(user.toString());
                if (possibleUser != null) {
                    mentions.add(user.toString());
                }

                // clear the builder
                user = new StringBuilder();
                user.append(content.charAt(i));
            }
        }

        if (user.length() > 0) {
            mentions.add(user.toString());
        }

        return mentions;
    }

    public void updateFeed(FollowerQueueBatch batch) {
        getFeedDAO().updateFeed(batch);
    }

    UserDAO getUserDAO(){ return new UserDAO(); }

    FeedDAO getFeedDAO(){ return new FeedDAO(); }
}
