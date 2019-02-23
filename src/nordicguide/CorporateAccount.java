package nordicguide;

/**
 * Created by lovisafinnbck on 2017-04-11.
 */
public class CorporateAccount extends BasicAccount {
    private String companyName;
    private long ocrNr;

    public CorporateAccount(String companyName, long ocrNr, String adress, String phoneNumber, String email) {
        super(phoneNumber, adress, email);
        this.companyName = companyName;
        this.ocrNr = ocrNr;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public long getOcrNr() {
        return this.ocrNr;
    }
}
