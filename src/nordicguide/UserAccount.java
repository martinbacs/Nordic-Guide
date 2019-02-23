package nordicguide;

/**
 * Created by lovisafinnbck on 2017-04-11.
 */
public class UserAccount extends BasicAccount {

    private String userName;
    private long civicNumber;

    public UserAccount(String userName, String phoneNumber, String adress, String email, long civicNumber) {
        super(phoneNumber, adress, email);
        this.userName = userName;
        this.civicNumber = civicNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public long getCivicNumber() {
        return civicNumber;
    }

}
