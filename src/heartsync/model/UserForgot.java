package heartsync.model;

public class UserForgot {
    private int id;
    private String username;
    private String password;
    private String favoriteColor;
    private String firstSchool;

    public UserForgot() {}

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
