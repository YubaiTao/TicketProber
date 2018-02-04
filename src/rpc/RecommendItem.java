package rpc;

import algorithm.GeoRecommendation;
import entity.Item;
import org.json.JSONArray;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * project: Webtest
 *
 * @author YubaiTao on 22/01/2018.
 */
@WebServlet("/recommendation")
public class RecommendItem extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        String userId = session.getAttribute("user_id").toString();

//        String userId = request.getParameter("user_id");
        double lat = Double.parseDouble(request.getParameter("lat"));
        double lon = Double.parseDouble(request.getParameter("lon"));
        GeoRecommendation recommendation = new GeoRecommendation();
        List<Item> items = recommendation.recommendItems(userId, lat, lon);

        JSONArray result = new JSONArray();
        try {
            for (Item item : items) {
                result.put(item.toJSONObject());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utility.writeJsonArray(response, result);




//        response.addHeader("Access-Control_Allow_Origin", "*");
//        response.setContentType("application/json");
//        PrintWriter out = response.getWriter();
//        JSONArray array = new JSONArray();
//        JSONObject fakeObj_1 = new JSONObject();
//        JSONObject fakeObj_2 = new JSONObject();
//        try {
//            fakeObj_1.put("username", "abcd");
//            fakeObj_1.put("address", "San Francisco");
//            fakeObj_1.put("time", "01/01/2017");
//            fakeObj_2.put("username", "1234");
//            fakeObj_2.put("address", "San Jose");
//            fakeObj_2.put("time", "01/02/2017");
//            array.put(fakeObj_1);
//            array.put(fakeObj_2);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        out.print(array);
//        out.flush();
//        out.close();
    }
}
