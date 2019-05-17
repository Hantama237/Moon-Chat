package com.hantama.climberschat;

public class User {

    private String id;
    private String username;
    private String imageURL;
    private String status;
    private String info;
    private String luck;


    public User(String id, String username, String imageURL, String status, String info/*,String luck*/){
        this.id=id;
        this.username=username;
        this.imageURL=imageURL;
        this.status=status;
        this.info=info;
        //this.luck=luck;
    }

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
    /*
    public String getLuck() {
        return luck;
    }

    public void setLuck(String luck) {
        this.luck = luck;
    }
    */
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
