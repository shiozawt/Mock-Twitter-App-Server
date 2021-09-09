package service;

import dao.AuthDAO;
import dao.StoryDAO;
import dao.UserDAO;
import domain.AuthToken;
import domain.Status;
import domain.User;
import service.proxyInterface.IStoryService;
import service.request.StoryRequest;
import service.response.StoryResponse;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class StoryServiceImpl implements IStoryService {
    @Override
    public StoryResponse getStory(StoryRequest request) {

        StoryDAO sDao = getStoryDAO();
        UserDAO uDao = getUserDAO();

        if(!new AuthDAO().validate(new AuthToken(request.token))){
            throw new RuntimeException("401 : Missing or Invalid Token");
        }

        //build lists
        sDao.getStory(request);

        //get lists
        List<String> content = sDao.getContentList();
        List<String> timestamp = sDao.getTimestamp();

        List<String> date = new ArrayList<String>();
        List<String> time = new ArrayList<String>();

        List<Status> statusList = new ArrayList<Status>();

        User user = uDao.getUser(request.getUserAlias());

        for(int j = 0; j < timestamp.size(); j++){
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
        for (int i = 0; i < content.size(); i++){
            Status status = new Status(user, date.get(i), time.get(i), content.get(i), parseMentions(content.get(i)));
            statusList.add(status);
        }

        //create list of statuses and return
        boolean hasMorePages = true;

        if (statusList.size() < 10){
            hasMorePages  = false;
        }

        StoryResponse response = new StoryResponse(statusList, hasMorePages);
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

    UserDAO getUserDAO(){ return new UserDAO(); }

    StoryDAO getStoryDAO(){ return new StoryDAO(); }
}
