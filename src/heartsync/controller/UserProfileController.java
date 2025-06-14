package heartsync.controller;

import java.util.List;

import heartsync.model.UserProfile;
import heartsync.view.MoreInfoView;

public class UserProfileController {
    private UserProfile model;
    private String currentUsername;
    private MoreInfoView moreInfoView;

    public UserProfileController(UserProfile model, String username) {
        this.model = model;
        this.currentUsername = username;
    }

    public UserProfile getModel() {
        return model;
    }

    public String getCurrentUsername() {
        return currentUsername;
    }

    // Basic info methods
    public void updateBasicInfo(String username, String fullName, int height, int weight, 
                              String country, String address, String phone, 
                              String qualification, String gender, String preferences, 
                              String aboutMe) {
        model.setFullName(fullName);
        model.setHeight(height);
        model.setWeight(weight);
        model.setCountry(country);
        model.setAddress(address);
        model.setPhoneNumber(phone);
        model.setQualification(qualification);
        model.setGender(gender);
        model.setPreferences(preferences);
        model.setAboutMe(aboutMe);
    }

    // Additional info methods
    public void updateAdditionalInfo(String relationshipGoal, String occupation,
                                   String religion, String ethnicity, List<String> languages,
                                   String dateOfBirth, String email) {
        model.setRelationshipGoal(relationshipGoal);
        model.setOccupation(occupation);
        model.setReligion(religion);
        model.setEthnicity(ethnicity);
        model.setLanguages(languages);
        model.setDateOfBirth(dateOfBirth);
        model.setEmail(email);
    }

    public void setProfilePicture(String profilePicPath) {
        model.setProfilePicPath(profilePicPath);
    }

    public void updateHobbies(List<String> hobbies) {
        model.setHobbies(hobbies);
    }

    public void setRelationChoice(String relation) {
        model.setRelationshipGoal(relation);
    }

    public void showMoreInfoView() {
        if (moreInfoView == null) {
            moreInfoView = new MoreInfoView(this);
        }
        moreInfoView.setVisible(true);
    }

    // Individual setters for all fields
    public void setFullName(String fullName) {
        model.setFullName(fullName);
    }

    public void setHeight(int height) {
        model.setHeight(height);
    }

    public void setWeight(int weight) {
        model.setWeight(weight);
    }

    public void setCountry(String country) {
        model.setCountry(country);
    }

    public void setAddress(String address) {
        model.setAddress(address);
    }

    public void setPhoneNumber(String phoneNumber) {
        model.setPhoneNumber(phoneNumber);
    }

    public void setQualification(String qualification) {
        model.setQualification(qualification);
    }

    public void setGender(String gender) {
        model.setGender(gender);
    }

    public void setPreferences(String preferences) {
        model.setPreferences(preferences);
    }

    public void setAboutMe(String aboutMe) {
        model.setAboutMe(aboutMe);
    }

    public void setRelationshipGoal(String relationshipGoal) {
        model.setRelationshipGoal(relationshipGoal);
    }

    public void setOccupation(String occupation) {
        model.setOccupation(occupation);
    }

    public void setReligion(String religion) {
        model.setReligion(religion);
    }

    public void setEthnicity(String ethnicity) {
        model.setEthnicity(ethnicity);
    }

    public void setLanguages(List<String> languages) {
        model.setLanguages(languages);
    }

    public void setDateOfBirth(String dateOfBirth) {
        model.setDateOfBirth(dateOfBirth);
    }

    public void setEmail(String email) {
        model.setEmail(email);
    }
}