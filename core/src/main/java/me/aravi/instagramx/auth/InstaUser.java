package me.aravi.instagramx.auth;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

@Keep
public class InstaUser {

    private String username;

    @Nullable
    private String fullName;

    private long userId;

    @Nullable
    private String biography;

    private String profilePicUrl;

    private boolean isPrivate;

    private long loggedInAt;

    @Nullable
    private String csrfToken;

    private String cookie;

    private String roll_hash;

    public InstaUser() {

    }

    public InstaUser(String username, @Nullable String fullName, long userId, @Nullable String biography, String profilePicUrl, boolean isPrivate, long loggedInAt, @Nullable String csrfToken, String cookie, String roll_hash) {
        this.username = username;
        this.fullName = fullName;
        this.userId = userId;
        this.biography = biography;
        this.profilePicUrl = profilePicUrl;
        this.isPrivate = isPrivate;
        this.loggedInAt = loggedInAt;
        this.csrfToken = csrfToken;
        this.cookie = cookie;
        this.roll_hash = roll_hash;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public String getFullName() {
        return fullName;
    }

    public void setFullName(@Nullable String fullName) {
        this.fullName = fullName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Nullable
    public String getBiography() {
        return biography;
    }

    public void setBiography(@Nullable String biography) {
        this.biography = biography;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public long getLoggedInAt() {
        return loggedInAt;
    }

    public void setLoggedInAt(long loggedInAt) {
        this.loggedInAt = loggedInAt;
    }

    @Nullable
    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(@Nullable String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getCookie() {
        return cookie;
    }

    public void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public String getRoll_hash() {
        return roll_hash;
    }

    public void setRoll_hash(String roll_hash) {
        this.roll_hash = roll_hash;
    }


    @NonNull
    @Override
    public String toString() {
        return "Username:" + getUsername() +
                " UserID:" + getUserId() +
                " CSRFToken:" + getCsrfToken() +
                " RollHash:" + getRoll_hash() +
                " FullName:" + getFullName() +
                " Cookie:" + getCookie();
    }
}
