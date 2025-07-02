package heartsync.controller;

import java.util.List;

import heartsync.model.UserProfile;
import heartsync.view.MoreInfoView;
import heartsync.database.DatabaseManagerProfile;

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
                                String country, String address, String phoneNumber,
                                String education, String gender, String preferences,
                                String aboutMe) {
        try {
            UserProfile profile = getModel();
            profile.setUsername(username);
            profile.setFullName(fullName);
            profile.setHeight(height);
            profile.setWeight(weight);
            profile.setCountry(country);
            profile.setAddress(address);
            profile.setPhoneNumber(phoneNumber);
            profile.setEducation(education);
            profile.setGender(gender);
            profile.setPreferences(preferences);
            profile.setAboutMe(aboutMe);
            
            // Save to Firebase under user_details/{username}
            DatabaseManagerProfile.getInstance().saveUserProfile(profile);
            
            // Also save to the dating database format for compatibility
            heartsync.database.DatingDatabase datingDB = heartsync.database.DatingDatabase.getInstance();
            heartsync.dao.UserDAO userDAO = new heartsync.dao.UserDAO();
            heartsync.model.User user = userDAO.getUserByUsername(username);
            if (user != null) {
                // Update User object with profile data
                user.setFullName(fullName);
                user.setGender(gender);
                user.setBio(aboutMe);
                user.setPhoneNumber(phoneNumber);
                user.setProfilePictureUrl(profile.getProfilePicPath());
                datingDB.createOrUpdateUserProfile(user);
            }
            
            // Update the current user's profile in memory
            UserProfile.setCurrentUser(profile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update profile: " + e.getMessage());
        }
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

    public void setProfilePicture(String path) {
        try {
            UserProfile profile = getModel();
            profile.setProfilePicPath(path);
            
            // Save to Firebase
            DatabaseManagerProfile.getInstance().saveUserProfile(profile);
            
            // Update the current user's profile in memory
            UserProfile.setCurrentUser(profile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update profile picture: " + e.getMessage());
        }
    }

    public void setRelationChoice(String relationshipGoal) {
        try {
            UserProfile profile = getModel();
            profile.setRelationshipGoal(relationshipGoal);
            
            // Save to Firebase
            DatabaseManagerProfile.getInstance().saveUserProfile(profile);
            
            // Update the current user's profile in memory
            UserProfile.setCurrentUser(profile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update relationship goal: " + e.getMessage());
        }
    }

    public void setHobbies(List<String> hobbies) {
        try {
            UserProfile profile = getModel();
            profile.setHobbies(hobbies);
            
            // Save to Firebase
            DatabaseManagerProfile.getInstance().saveUserProfile(profile);
            
            // Also sync to dating database
            heartsync.database.DatingDatabase datingDB = heartsync.database.DatingDatabase.getInstance();
            heartsync.dao.UserDAO userDAO = new heartsync.dao.UserDAO();
            heartsync.model.User user = userDAO.getUserByUsername(currentUsername);
            if (user != null) {
                datingDB.createOrUpdateUserProfile(user);
            }
            
            // Update the current user's profile in memory
            UserProfile.setCurrentUser(profile);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to update hobbies: " + e.getMessage());
        }
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