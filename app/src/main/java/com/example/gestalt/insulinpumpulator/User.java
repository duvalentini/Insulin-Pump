package com.example.gestalt.insulinpumpulator;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

/**
 * Created by Ma on 6/17/2016.
 */
@DynamoDBTable(tableName = "Users")
public class User {

    private String username;
    private String email;
    private String name;
    private String identityID;
    private String soundFile;

    @DynamoDBHashKey(attributeName = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBHashKey(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBHashKey(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBHashKey(attributeName = "IdentityID")
    public String getIdentityID() {
        return identityID;
    }

    public void setIdentityID(String identityID) {
        this.identityID = identityID;
    }

    @DynamoDBHashKey(attributeName = "SoundFile")
    public String getSoundFile() { return soundFile; }

    public void setSoundFile(String soundFile) { this.soundFile = soundFile; }

}

