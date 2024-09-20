package com.example.twf_final.dataBase.entity;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "users")
public class UserEntity  {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String mobileNumber;
    private String emailAddress;
    private String name;
    private String lastName;
    private String profilePicturePath;


    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public UserEntity(int id, String mobileNumber, String emailAddress, String name, String lastName, String profilePicturePath) {
        this.id = id;
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.name = name;
        this.lastName = lastName;
        this.profilePicturePath = profilePicturePath;
    }

    public UserEntity(String mobileNumber, String emailAddress, String name, String lastName,String profilePicturePath) {
        this.mobileNumber = mobileNumber;
        this.emailAddress = emailAddress;
        this.name = name;
        this.lastName = lastName;
        this.profilePicturePath = profilePicturePath;
    }

    public UserEntity() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


}
