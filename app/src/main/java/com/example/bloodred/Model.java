package com.example.bloodred;

public class Model {

    String bloodgroup;
    String name;
    String type;
    String email;
    String profilepicture;

    public Model() {
    }

    public Model(String bloodgroup, String name, String type, String profilepicture) {
        this.bloodgroup = bloodgroup;
        this.name = name;
        this.type = type;
        this.email = email;
        this.profilepicture = profilepicture;
    }

    public String getBloodgroup() {
        return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
        this.bloodgroup = bloodgroup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }
}
