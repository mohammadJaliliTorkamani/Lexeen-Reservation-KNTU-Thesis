package ir.ac.kntu.Entity;

public class LexinMarchant {
    private String marchantID;
    private String email;
    private String phone;

    public LexinMarchant(String marchantID, String email, String phone) {
        this.marchantID = marchantID;
        this.email = email;
        this.phone = phone;
    }

    public String getMarchantID() {
        return marchantID;
    }

    public void setMarchantID(String marchantID) {
        this.marchantID = marchantID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
