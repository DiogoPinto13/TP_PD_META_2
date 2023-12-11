package isec.pd.meta2.Server;

import isec.pd.meta2.Shared.ErrorMessages;
import isec.pd.meta2.Shared.Login;
import isec.pd.meta2.Shared.Register;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * This class has a bunch of static methods to perform operations in the database to perform
 * updates and checks for users
 */
public class UserManager {

    /**
     * This method will register an user in the database, it will return true if the operation
     * succeed or false if the user already exists or an error occurred
     *
     * @param register
     * @return boolean
     */
    public static boolean registerUser(Register register) {
        if (!userExists(register.getUsername())) {
            return DatabaseManager.executeUpdate("INSERT INTO utilizadores (idutilizador, username, password, nome, isAdmin)" +
                    " VALUES ('" + register.getId() + "', '"
                    + register.getUsername() + "', '"
                    + register.getPassword() + "', '"
                    + register.getName() + "', '"
                    + false +
                    "');");
        }
        return false;
    }

    /**
     * This method will return true if the password is correct or otherwise false
     *
     * @param login
     * @return boolean
     */
    public static ErrorMessages checkPassword(Login login) {
        //System.out.println("User: " + login.getUsername() + " Pass: "+login.getPassword() + "\n");

        try (ResultSet rs = DatabaseManager.executeQuery("SELECT password, isAdmin FROM utilizadores WHERE username ='" + login.getUsername() + "';")){
            if (rs == null)
                return ErrorMessages.INVALID_PASSWORD;
            while (rs.next()) {
                String password = rs.getString("password");
                //System.out.println("Pass obtida: " + password + "\n");
                boolean isAdmin = rs.getBoolean("isAdmin");
                //System.out.println("É admin? " + isAdmin + "\n");
                if(login.getPassword().equals(password) && isAdmin)
                    return ErrorMessages.LOGIN_ADMIN_USER;
                else if(login.getPassword().equals(password) && !isAdmin)
                    return ErrorMessages.LOGIN_NORMAL_USER;
                else
                    return ErrorMessages.INVALID_PASSWORD;
            }

        } catch (SQLException sqlException) {
            System.out.println("Error with the database: " + sqlException);
        }
        return ErrorMessages.INVALID_PASSWORD;
    }

    /**
     * This method is to check if a user exists in the database or not, returning true or false
     *
     * @param username
     * @return boolean
     */
    public static boolean userExists(String username) {
        try (ResultSet rs = DatabaseManager.executeQuery("SELECT * FROM utilizadores WHERE username = '" + username + "';");){
            if (rs == null)
                return false;
            return rs.next();
        } catch (SQLException sqlException) {
            System.out.println("Error with the database: " + sqlException);
        }
        return false;
    }

    /**
     * this function is meant to change the password of an account
     *
     * @param username
     * @param password
     * @return
     */
    public static boolean changePassword(String username, String password) {
        return DatabaseManager.executeUpdate("UPDATE utilizadores SET password = '" + password + "';");
    }

    /**
     * this function is meant to change the name of the user
     * @param username
     * @param name
     * @return
     */
    public static boolean changeName(String username, String name){
        return DatabaseManager.executeUpdate("UPDATE utilizadores SET nome = '" + name + "';");
    }

    /**
     * this function is meant to edit the profile of an user, given a specific username, returns true or false
     * if success
     * @param username
     * @param name
     * @param id
     * @param password
     * @return
     */
    public static boolean editProfile(String username, String name, String id, String password){
        return DatabaseManager.executeUpdate("UPDATE utilizadores SET idutilizador = '" + id + "', password = '" + password + "', nome= '" + name+ "' WHERE username = '" + username + "';");
    }

    /**
     * This function is meant to send the profile information of a given username in order to edit whatever the user wants
     * @param username
     * @return all data of a given user in one String separated by a Comma.
     */
    public static String getProfileForEdition(String username){
        StringBuilder stringBuilder = new StringBuilder();

        try (ResultSet rs = DatabaseManager.executeQuery("SELECT * FROM utilizadores WHERE username = '" + username + "';");){
            if (rs == null)
                return ErrorMessages.SQL_ERROR.toString();

            ResultSetMetaData metaData = rs.getMetaData();
            int nColunas = metaData.getColumnCount();
            while(rs.next()) {
                for (int i = 1; i <= nColunas; i++) {
                    stringBuilder.append(rs.getString(i)).append(",");
                }
            }
        } catch (SQLException sqlException) {
            System.out.println("Error with the database: " + sqlException);
        }
        return stringBuilder.toString();
    }
}