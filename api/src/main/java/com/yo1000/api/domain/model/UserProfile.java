package com.yo1000.api.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class UserProfile {
    @Id
    private Integer id;
    private String username;
    private String familyName;
    private String givenName;
    private Integer age;
    private String gender;
    private String profile;

    public UserProfile() {}

    public UserProfile(Integer id, String username, String familyName, String givenName, Integer age, String gender, String profile) {
        this.id = id;
        this.username = username;
        this.familyName = familyName;
        this.givenName = givenName;
        this.age = age;
        this.gender = gender;
        this.profile = profile;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFamilyName() {
        return familyName;
    }

    public void setFamilyName(String familyName) {
        this.familyName = familyName;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }
}
