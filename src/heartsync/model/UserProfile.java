package heartsync.model;

import java.util.ArrayList;
import java.util.List;

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
            // TODO: In the future, load this from database
            // For now, return dummy data for testing
            currentUser = new UserProfile();
            currentUser.setFullName("John Doe");
            currentUser.setHeight(175);
            currentUser.setWeight(70);
            currentUser.setCountry("Nepal");
            currentUser.setAddress("Kathmandu");
            currentUser.setPhoneNumber("+977 9876543210");
            currentUser.setQualification("Bachelor's Degree");
            currentUser.setGender("Male");
            currentUser.setPreferences("Women");
            currentUser.setAboutMe("Looking for meaningful connections and shared adventures.");
            currentUser.setProfilePicPath("/path/to/profile.jpg");
            currentUser.setRelationshipGoal("Long-term Relationship");
            currentUser.setOccupation("Software Developer");
            currentUser.setReligion("Hindu");
            currentUser.setEthnicity("Asian");
            
            List<String> languages = new ArrayList<>();
            languages.add("English");
            languages.add("Nepali");
            currentUser.setLanguages(languages);
            
            List<String> hobbies = new ArrayList<>();
            hobbies.add("Reading");
            hobbies.add("Hiking");
            hobbies.add("Photography");
            currentUser.setHobbies(hobbies);
            
            currentUser.setDateOfBirth("1998-01-01");
            currentUser.setEmail("john.doe@example.com");
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