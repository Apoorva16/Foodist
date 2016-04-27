package foodistapi;

import pd.sqlframe.SqlHandler;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Created by csuser1234 on 4/23/16.
 */
public class Database {

    private SqlHandler sqlHandler;
    private DataSource ds;

    protected Database() {
        try {
            this.ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Foodist");
        } catch (NamingException ne) {
            throw new RuntimeException(ne.toString());
        }
        this.sqlHandler = new SqlHandler(ds);
    }

    protected String[][] get_all_items() {
        String sql =
            " SELECT IT.ID, IT.NAME, IT.PLACE, M.TYPE AS MEAL, C.TYPE AS CUISINE, " +
            " IT.DESCRIPTION, IT.ITEMURL, IT.PLACEURL FROM ITEMS AS IT " +
            " INNER JOIN MEALS AS M ON M.MEAL_ID = IT.MEAL_ID " +
            " INNER JOIN CUISINES AS C ON C.CUISINE_ID = IT.CUISINE_ID; ";
        sqlHandler.execute(sql);
        return sqlHandler.grabStringResults();
    }

    protected String[] get_user_info(Integer userId) {
        String sql = "SELECT FNAME, LNAME, IMAGEURL FROM USERS WHERE USERID = ?";
        sqlHandler.execute(sql, userId);
        return sqlHandler.grabFirstColumn(); //PD: TODO Rename this to grabFirstRow
    }

    protected boolean insert_user(String firstName, String lastName, String imageUrl, String username, String password, String apiToken, String authority, Boolean enabled) {
        String sql = "INSERT INTO USERS (FNAME, LNAME, IMAGEURL, USERNAME, PASSWORD, APITOKEN, AUTHORITY, ENABLED) VALUES (?,?,?,?,SHA(?),?,?,?)";
        return sqlHandler.execute(sql, firstName, lastName, imageUrl, username, password, apiToken, authority, enabled);
    }

    protected boolean check_user_duplicateApiToken(String apiToken) {
        String sql =
                "SELECT USERID FROM USERS WHERE APITOKEN = ?";
        sqlHandler.execute(sql, apiToken);
        return sqlHandler.getNumRows() > 0;
    }
}


