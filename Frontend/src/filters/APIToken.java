package filters;

import pd.sqlframe.SqlHandler;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * Created by csuser1234 on 4/27/16.
 */
public class APIToken implements Filter {
    SqlHandler sqlHandler;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        if(req.getUserPrincipal() != null){
            String username = req.getUserPrincipal().getName();

            String userId = "";
            String apiToken = "";
            String sql = "SELECT USERID, APITOKEN FROM USERS WHERE USERNAME = ?;";
            sqlHandler.execute(sql, username);
            if(sqlHandler.getNumRows() > 0){
                userId = sqlHandler.grabStringResults()[0][0];
                apiToken = sqlHandler.grabStringResults()[0][1];
            }

            Cookie userIdCookie = new Cookie("foodistAccessId", userId);
            Cookie tokenCookie = new Cookie("foodistAccessToken", apiToken);

            userIdCookie.setPath("/");
            tokenCookie.setPath("/");

            res.addCookie(userIdCookie);
            res.addCookie(tokenCookie);
        }
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        DataSource ds = null;
        try {
            ds = (DataSource) new InitialContext().lookup("java:comp/env/jdbc/Foodist");
        } catch (NamingException ne) {
            throw new RuntimeException(ne.toString());
        }
        this.sqlHandler = new SqlHandler(ds);
    }
}
