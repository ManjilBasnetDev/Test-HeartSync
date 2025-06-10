package heartsync.controller;

import heartsync.model.UserProfile;
import heartsync.view.MoreInfoView;
import java.util.List;

public class UserProfileController {
    private UserProfile model;
    private MoreInfoView moreInfoView;
    private int userId;

    public UserProfileController(UserProfile model) {
        this.model = model;
    }

    public UserProfile getModel() {
        return model;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

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

    public void setProfilePicPath(String profilePicPath) {
        model.setProfilePicPath(profilePicPath);
    }

    public void updateHobbies(List<String> hobbies) {
        model.setHobbies(hobbies);
    }

    public void setRelationChoice(String relationChoice) {
        // This will be stored in the database but not in the model
        // as it's only needed during profile creation
    }

    // Add missing methods
    public void updateBasicInfo(String fullName, int height, int weight, String country,
                              String address, String phone, String qualification,
                              String gender, String preferences, String aboutMe) {
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

    public void showMoreInfoView() {
        if (moreInfoView == null) {
            moreInfoView = new MoreInfoView(this, userId);
        }
        moreInfoView.setVisible(true);
    }

    public void setProfilePicture(String path) {
        model.setProfilePicPath(path);
    }
} 