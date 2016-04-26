package foodistapi;

import javax.ws.rs.GET;
import javax.ws.rs.HEAD;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * Created by csuser1234 on 4/23/16.
 */

@Path("/")

public class API {

    private Database db = new Database();

    @HEAD
    @Path("/ping")
    public Response ping() {
        return Response.ok().build();
    }

    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    public Response items() {

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
                map.put("viewed", 100);
                map.put("saved", 5);
                map.put("tried", 5);

                items.add(map);
            }

            returnObj.put("items", items);
        }

        return Response.ok(returnObj, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/meal")
    @Produces(MediaType.APPLICATION_JSON)
    public Response meals()
    {
        HashMap returnObj= new HashMap();

        String[][] arraylist = db.get_all_meals();

        if(arraylist!=null) {
            ArrayList meals = new ArrayList();
        for(String [] item: arraylist) {
            HashMap map = new HashMap();
            map.put("id", Integer.parseInt(item[0]));
            map.put("type", item[1]);
            meals.add(map);
        }
        returnObj.put("mealtypes", meals);
    }
       return Response.ok(returnObj, MediaType.APPLICATION_JSON).build();
    }

}
