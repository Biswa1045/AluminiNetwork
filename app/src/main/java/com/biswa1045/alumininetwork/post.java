package com.biswa1045.alumininetwork;

public class post {
    String URL;
    String CAPTION;

    String UPLOADER_IMG;
    String UPLOADER_NAME;
    String UPLOADER_UID;
    String POST_ID;
    String POST_TIME;
    String POST_TYPE;


        public post() {
        }

        public post(String URL, String CAPTION, String UPLOADER_IMG, String UPLOADER_NAME, String UPLOADER_UID,String POST_ID,String POST_TIME,String POST_TYPE) {
            this.URL = URL;
            this.CAPTION = CAPTION;
            this.UPLOADER_IMG = UPLOADER_IMG;
            this.UPLOADER_NAME = UPLOADER_NAME;
            this.UPLOADER_UID = UPLOADER_UID;
            this.POST_ID = POST_ID;
            this.POST_TIME=POST_TIME;
            this.POST_TYPE=POST_TYPE;
        }

    public String getPOST_TIME() {
        return POST_TIME;
    }

    public void setPOST_TIME(String POST_TIME) {
        this.POST_TIME = POST_TIME;
    }

    public String getPOST_TYPE() {
        return POST_TYPE;
    }

    public void setPOST_TYPE(String POST_TYPE) {
        this.POST_TYPE = POST_TYPE;
    }

    public String getPOST_ID() {
        return POST_ID;
    }

    public void setPOST_ID(String POST_ID) {
        this.POST_ID = POST_ID;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getCAPTION() {
        return CAPTION;
    }

    public void setCAPTION(String CAPTION) {
        this.CAPTION = CAPTION;
    }



    public String getUPLOADER_IMG() {
        return UPLOADER_IMG;
    }

    public void setUPLOADER_IMG(String UPLOADER_IMG) {
        this.UPLOADER_IMG = UPLOADER_IMG;
    }

    public String getUPLOADER_NAME() {
        return UPLOADER_NAME;
    }

    public void setUPLOADER_NAME(String UPLOADER_NAME) {
        this.UPLOADER_NAME = UPLOADER_NAME;
    }

    public String getUPLOADER_UID() {
        return UPLOADER_UID;
    }

    public void setUPLOADER_UID(String UPLOADER_UID) {
        this.UPLOADER_UID = UPLOADER_UID;
    }
}
