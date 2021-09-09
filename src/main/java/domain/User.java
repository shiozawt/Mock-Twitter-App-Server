package domain;

import java.io.Serializable;
import java.util.Objects;


/**
 * Represents a user in the system.
 */
public class User implements Comparable<User>, Serializable {

    private String firstName;
    private String lastName;
    private String alias;
    private String imageUrl;
    private byte [] imageBytes;
    private int followingCount = 0;
    private int followerCount = 0;

    public User() {}

    public User(String firstName, String lastName, String imageUrl, int followingCount, int followerCount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.imageUrl = imageUrl;
        this.alias = String.format("@%s%s", firstName, lastName);
        this.followingCount = followingCount;
        this.followerCount = followerCount;
    }

    public User(String firstName, String lastName, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = String.format("@%s%s", firstName, lastName);
        this.imageUrl = imageURL;
    }

    public User(String firstName, String lastName, String alias, String imageURL) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.alias = alias;
        this.imageUrl = imageURL;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getName() {
        return String.format("%s %s", firstName, lastName);
    }

    public String getAlias() {
        return alias;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public byte [] getImageBytes() {
        return imageBytes;
    }

    public int getFollowingCount() {
        return this.followingCount;
    }

    public int getFollowerCount() {
        return this.followerCount;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
    }

    public void setFollowingCount(int numFollowing) {
        this.followingCount = numFollowing;
    }

    public void setFollowerCount(int numFollower) {
        this.followerCount = numFollower;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return alias.equals(user.alias);
    }

    @Override
    public int hashCode() {
        return Objects.hash(alias);
    }

    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", alias='" + alias + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    @Override
    public int compareTo(User user) {
        return this.getAlias().compareTo(user.getAlias());
    }
}
