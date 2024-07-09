package com.yo1000.api.presentation;

import java.util.Optional;

public class UserProfilePatchRequest {
    private Optional<Integer> id;
    private Optional<String> username;
    private Optional<String> familyName;
    private Optional<String> givenName;
    private Optional<Integer> age;
    private Optional<String> gender;
    private Optional<String> profile;

    public UserProfilePatchRequest() {}

    public UserProfilePatchRequest(
            Optional<Integer> id, Optional<String> username,
            Optional<String> familyName, Optional<String> givenName,
            Optional<Integer> age, Optional<String> gender, Optional<String> profile
    ) {
        this.id = id;
        this.username = username;
        this.familyName = familyName;
        this.givenName = givenName;
        this.age = age;
        this.gender = gender;
        this.profile = profile;
    }

    public Optional<Integer> getId() {
        return id;
    }

    public void setId(Optional<Integer> id) {
        this.id = id;
    }

    public Optional<String> getUsername() {
        return username;
    }

    public void setUsername(Optional<String> username) {
        this.username = username;
    }

    public Optional<String> getFamilyName() {
        return familyName;
    }

    public void setFamilyName(Optional<String> familyName) {
        this.familyName = familyName;
    }

    public Optional<String> getGivenName() {
        return givenName;
    }

    public void setGivenName(Optional<String> givenName) {
        this.givenName = givenName;
    }

    public Optional<Integer> getAge() {
        return age;
    }

    public void setAge(Optional<Integer> age) {
        this.age = age;
    }

    public Optional<String> getGender() {
        return gender;
    }

    public void setGender(Optional<String> gender) {
        this.gender = gender;
    }

    public Optional<String> getProfile() {
        return profile;
    }

    public void setProfile(Optional<String> profile) {
        this.profile = profile;
    }
}
