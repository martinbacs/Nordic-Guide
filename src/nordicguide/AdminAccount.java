package nordicguide;

/**
 * Created by lovisafinnbck on 2017-04-11.
 */
public class AdminAccount extends BasicAccount {
    private String employeeName;
    private long civicNumber;

    public AdminAccount(String employeeName, long civicNumber, String phoneNumber, String adress, String email) {
        super(phoneNumber, adress, email);
        this.civicNumber = civicNumber;
        this.employeeName = employeeName;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public long getCivicNumber() {
        return civicNumber;
    }
}
