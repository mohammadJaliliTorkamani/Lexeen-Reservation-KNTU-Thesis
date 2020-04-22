package ir.ac.kntu.Entity;

public class NormalUser {
    private String name;
    private String lastName;
    private String phone;
    private String profilePhoto;
    private String address;
    private String pusheID;
    private String password;
    private boolean male;
    private double cash;

    public NormalUser(String name, String lastName, String phone, String profilePhoto, String address, double cash) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.address = address;
        this.cash = cash;
    }

    public NormalUser(String name, String lastName, String phone, String profilePhoto, String address) {
        this.name = name;
        this.lastName = lastName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.address = address;
    }

    public NormalUser(String name, String lastName, String phone, String profilePhoto, String address, String pusheID, String password, double cash, boolean male) {
        this.name = name;
        this.male = male;
        this.lastName = lastName;
        this.phone = phone;
        this.profilePhoto = profilePhoto;
        this.address = address;
        this.pusheID = pusheID;
        this.password = password;
        this.cash = cash;
    }

    public String getPusheID() {
        return pusheID;
    }

    public void setPusheID(String pusheID) {
        this.pusheID = pusheID;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getCash() throws Exception {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public boolean isMale() throws Exception {
        return male;
    }

    public void setMale(boolean male) {
        this.male = male;
    }
}
