package model;

public class User {
    String username;
    int id;
    Boolean is_admin;
    String token;

    public User(String username, int id, Boolean is_admin, String token) {
        this.username = username;
        this.id = id;
        this.is_admin = is_admin;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Boolean getIs_admin() {
        return is_admin;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return this.getId()+" "+this.getIs_admin()+" "+getUsername()+" "+getToken();
    }


}
