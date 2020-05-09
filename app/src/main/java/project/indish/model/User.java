package project.indish.model;

public class User {

    private String UID;
    private String name;
    private String email;
    private String password;
    private String image;

    public User() {

    }

    public User(String UID, String name, String email, String password, String image) {
        this.UID = UID;
        this.name = name;
        this.email = email;
        this.password = password;
        this.image = image == null ? "" : image;
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
