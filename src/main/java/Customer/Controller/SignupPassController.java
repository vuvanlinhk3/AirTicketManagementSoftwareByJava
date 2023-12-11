package Customer.Controller;

import javafx.application.Application;
import javafx.stage.Stage;

public class SignupPassController  {
    private static String usernamedata;
    private static String genderdata;
    private static String nationalitydata;
    private static String languagedata;
    private static String addressdata;
    private static String countrydata;
    private static String provincesdata;
    private static String citydata;
    public static void setReceivedData(
            String nameuser,
            String gender,
            String nationality,
            String language,
            String address,
            String country,
            String provinces,
            String city
    ) {
        usernamedata = nameuser;
        genderdata = gender;
        nationalitydata = nationality;
        languagedata = language;
        addressdata = address;
        countrydata = country;
        provincesdata = provinces;
        citydata = city;
        System.out.println(usernamedata);
        System.out.println(genderdata);
        System.out.println(languagedata);
        System.out.println(countrydata);
        System.out.println(citydata);
    }

}
