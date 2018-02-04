package rpc;

import algorithm.GeoRecommendation;
import com.mysql.jdbc.Util;
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
import java.util.List;

/**
 * project: TicketProber
 *
 * @author YubaiTao on 03/02/2018.
 */
@WebServlet("/login")
public class Login extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        DBConnection conn = DBConnectionFactory.getDBConnection();
        try {
            JSONObject input = Utility.readJsonObject(request);
            String userId = input.getString("user_id");
            String pwd = input.getString("password");

            JSONObject obj = new JSONObject();

            if (conn.verifyLogin(userId, pwd)) {
                HttpSession session = request.getSession();
                session.setAttribute("user_id", userId);
                // setting session to expire in 10 minutes
                session.setMaxInactiveInterval(10 * 60);
                // Get user name
                String name = conn.getFullname(userId);
                obj.put("status", "OK");
                obj.put("user_id", userId);
                obj.put("name", name);
            } else {
                response.setStatus(401);
            }
            Utility.writeJsonObj(response, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        DBConnection conn = DBConnectionFactory.getDBConnection();
        try {
            JSONObject obj = new JSONObject();
            HttpSession session = request.getSession(false);
            if (session == null) {
                response.setStatus(403);
                obj.put("status", "Session Invalid");
            } else {
                String userId = (String) session.getAttribute("user_id");
                String name = conn.getFullname(userId);
                obj.put("status", "OK");
                obj.put("user_id", userId);
                obj.put("name", name);
            }
            Utility.writeJsonObj(response, obj);
        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
}