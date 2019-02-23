package nordicguide;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by marbac on 4/12/2017.
 */
public class NordicDatabase {

    private String url = "jdbc:mysql://127.0.0.1:3306/NordicGuideDataBase?user=root&password=root";
    private Statement statement;


    public NordicDatabase() {
        try {
            Connection connection = (Connection) DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<String> getAllCountries() {
        ArrayList<String> countries = new ArrayList<String>();
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT CountryName FROM country");
            while (result.next()) {
                countries.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countries;

    }

    public String getCountryCode(String country) {
        String countryCode = "";
        try {
            ResultSet rs = statement.executeQuery("SELECT CountryCode FROM country WHERE CountryName = '" + country + "'");
            rs.next();
            countryCode = rs.getString(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return countryCode;
    }

    public ArrayList<String> searchForRegion(String region) {
        ArrayList<String> results = new ArrayList<String>();
        try {

            ResultSet result = statement.executeQuery(
                    "SELECT CountryName FROM country WHERE CountryName LIKE '" + region + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            result = statement.executeQuery(
                    "SELECT CityName FROM city WHERE CityName Like '" + region + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> searchForAttractions(String attraction) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT hotelName FROM hotels WHERE hotelName LIKE '%" + attraction + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            result = statement.executeQuery(
                    "SELECT restaurantsName FROM restaurants WHERE restaurantsName LIKE '%" + attraction + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            result = statement.executeQuery(
                    "SELECT museumName FROM museums WHERE museumName LIKE '%" + attraction + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            result = statement.executeQuery(
                    "SELECT sightName FROM sights WHERE sightName LIKE '%" + attraction + "%'");
            while (result.next()) {
                results.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> searchForAttractionsInRegion(String region, String attraction) {
        ArrayList<String> results = new ArrayList<>();
        String city = null;
        String country = null;

        try {
            if (!isACity(region) && !isACountry(region)) {
                ResultSet result = statement.executeQuery(
                        "SELECT CountryName FROM country WHERE CountryName LIKE '" + region + "%'");
                if (result.next()) {
                    country = result.getString(1);
                    System.out.println(country);
                }

                // while (result.next()) {
                //   results.add(result.getString(1));
                //}
                result = statement.executeQuery(
                        "SELECT CityName FROM city WHERE CityName Like '" + region + "%'");
                if (result.next()) {
                    city = result.getString(1);
                    System.out.println(city);
                }
                // while (result.next()) {
                //   results.add(result.getString(1));

                // }
            } else if (isACity(region)) {
                city = region;
            } else if (isACountry(region)) {
                country = region;
            }
            if (country != null) {
                ResultSet result = statement.executeQuery(
                        "SELECT countryCode FROM country WHERE countryName like '" + country + "%'");
                result.next();
                result = statement.executeQuery(
                        "SELECT hotelName FROM hotels WHERE City_Country_countryCode = '" + result.getString(1) + "'" +
                                "AND hotelName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT restaurantsName FROM restaurants WHERE City_Country_countryCode ='" + result.getString(1) + "'" +
                                "AND restaurantsName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT museumName FROM museums WHERE City_Country_countryCode = '" + result.getString(1) + "'" +
                                "AND museumName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT sightName FROM sights WHERE City_Country_countryCode ='" + result.getString(1) + "'" +
                                "AND sightName LIKE '%" + attraction + "%'");
                while (result.next()) {
                    results.add(result.getString(1));
                }
            }
            if (city != null) {
                ResultSet result = statement.executeQuery(
                        "SELECT cityCode FROM city WHERE cityName like '" + city + "%'");
                result.next();
                result = statement.executeQuery(
                        "SELECT hotelName FROM hotels WHERE City_cityCode ='" + result.getString(1) + "'" +
                                "AND hotelName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT restaurantsName FROM restaurants WHERE City_cityCode ='" + result.getString(1) + "'" +
                                "AND restaurantsName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT museumName FROM museums WHERE City_cityCode = '" + result.getString(1) + "'" +
                                "AND museumName LIKE '%" + attraction + "%'" +
                                "UNION " +
                                "SELECT sightName FROM sights WHERE City_cityCode ='" + result.getString(1) + "'" +
                                "AND sightName LIKE '%" + attraction + "%'");
                while (result.next()) {
                    results.add(result.getString(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;

    }

    public void addPrivateUser(String privateUserName, String privateUserPhone, String privateUserAddress, String privateUserEmail,
                               int privateUserCivicNumber, String privateUserPassword) {
        try {
            statement.executeUpdate("INSERT INTO privateuser " +
                    "VALUES ('" + privateUserName + "', '" + privateUserPhone + "', '" + privateUserAddress + "', '" +
                    privateUserEmail + "', " + privateUserCivicNumber + ", '" + privateUserPassword + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCorporateUser(String companyUserName, String companyUserPhone, String companyUserAddress, String companyUserEmail,
                                 long companyUserOCR, String companyUserPassword) {
        try {
            statement.executeUpdate("INSERT INTO corporateuser " +
                    "VALUES ('" + companyUserName + "', " + companyUserOCR + ", '" + companyUserAddress + "', '" +
                    companyUserPhone + "', '" + companyUserEmail + "', '" + companyUserPassword + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String handleLogIn(String email, String password) {
        try {
            ResultSet result = statement.executeQuery("SELECT `PrivateUserE-mail` " +
                    "FROM privateuser " +
                    "WHERE `PrivateUserE-mail` = '" + email + "'");
            while (result.next()) {
                if (result.getString(1).equals(email)) {
                    result = statement.executeQuery("SELECT PrivateUserPassword " +
                            "FROM privateuser " +
                            "WHERE PrivateUserPassword = '" + password + "'");
                    while (result.next()) {
                        if (result.getString(1).equals(password)) {
                            return "UserAccount";
                        }
                    }
                }
            }
            result = statement.executeQuery(
                    "SELECT `CompanyE-mail` " +
                            "FROM corporateuser " +
                            "WHERE `CompanyE-mail` ='" + email + "'");
            while (result.next()) {
                if (result.getString(1).equals(email)) {
                    result = statement.executeQuery(
                            "SELECT CorporateUserPassword " +
                                    "FROM corporateuser " +
                                    "WHERE CorporateUserPassword ='" + password + "'");
                    while (result.next()) {
                        if (result.getString(1).equals(password)) {
                            return "CorporateAccount";
                        }
                    }
                }
            }
            result = statement.executeQuery("SELECT `e-mail` FROM admin " +
                    "WHERE `e-mail` = '" + email + "'");
            while (result.next()) {
                if (result.getString(1).equals(email)) {
                    result = statement.executeQuery("SELECT Password FROM admin " +
                            "WHERE Password= '" + password + "'");
                    while (result.next()) {
                        if (result.getString(1).equals(password)) {
                            return "AdminAccount";
                        }
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }

    public ArrayList<String> getPrivateUser(String email) {
        ArrayList<String> userInfo = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * " +
                    "FROM privateuser " +
                    "WHERE `PrivateUserE-mail` = '" + email + "'");
            rs.next();
            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                userInfo.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public String getPassword(String email) {
        String password = null;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT privateUserPassword" +
                            " FROM privateUser" +
                            " WHERE `PrivateUserE-mail` = '" + email + "'");
            result.next();
            password = result.getString(1);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return password;
    }

    public void updatePrivateUserInfo(String phone, String address, String email, String password, String userName) {
        try {
            statement.executeUpdate(
                    "UPDATE privateUser" +
                            " SET PrivateUserPhone= '" + phone + "' , PrivateUserAddress= '" + address + "' , `PrivateUserE-mail`= '" + email + "' , PrivateUserPassword= '" + password + "'" +
                            " WHERE PrivateUserName= '" + userName + "'");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ArrayList<String> getCorporateUser(String email) {
        ArrayList<String> corpInfo = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * " +
                    "FROM corporateuser " +
                    "WHERE `CompanyE-mail` = '" + email + "'");
            rs.next();
            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                corpInfo.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return corpInfo;
    }

    public ArrayList<String> getAdminUser(String email) {
        ArrayList<String> adminInfo = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * " +
                    "FROM admin " +
                    "WHERE `e-mail` = '" + email + "'");
            rs.next();
            for (int i = 1; i < rs.getMetaData().getColumnCount(); i++) {
                adminInfo.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return adminInfo;
    }

    public ArrayList<String> getAllCitiesInCountry(String country) {
        ArrayList<String> result = new ArrayList<>();

        try {
            ResultSet result1 = statement.executeQuery(
                    "SELECT CountryCode FROM country WHERE countryName = '" + country + "'");

            result1.next();                                         // ???
            String countryCode = result1.getString(1);


            ResultSet result2 = statement.executeQuery("SELECT CityName FROM city WHERE country_CountryCode = '" + countryCode + "'");
            while (result2.next()) {
                result.add(result2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ArrayList<String> getAllHotelsInCity(String city) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result1 = statement.executeQuery(
                    "SELECT cityCode FROM city WHERE cityName = '" + city + "'");
            result1.next();

            ResultSet result2 = statement.executeQuery(
                    "SELECT hotelName FROM hotels WHERE city_cityCode = '" + result1.getString(1) + "' AND isVerified = 1");
            while (result2.next()) {
                results.add(result2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllRestaurantsInCity(String city) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result1 = statement.executeQuery(
                    "SELECT cityCode from city where cityName = '" + city + "'");
            result1.next();

            ResultSet result2 = statement.executeQuery(
                    "SELECT restaurantsName from restaurants where city_cityCode = '" + result1.getString(1) + "' AND isVerified = 1");
            while (result2.next()) {
                results.add(result2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllMuseumsInCity(String city) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result1 = statement.executeQuery(
                    "SELECT cityCode from city where cityName = '" + city + "'");
            result1.next();

            ResultSet result2 = statement.executeQuery(
                    "SELECT museumName from museums where city_cityCode = '" + result1.getString(1) + "' AND isVerified = 1");
            while (result2.next()) {
                results.add(result2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllUnVerifiedHotels() {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT hotelName FROM hotels WHERE isVerified = 0");
            while (result.next()) {
                results.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllUnVerifiedRestauraunts() {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT restaurantsName from restaurants WHERE isVerified = 0");
            while (result.next()) {
                results.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllUnVerifiedMuseums() {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery("SELECT museumName from museums WHERE isVerified = 0");
            while (result.next()) {
                results.add(result.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getAllSightsInCity(String city) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result1 = statement.executeQuery(
                    "SELECT cityCode from city where cityName = '" + city + "'");
            result1.next();

            ResultSet result2 = statement.executeQuery(
                    "SELECT sightName from sights where city_cityCode  = '" + result1.getString(1) + "'");
            while (result2.next()) {
                results.add(result2.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public boolean isACity(String searchResult) {
        ArrayList<String> listTest = new ArrayList<>();
        boolean isCity = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT cityName FROM city WHERE cityName = '" + searchResult + "'");
            while (result.next()) {
                listTest.add(result.getString(1));
            }

            if (listTest.isEmpty()) {
                isCity = false;
            } else {
                isCity = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isCity;
    }

    public boolean isACountry(String searchResult) {
        ArrayList<String> listTest = new ArrayList<>();
        boolean isCountry = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT countryName FROM country WHERE countryName ='" + searchResult + "'");
            while (result.next()) {
                listTest.add(result.getString(1));
            }
            if (listTest.isEmpty()) {
                isCountry = false;
            } else {
                isCountry = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isCountry;
    }

    public boolean isARestaurant(String searchResult) {
        ArrayList<String> results = new ArrayList<>();
        boolean isRestaurant = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT restaurantsName FROM restaurants WHERE restaurantsName ='" + searchResult + "'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            if (results.isEmpty()) {
                isRestaurant = false;
            } else {
                isRestaurant = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isRestaurant;
    }

    public boolean isAHotel(String searchResult) {
        ArrayList<String> results = new ArrayList<>();
        boolean isHotel = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT hotelName FROM hotels WHERE hotelName ='" + searchResult + "'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            if (results.isEmpty()) {
                isHotel = false;
            } else {
                isHotel = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isHotel;
    }

    public boolean isAMuseum(String searchResult) {
        ArrayList<String> results = new ArrayList<>();
        boolean isMuseum = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT museumName FROM museums WHERE museumName ='" + searchResult + "'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            if (results.isEmpty()) {
                isMuseum = false;
            } else {
                isMuseum = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isMuseum;
    }

    public boolean isASight(String searchResult) {
        ArrayList<String> results = new ArrayList<>();
        boolean isSight = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT sightName FROM sights WHERE sightName ='" + searchResult + "'");
            while (result.next()) {
                results.add(result.getString(1));
            }
            if (results.isEmpty()) {
                isSight = false;
            } else {
                isSight = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isSight;
    }

    public boolean isAAttraction(String searchResult) {
        ArrayList<String> results = new ArrayList<>();
        boolean isAAttraction = false;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT hotels.hotelName FROM hotels WHERE hotelName like '" + searchResult + "%'" +
                            " UNION " + "SELECT restaurants.restaurantsName FROM restaurants WHERE restaurantsName = '" + searchResult + "'" +
                            " UNION " + "SELECT museums.museumName FROM museums WHERE museumName = '" + searchResult + "'" +
                            " UNION " + "SELECT sights.sightName FROM sights WHERE sightName = '" + searchResult + "'");

            while (result.next()) {
                results.add(result.getString(1));
            }
            if (results.isEmpty()) {
                isAAttraction = false;
            }
            if (!results.isEmpty()) {
                isAAttraction = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isAAttraction;
    }

    public ArrayList<String> getRestaurant(String restaurant) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(
                    "SELECT RestaurantsName,RestaurantsAddress,restaurantsPhone,`RestaurantE-mail`, RestaurantsWebb FROM restaurants WHERE RestaurantsName = '" + restaurant + "'");
            result.next();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                results.add(result.getString(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getHotel(String hotel) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(
                    "SELECT HotelName,HotelAddress,HotelPhone,`HotelE-mail`, HotelWebb FROM hotels WHERE HotelName = '" + hotel + "'");
            result.next();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                results.add(result.getString(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;

    }

    public ArrayList<String> getMuseum(String museum) {
        ArrayList<String> results = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(
                    "SELECT museumName,museumAddress,museumPhone,`museumE-mail`, museumsWebb FROM museums WHERE museumName = '" + museum + "'");
            result.next();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                results.add(result.getString(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getSight(String sight) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT sightName,sightAddress FROM sights WHERE sightName = '" + sight + "'");
            result.next();
            for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                results.add(result.getString(i));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getHotelAllColumns(String hotel) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM hotels WHERE HotelName = '" + hotel + "'");
            rs.next();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                results.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return results;
    }

    public ArrayList<String> getRestaurantAllColumns(String restaurant) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM restaurants WHERE RestaurantsName = '" + restaurant + "'");
            rs.next();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                results.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getMuseumAllColumns(String museum) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet rs = statement.executeQuery("SELECT * FROM museums WHERE MuseumName = '" + museum + "'");
            rs.next();
            for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                results.add(rs.getString(i));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void addHotel(String hotelName, String hotelAddress, String hotelPhone, String hotelEmail,
                         String hotelWebb, int cityCode, String countryCode, long CorporateUserOCR, byte isVerified) {
        try {
            statement.executeUpdate("insert into hotels(HotelName,HotelAddress,HotelPhone,`HotelE-mail`,HotelWebb,City_CityCode,City_Country_CountryCode," +
                    "`CorporateUser_OCR-number`,isVerified)" +
                    "values ('" + hotelName + "','" + hotelAddress + "',' " +
                    hotelPhone + "', '" + hotelEmail + "', '" + hotelWebb + "'," + cityCode + ",'" + countryCode + "'," + CorporateUserOCR + "," + isVerified + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addRestaurant(String restaurantName, String restaurantAddress, String restaurantPhone, String restaurantEmail,
                              String restaurantWebb, int cityCode, String countryCode, long CorporateUserOCR, byte isVerified) {
        try {
            statement.executeUpdate("insert into restaurants(RestaurantsName,RestaurantsAddress,RestaurantsPhone,`RestaurantE-mail`,RestaurantsWebb,City_CityCode," +
                    "City_Country_CountryCode,`CorporateUser_OCR-number`,isVerified)" +
                    "values ('" + restaurantName + "','" + restaurantAddress + "', '" +
                    restaurantPhone + "', '" + restaurantEmail + "', '" + restaurantWebb + "'," + cityCode + ",'" + countryCode + "'," + CorporateUserOCR + "," + isVerified + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addMuseum(String museumName, String museumAddress, String museumPhone, String museumEmail,
                          String museumWeb, int cityCode, String countryCode, long CorporateUserOCR, byte isVerified) {
        try {
            statement.executeUpdate("insert into museums(MuseumName, MuseumAddress, MuseumPhone, `MuseumE-mail`, MuseumsWebb,City_CityCode, City_Country_CountryCode, " +
                    "`CorporateUser_OCR-number`, isVerified)" +
                    "values ('" + museumName + "','" + museumAddress + "', '" +
                    museumPhone + "', '" + museumEmail + "', '" + museumWeb + "'," + cityCode + ",'" + countryCode + "'," + CorporateUserOCR + "," + isVerified + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSight(String sightName, String sightAddress, int cityCode, String countryCode) {
        try {
            statement.executeUpdate("INSERT INTO sights(SightName, SightAddress, City_CityCode, City_Country_CountryCode) " +
                    "VALUES('" + sightName + "', '" + sightAddress + "', " + cityCode + ", '" + countryCode + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void writeCommentToDB(String commentText, String attractionName, String userName) {
        String idStatement = null;
        String foreignKeyId = null;
        int attractionId;

        if (isAMuseum(attractionName)) {
            foreignKeyId = "Museums_";
            idStatement = "MuseumId";
        }
        if (isAHotel(attractionName)) {
            foreignKeyId = "Hotels_";
            idStatement = "HotelId";
        }
        if (isASight(attractionName)) {
            foreignKeyId = "Sights_";
            idStatement = "SightId";
        }
        if (isARestaurant(attractionName)) {
            foreignKeyId = "Restaurants_";
            idStatement = "RestaurantId";
        }
        attractionId = getAttractionId(attractionName);

        try {
            statement.executeUpdate(
                    "INSERT INTO Review(" + foreignKeyId + idStatement + ", commentText, commentDate," +
                            "PrivateUser_PrivateUserName)" +
                            " Values(" + attractionId + "," + "'" + commentText + "', NOW(), '" + userName + "')");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getAttractionId(String attractionName) {
        int attractionId = 0;
        String idStatement = null;
        String prepStatement = null;
        String table = null;

        if (isAMuseum(attractionName)) {
            table = "Museums";
            idStatement = "MuseumId";
            prepStatement = "MuseumName";
        }
        if (isAHotel(attractionName)) {
            table = "Hotels";
            idStatement = "HotelId";
            prepStatement = "HotelName";
        }
        if (isASight(attractionName)) {
            table = "Sights";
            idStatement = "SightId";
            prepStatement = "SightName";
        }
        if (isARestaurant(attractionName)) {
            table = "Restaurants";
            idStatement = "RestaurantId";
            prepStatement = "RestaurantsName";
        }
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT " + idStatement + " FROM " + table +
                            " WHERE " + prepStatement + " = '" + attractionName + "'");
            result.next();
            attractionId = result.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return attractionId;
    }


    public void verifyAddRequest(String name, int id) {
        String sqlUpdate = "";
        if (isAHotel(name)) {
            sqlUpdate = "UPDATE hotels SET isVerified = 1 WHERE HotelId = " + id;
        } else if (isARestaurant(name)) {
            sqlUpdate = "UPDATE restaurants SET isVerified = 1 WHERE RestaurantId = " + id;
        } else if (isAMuseum(name)) {
            sqlUpdate = "UPDATE museums SET isVerified = 1 WHERE MuseumId = " + id;
        }

        try {
            statement.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getCommentsFromAttraction(String attractionName) {
        ArrayList<String> results = new ArrayList<>();
        String prepStatement1 = null;
        String userName = null;
        String commentText = null;
        String commentDate = null;
        int attractionId = getAttractionId(attractionName);

        if (isARestaurant(attractionName)) {
            prepStatement1 = "Restaurants_RestaurantId";
        }
        if (isAHotel(attractionName)) {
            prepStatement1 = "Hotels_HotelId";
        }
        if (isAMuseum(attractionName)) {
            prepStatement1 = "Museums_MuseumId";
        }
        if (isASight(attractionName)) {
            prepStatement1 = "Sights_SightId";
        }
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT review.PrivateUser_PrivateUserName, review.commentText, review.commentDate" +
                            " FROM review" +
                            " WHERE review." + prepStatement1 + " = " + attractionId);
            while (result.next()) {
                userName = result.getString(1);
                commentText = result.getString(2);
                commentDate = result.getString(3);
                if (commentText != null) {
                    results.add(userName + ": " + commentText + "     " + commentDate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<Integer> getCommentsIdFromAttraction(String attractionName) {
        ArrayList<Integer> results = new ArrayList<>();
        String prepStatement1 = null;
        int attractionId = getAttractionId(attractionName);

        if (isARestaurant(attractionName)) {
            prepStatement1 = "Restaurants_RestaurantId";
        }
        if (isAHotel(attractionName)) {
            prepStatement1 = "Hotels_HotelId";
        }
        if (isAMuseum(attractionName)) {
            prepStatement1 = "Museums_MuseumId";
        }
        if (isASight(attractionName)) {
            prepStatement1 = "Sights_SightId";
        }
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT commentId, commentText" +
                            " FROM review" +
                            " WHERE review." + prepStatement1 + " = " + attractionId);
            while (result.next()) {
                if (result.getString(2) != null) {
                    results.add(result.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public ArrayList<String> getReplyFromComment(int commentId) {
        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet result2 = statement.executeQuery(
                    "SELECT privateUser_privateUserName, CompanyName, replyText, replyDate" +
                            " FROM reply" +
                            " WHERE review_commentid = " + commentId);
            while (result2.next()) {
                if (result2.getString(1) != null) {
                    results.add("   >>" + result2.getString(1) + ":" + result2.getString(3) +
                            "   " + result2.getString(4));
                }
                if (result2.getString(2) != null) {
                    results.add("   >>" + result2.getString(2) + ": " + result2.getString(3) +
                            "   " + result2.getString(4));
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }


    public void writeReplyToDataBase(int commentId, BasicAccount account, String replyText) {

        if (account instanceof UserAccount) {
            try {
                statement.executeUpdate(
                        "INSERT INTO Reply(review_commentid, PrivateUser_PrivateUserName, replyText, replyDate) " +
                                "VALUES(" + commentId + ", '" + ((UserAccount) account).getUserName() + "', '" + replyText + "', NOW())");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (account instanceof CorporateAccount) {
            try {
                statement.executeUpdate(
                        "INSERT INTO Reply(review_commentid,`CorporateUser_OCR-number` ,CompanyName, replyText, replyDate) " +
                                "VALUES(" + commentId + "," + ((CorporateAccount) account).getOcrNr() + " ,'" + ((CorporateAccount) account).getCompanyName() + "','" + replyText + "', NOW())");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ArrayList<Integer> getCommentId(String userName, String date) {
        ArrayList<Integer> results = new ArrayList<>();

        try {
            ResultSet result = statement.executeQuery(
                    "SELECT commentId" +
                            " FROM Review" +
                            " WHERE PrivateUser_PrivateUserName = '" + userName +
                            "' AND commentDate = '" + date + "'");
            while (result.next()) {
                results.add(result.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public Integer getCityCode(String cityName) {

        int cityCode = 0;

        try {
            ResultSet result = statement.executeQuery(
                    "SELECT CityCode" +
                            " FROM City" +
                            " WHERE CityName = '" + cityName + "'");
            while (result.next()) {
                cityCode = ((Number) result.getObject(1)).intValue();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityCode;
    }


    public void writeRatingToDatabase(String attractionName, String userName, int rating) {
        String prepStatement = null;
        int attractionId;

        if (isAMuseum(attractionName)) {
            prepStatement = "Museums_Museumid";
        }
        if (isAHotel(attractionName)) {
            prepStatement = "Hotels_HotelId";
        }
        if (isASight(attractionName)) {
            prepStatement = "Sights_SightId";
        }
        if (isARestaurant(attractionName)) {
            prepStatement = "Restaurants_RestaurantId";
        }
        attractionId = getAttractionId(attractionName);

        try {
            statement.executeUpdate(
                    "INSERT INTO Review(" + prepStatement + ", privateUser_PrivateUserName, rating)" +
                            " Values(" + attractionId + ", '" + userName + "'," + rating + ")");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isRated(String attractionName, String userName) {
        String prepStatement = null;
        int attractionId;
        int rating = 0;
        if (isAMuseum(attractionName)) {
            prepStatement = "Museums_Museumid";
        }
        if (isAHotel(attractionName)) {
            prepStatement = "Hotels_HotelId";
        }
        if (isASight(attractionName)) {
            prepStatement = "Sights_SightId";
        }
        if (isARestaurant(attractionName)) {
            prepStatement = "Restaurants_RestaurantId";
        }
        attractionId = getAttractionId(attractionName);
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT rating, privateUser_PrivateUserName" +
                            " FROM review WHERE " + prepStatement + " = " + attractionId + "" +
                            " AND privateUser_PrivateUserName = '" + userName + "'");
            while (result.next()) {
                rating = rating + result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rating == 0) {
            return false;
        } else
            return true;
    }

    public int getRating(int commentId) {
        int rating = 0;
        try {
            ResultSet result = statement.executeQuery(
                    "SELECT AVG(rating)" +
                            " FROM review" +
                            " WHERE hotels_hotelId = " + commentId);
            while (result.next()) {
                rating = result.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rating;
    }

    public void deleteUserAccount(String userName, BasicAccount account) {

        if (account instanceof UserAccount) {
            try {
                statement.executeUpdate("DELETE FROM privateuser " +
                        "WHERE PrivateUserName = '" + userName + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (account instanceof CorporateAccount) {
            try {
                statement.executeUpdate("DELETE FROM corporateuser " +
                        "WHERE CompanyName = '" + userName + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else if (account == null) {
            try {
                statement.executeUpdate("DELETE FROM corporateuser " +
                        "WHERE CompanyName = '" + userName + "'");
                statement.executeUpdate("DELETE FROM privateuser " +
                        "WHERE PrivateUserName = '" + userName + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

    }

    public void removeAttraction(String name, int id) {
        String sqlUpdate = "";
        if (isAHotel(name)) {
            sqlUpdate = "DELETE FROM hotels WHERE HotelId = " + id;
        } else if (isARestaurant(name)) {
            sqlUpdate = "DELETE FROM restaurants WHERE RestaurantId = " + id;
        } else if (isAMuseum(name)) {
            sqlUpdate = "DELETE FROM museums WHERE MuseumId = " + id;
        } else if (isASight(name)) {
            sqlUpdate = "DELETE FROM sights WHERE SightId = " + id;
        }
        try {
            statement.executeUpdate(sqlUpdate);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
