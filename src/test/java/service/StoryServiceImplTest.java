package service;

import dao.StoryDAO;
import domain.Status;
import domain.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import service.request.StoryRequest;
import service.response.StoryResponse;

import java.util.Arrays;
import java.util.List;

public class StoryServiceImplTest {

    private StoryRequest request;
    private StoryResponse expectedResponse;
    private StoryDAO mockStoryDAO;
    private StoryServiceImpl storyServiceImplSpy;

    @BeforeEach
    public void setup() {
        User currentUser = new User("FirstName", "LastName", null);

        User resultUser1 = new User("FirstName1", "LastName1",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/donald_duck.png");
        User resultUser2 = new User("FirstName2", "LastName2",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");
        User resultUser3 = new User("FirstName3", "LastName3",
                "https://faculty.cs.byu.edu/~jwilkerson/cs340/tweeter/images/daisy_duck.png");

        Status status1 = new Status(resultUser1,"Jan 1","6pm","TEST MESSAGE1", null);
        Status status2 = new Status(resultUser2,"Jan 2","7pm","TEST MESSAGE2", null);
        Status status3 = new Status(resultUser3,"Jan 3","8pm","TEST MESSAGE3", null);

        // Setup a request object to use in the tests
        request = new StoryRequest(currentUser.getAlias(), 3, null,"token");

        // Setup a mock StoryDAO that will return known responses
        expectedResponse = new StoryResponse(Arrays.asList(status1,status2,status3), false);
        mockStoryDAO = Mockito.mock(StoryDAO.class);
        //Mockito.when(mockStoryDAO.getStory(request)).thenReturn(expectedResponse);

        storyServiceImplSpy = Mockito.spy(StoryServiceImpl.class);
        Mockito.when(storyServiceImplSpy.getStoryDAO()).thenReturn(mockStoryDAO);

        mockStoryDAO.getStory(request);
        List<String> list = mockStoryDAO.getContentList();

        Assertions.assertNull(list);
    }
}