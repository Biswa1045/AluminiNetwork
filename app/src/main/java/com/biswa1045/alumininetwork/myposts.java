package com.biswa1045.alumininetwork;

public class myposts {
    private String Caption;
    private String Post_id;
    private String Post_url;
    private String firestore_id;

    public myposts() {}

    public String getFirestore_id() {
        return firestore_id;
    }

    public void setFirestore_id(String firestore_id) {
        this.firestore_id = firestore_id;
    }

    public String getCaption() {
        return Caption;
    }

    public void setCaption(String caption) {
        Caption = caption;
    }

    public String getPost_id() {
        return Post_id;
    }

    public void setPost_id(String post_id) {
        Post_id = post_id;
    }

    public String getPost_url() {
        return Post_url;
    }

    public void setPost_url(String post_url) {
        Post_url = post_url;
    }
}
