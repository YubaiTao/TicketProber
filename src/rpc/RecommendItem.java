package rpc;

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
        response.addHeader("Access-Control_Allow_Origin", "*");
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray array = new JSONArray();
        JSONObject fakeObj_1 = new JSONObject();
        JSONObject fakeObj_2 = new JSONObject();
        try {
            fakeObj_1.put("username", "abcd");
            fakeObj_1.put("address", "San Francisco");
            fakeObj_1.put("time", "01/01/2017");
            fakeObj_2.put("username", "1234");
            fakeObj_2.put("address", "San Jose");
            fakeObj_2.put("time", "01/02/2017");
            array.put(fakeObj_1);
            array.put(fakeObj_2);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        out.print(array);
        out.flush();
        out.close();
    }
}
