package nordicguide;

/**
 * Created by lovisafinnbck on 2017-04-11.
 */
public abstract class BasicAccount {
    private String phoneNumber, adress, email;

    public BasicAccount(String phoneNumber, String adress, String email) {
        this.phoneNumber = phoneNumber;
        this.adress = adress;
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAdress() {
        return adress;
    }

    public String getEmail() {
        return email;
    }

}
