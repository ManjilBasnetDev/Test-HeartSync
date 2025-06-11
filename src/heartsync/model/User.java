package heartsync.model;

import java.time.LocalDate;

public class User {
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
    private String favoriteColor;
    private String firstSchool;
    
    // Default constructor
    public User() {}
    
    // Constructor for basic user creation
    public User(String username, String password, String userType) {
        this.username = username;
        this.password = password;
        this.userType = userType;
    }
    
    // Full constructor
    public User(int id, String username, String password, String userType, 
                String email, String phoneNumber, LocalDate dateOfBirth, 
                String gender, String interests, String bio,
                String favoriteColor, String firstSchool) {
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
        this.favoriteColor = favoriteColor;
        this.firstSchool = firstSchool;
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
}