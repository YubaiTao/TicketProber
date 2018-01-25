package rpc;

import entity.Item;
import external.TicketMasterAPI;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * project: Webtest
 *
 * @author YubaiTao on 22/01/2018.
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("long"));
        // Term can be empty or null
        String term = request.getParameter("term");
        TicketMasterAPI tmAPI = new TicketMasterAPI();
        List<Item> items = tmAPI.search(lat, lon, term);
        List<JSONObject> list = new ArrayList<>();
        try {
            for (Item item : items) {
                // Add a thin version of item objects
                JSONObject obj = item.toJSONObject();
                list.add(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONArray array = new JSONArray(list);
        Utility.writeJsonArray(response, array);



//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.setContentType("text/html");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        if (request.getParameter("username") != null) {
//            String username = request.getParameter("username");
//            out.println("<html><body><h1>");
//            out.println("Hello, " + username);
//            out.println("<br />This is an html page!");
//            out.println("</h1></body></html>");
//        }

//        String username = "";
//        if (request.getParameter("username") != null) {
//            username = request.getParameter("username");
//        }
//        JSONObject obj = new JSONObject();
//        JSONArray array = new JSONArray();
//        try {
//            obj.put("username", username);
//            array.put(new JSONObject().put("username", "Yuki"));
//            array.put(new JSONObject().put("username", "Jennifer"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        out.println(obj);
//        out.print(array);
//        out.flush();
//        out.close();
    }
}
