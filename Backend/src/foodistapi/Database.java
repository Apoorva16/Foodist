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

    protected String[][] get_all_meals() {
        String sql =
               " SELECT MEAL_ID, TYPE FROM MEALS AS M;" ;
        sqlHandler.execute(sql);
        return sqlHandler.grabStringResults();
    }

}


