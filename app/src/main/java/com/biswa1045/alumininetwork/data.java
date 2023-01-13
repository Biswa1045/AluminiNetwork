package com.biswa1045.alumininetwork;

public class data {
    String NAME;
    String EMAIL;
    String BATCH;
    String BRANCH;
    String ADDRESS;
    String GENDER;

        public data() {
        }

        public data(String NAME, String EMAIL, String BATCH, String BRANCH,String ADDRESS,String GENDER) {
            this.NAME = NAME;
            this.EMAIL = EMAIL;
            this.BATCH = BATCH;
            this.BRANCH = BRANCH;
            this.ADDRESS = ADDRESS;
            this.GENDER = GENDER;
        }

    public String getNAME() {
        return NAME;
    }

    public void setNAME(String NAME) {
        this.NAME = NAME;
    }

    public String getEMAIL() {
        return EMAIL;
    }

    public void setEMAIL(String EMAIL) {
        this.EMAIL = EMAIL;
    }

    public String getBATCH() {
        return BATCH;
    }

    public void setBATCH(String BATCH) {
        this.BATCH = BATCH;
    }

    public String getBRANCH() {
        return BRANCH;
    }

    public void setBRANCH(String BRANCH) {
        this.BRANCH = BRANCH;
    }

    public String getADDRESS() {
        return ADDRESS;
    }

    public void setADDRESS(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    public String getGENDER() {
        return GENDER;
    }

    public void setGENDER(String GENDER) {
        this.GENDER = GENDER;
    }
}
