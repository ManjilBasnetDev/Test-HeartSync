package heartsync.model;

import java.util.ArrayList;
import java.util.List;
import heartsync.database.DatabaseManagerProfile;
import heartsync.model.User;

public class UserProfile {
    private static UserProfile currentUser;
    
    // User details
    private String fullName;
    private int age;
    private String location;
    private String interests;
    private String bio;
    private String education;
    private String occupation;
    private String profilePicturePath;
    private int height;
    private int weight;
    private String country;
    private String address;
    private String phoneNumber;
    private String qualification;
    private String gender;
    private String preferences;
    private String aboutMe;
    private String profilePicPath;
    private List<String> hobbies;
    private String relationshipGoal;
    private String religion;
    private String ethnicity;
    private List<String> languages;
    private String dateOfBirth;
    private String email;

    // Default constructor
    public UserProfile() {
        this.hobbies = new ArrayList<>();
        this.languages = new ArrayList<>();
    }

    // Constructor with all fields
    public UserProfile(String fullName, int age, String location, String interests, 
                      String bio, String education, String occupation) {
        this.fullName = fullName;
        this.age = age;
        this.location = location;
        this.interests = interests;
        this.bio = bio;
        this.education = education;
        this.occupation = occupation;
    }

    // Static method to get current user
    public static UserProfile getCurrentUser() {
        if (currentUser == null) {
            try {
                // Get the currently logged in username from the User model
                String username = User.getCurrentUser().getUsername();
                
                // Load the profile from database
                DatabaseManagerProfile dbManager = DatabaseManagerProfile.getInstance();
                currentUser = dbManager.getUserProfile(username);
                
                // If no profile exists yet, create an empty one
                if (currentUser == null) {
                    currentUser = new UserProfile();
                }
            } catch (Exception e) {
                System.err.println("Error loading user profile: " + e.getMessage());
                currentUser = new UserProfile(); // Return empty profile on error
            }
        }
        return currentUser;
    }

    // Static method to set current user
    public static void setCurrentUser(UserProfile user) {
        currentUser = user;
    }

    // Getter methods
    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getLocation() {
        return location;
    }

    public String getInterests() {
        if (hobbies != null && !hobbies.isEmpty()) {
            return String.join(", ", hobbies);
        }
        return "";
    }

    public String getBio() {
        return bio;
    }

    public String getEducation() {
        return education;
    }

    public String getOccupation() {
        return occupation;
    }

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getQualification() {
        return qualification;
    }

    public String getGender() {
        return gender;
    }

    public String getPreferences() {
        return preferences;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public String getProfilePicPath() {
        return profilePicPath;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public String getRelationshipGoal() {
        return relationshipGoal;
    }

    public String getReligion() {
        return religion;
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public List<String> getLanguages() {
        return languages;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    // Setter methods
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setInterests(String interests) {
        this.interests = interests;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPreferences(String preferences) {
        this.preferences = preferences;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public void setProfilePicPath(String profilePicPath) {
        this.profilePicPath = profilePicPath;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public void setRelationshipGoal(String relationshipGoal) {
        this.relationshipGoal = relationshipGoal;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setEmail(String email) {
        this.email = email;
    }
} 