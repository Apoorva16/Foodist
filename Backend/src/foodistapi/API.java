package foodistapi;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

/**
 * Created by csuser1234 on 4/23/16.
 */

@Path("/")

public class API {

    @Context
    SecurityContext securityContext;

    private Database db = new Database();

    @HEAD
    @Path("/ping")
    public Response ping() {
        return Response.ok().build();
    }

    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getItems() {

        HashMap returnObj = new HashMap();

        String[][] itemArray = db.get_all_items();
        if(itemArray != null) {
            ArrayList items = new ArrayList();

            for(String[] item : itemArray) {
                HashMap map = new HashMap();
                map.put("id", Integer.parseInt(item[0]));
                map.put("name", item[1]);
                map.put("place", item[2]);
                map.put("meal", item[3]);
                map.put("cuisine", item[4]);
                map.put("description", item[5]);
                map.put("itemurl", item[6]);
                map.put("placeurl", item[7]);

                //AP: TODO Needs to change
                map.put("viewed", randInt(0, 200));
                map.put("saved", randInt(0,30));
                map.put("tried", randInt(0, 80));

                items.add(map);
            }

            returnObj.put("items", items);
        }

        return Response.ok(returnObj, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/user-info")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserInfo() {
        int userId = Integer.parseInt(this.securityContext.getUserPrincipal().getName());

        HashMap returnObj = new HashMap();

        String[] userInfo = db.get_user_info(userId);

        if(userInfo != null) {
            HashMap userInfoObj = new HashMap();
            userInfoObj.put("firstName", userInfo[0]);
            userInfoObj.put("lastName", userInfo[1]);
            userInfoObj.put("imageUrl", userInfo[2]);
            returnObj.put("userInfo", userInfoObj);
        }

        return Response.ok(returnObj, MediaType.APPLICATION_JSON).build();
    }

    private int randInt(int min, int max) {
        return new Random().nextInt((max - min) + 1) + min;
    }

    @POST
    @Path("/register")
    @Produces(MediaType.APPLICATION_JSON)
    public Response postRegister(HashMap objIn) {
        if(objIn != null) {
            String username = (String) objIn.get("email");
            String password = (String) objIn.get("password");
            String fullName = (String) objIn.get("fullName");

            String imageUrl = "http://speakeasyy.com.s3-website-us-west-1.amazonaws.com/images/default_image.jpg";
            String apiToken;

            boolean duplicate = false;
            do {
                apiToken = UUID.randomUUID().toString().replace("-", "");
                    /* PD: API Tokens must be unique accross all users in our database.
                           Just doing a sanity check to make sure */
                duplicate = db.check_user_duplicateApiToken(apiToken);
            } while (duplicate);

            String authority = "ROLE_USER";
            Boolean enabled = true;

            boolean success = db.insert_user(fullName, null, imageUrl, username, password, apiToken, authority, enabled);
            if(!success) {
                return Response.status(461).build();
            }
        }

        return Response.ok().build();
    }
}