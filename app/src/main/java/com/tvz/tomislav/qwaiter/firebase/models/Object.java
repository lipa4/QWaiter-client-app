package com.tvz.tomislav.qwaiter.firebase.models;

/**
 * Created by tomislav on 22/08/17.
 */

public class Object {

    private String objectAvatarImageURL;
    private String objectBackgroundImageURL;
    private String objectCategory;
    private String name;

    public Object(){

    }

    public Object(String objectAvatarImageURL, String objectBackgroundImageURL, String objectCategory, String name){
        this.objectAvatarImageURL=objectAvatarImageURL;
        this.objectBackgroundImageURL=objectBackgroundImageURL;
        this.objectCategory=objectCategory;
        this.name=name;
    }

    public String getObjectAvatarImageURL() {
        return objectAvatarImageURL;
    }

    public void setObjectAvatarImageURL(String objectAvatarImageURL) {
        this.objectAvatarImageURL = objectAvatarImageURL;
    }

    public String getObjectBackgroundImageURL() {
        return objectBackgroundImageURL;
    }

    public void setObjectBackgroundImageURL(String objectBackgroundImageURL) {
        this.objectBackgroundImageURL = objectBackgroundImageURL;
    }

    public String getObjectCategory() {
        return objectCategory;
    }

    public void setObjectCategory(String objectCategory) {
        this.objectCategory = objectCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
