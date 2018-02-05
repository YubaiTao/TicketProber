package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * project: TicketProber
 *
 * @author YubaiTao on 01/02/2018.
 */
@WebServlet("/history")
public class ItemHistory extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = Utility.readJsonObject(request);
            String user_id = input.getString("user_id");
            JSONArray array = input.getJSONArray("favorite");
            List<String> itemIds = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String itemId = array.get(i).toString();
                itemIds.add(itemId);
            }

            DBConnection conn = DBConnectionFactory.getDBConnection();
            conn.setFavoriteItems(user_id, itemIds);
            Utility.writeJsonObj(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setStatus(403);
            return;
        }
        String userId = session.getAttribute("user_id").toString();

//        String userId = request.getParameter("user_id");
        JSONArray array = new JSONArray();

        DBConnection conn = DBConnectionFactory.getDBConnection();
        Set<Item> items = conn.getFavoriteItems(userId);
        for (Item item : items) {
            JSONObject obj = item.toJSONObject();
            try {
                obj.append("favorite", true);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            array.put(obj);
        }
        Utility.writeJsonArray(response, array);

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            JSONObject input = Utility.readJsonObject(request);
            String user_id = input.getString("user_id");
            JSONArray array = input.getJSONArray("favorite");
            List<String> itemIds = new ArrayList<>();
            for (int i = 0; i < array.length(); i++) {
                String itemId = array.get(i).toString();
                itemIds.add(itemId);
            }

            DBConnection conn = DBConnectionFactory.getDBConnection();
            conn.unsetFavoriteItems(user_id, itemIds);
            Utility.writeJsonObj(response, new JSONObject().put("result", "SUCCESS"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
