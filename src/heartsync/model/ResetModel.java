package heartsync.model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.Period;

public class ResetModel {
    private int id;
    private String username;
    private String password;
    private String userType;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String interests;
    private String bio;
    private String securityQuestion;
    private String securityAnswer;
    private String favoriteColor;
    private String firstSchool;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    
    // Default constructor
    public ResetModel() {}
    
    // Constructor for basic user creation
    public ResetModel(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    // Full constructor
    public ResetModel(int id, String username, String password, String userType, 
                String email, String phoneNumber, LocalDate dateOfBirth, 
                String gender, String interests, String bio) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.userType = userType;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.interests = interests;
        this.bio = bio;
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getUserType() {
        return userType;
    }
    
    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public String getGender() {
        return gender;
    }
    
    public void setGender(String gender) {
        this.gender = gender;
    }
    
    public String getInterests() {
        return interests;
    }
    
    public void setInterests(String interests) {
        this.interests = interests;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public String getSecurityQuestion() {
        return securityQuestion;
    }
    
    public void setSecurityQuestion(String securityQuestion) {
        this.securityQuestion = securityQuestion;
    }
    
    public String getSecurityAnswer() {
        return securityAnswer;
    }
    
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }
    
    public String getFavoriteColor() {
        return favoriteColor;
    }
    
    public void setFavoriteColor(String favoriteColor) {
        this.favoriteColor = favoriteColor;
    }
    
    public String getFirstSchool() {
        return firstSchool;
    }
    
    public void setFirstSchool(String firstSchool) {
        this.firstSchool = firstSchool;
    }
    
    public Timestamp getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
    
    public Timestamp getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper method to check if user is 18+
    public boolean isAdult() {
        if (dateOfBirth == null) {
            return false;
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 18;
    }
    
    @Override
    public String toString() {
        return "User{" + "id=" + id + 
               ", username='" + username + '\'' +
               ", userType='" + userType + '\'' +
               ", email='" + email + '\'' +
               ", phoneNumber='" + phoneNumber + '\'' +
               ", dateOfBirth='" + dateOfBirth + '\'' +
               ", gender='" + gender + '\'' +
               '}';
    }
} 