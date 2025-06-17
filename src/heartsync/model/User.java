// User.java - Model class for User entity
package heartsync.model;

import java.util.Date;

public class User {
    private static User currentUser;
    
    private int id;
    private String username;
    private String password;
    private String email;
    private String userType;
    private Date createdAt;
    
    // Additional fields
    private String phoneNumber;
    private String dateOfBirth;
    private String gender;
    private String interests;
    private String bio;
    private String favoriteColor;
    private String firstSchool;
    private String fullName;
    
    // Static methods for current user management
    public static User getCurrentUser() {
        return currentUser;
    }
    
    public static void setCurrentUser(User user) {
        currentUser = user;
    }
    
    // Default constructor
    public User() {
    }
    
    // Full constructor with all fields
    public User(int id, String username, String password, String email, String userType, 
               String phoneNumber, String dateOfBirth, String gender, 
               String interests, String bio, String favoriteColor, String firstSchool) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.userType = userType;
        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.interests = interests;
        this.bio = bio;
        this.favoriteColor = favoriteColor;
        this.firstSchool = firstSchool;
        this.createdAt = new Date();
    }
    
    // Constructor with parameters
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.createdAt = new Date();
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
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Date getCreatedAt() {
        return createdAt;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
    
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    
    // Additional getters and setters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    // Overload to accept LocalDate
    public void setDateOfBirth(java.time.LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth != null ? dateOfBirth.toString() : null;
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
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        User user = (User) obj;
        return id == user.id;
    }
    
    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}