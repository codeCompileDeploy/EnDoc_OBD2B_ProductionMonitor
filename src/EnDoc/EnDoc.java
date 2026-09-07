package EnDoc;

import com.DatabaseManagement.Create_Tables;
import com.Licensing.checkLicense;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 *
 * @author IamDe
 */
public class EnDoc {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Add Licensing Here IP Address
        Create_Tables tab = new Create_Tables();
        tab.create_Tables();
        checkLicense();
    }

    public static void checkLicense() {
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Kolkata"));
        Calendar cal = new GregorianCalendar();
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int month = cal.get(Calendar.MONTH);
        int year = cal.get(Calendar.YEAR);
        checkLicense var = new checkLicense();
        var.check(day, month + 1, year);
    }

}
