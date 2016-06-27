package com.example.gestalt.insulinpumpulator;

import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBAttribute;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBHashKey;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBTable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ma on 6/17/2016.
 */
@DynamoDBTable(tableName = "Users")
public class User {

    private String username;
    private String email;
    private String name;
    private String identityID;
    private String pumpType;
    private float targetBloodSugar;
    private float insulinCarbRatio;
    private float activeInsulinTime;
    private float sensitivity;
    private List<Float> basalRates;
    private String soundFile;

    @DynamoDBHashKey(attributeName = "IdentityID")
    public String getIdentityID() {
        return identityID;
    }

    public void setIdentityID(String identityID) {
        this.identityID = identityID;
    }

    @DynamoDBAttribute(attributeName = "Username")
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @DynamoDBAttribute(attributeName = "Email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @DynamoDBAttribute(attributeName = "Name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @DynamoDBAttribute(attributeName = "PumpType")
    public String getPumpType() {
        return pumpType;
    }

    public void setPumpType(String pumpType) { this.pumpType = pumpType; }

    @DynamoDBAttribute(attributeName = "TargetBloodSugar")
    public float getTargetBloodSugar() { return targetBloodSugar; }

    public void setTargetBloodSugar(float targetBloodSugar) { this.targetBloodSugar = targetBloodSugar; }

    @DynamoDBAttribute(attributeName = "InsulinCarbRatio")
    public float getInsulinCarbRatio() { return insulinCarbRatio; }

    public void setInsulinCarbRatio(float insulinCarbRatio) { this.insulinCarbRatio = insulinCarbRatio; }

    @DynamoDBAttribute(attributeName = "ActiveInsulinTime")
    public float getActiveInsulinTime() { return activeInsulinTime; }

    public void setActiveInsulinTime(float activeInsulinTime) { this.activeInsulinTime = activeInsulinTime; }

    @DynamoDBAttribute(attributeName = "Sensitivity")
    public float getSensitivity() { return sensitivity; }

    public void setSensitivity(float sensitivity) { this.sensitivity = sensitivity; }

    @DynamoDBAttribute(attributeName = "BasalRates")
    public List<Float> getBasalRates() { return basalRates; }

    public void setBasalRates(List<Float> basalRates) { this.basalRates = basalRates; }

    @DynamoDBAttribute(attributeName = "SoundFile")
    public String getSoundFile() { return soundFile; }

    public void setSoundFile(String soundFile) { this.soundFile = soundFile; }

}

