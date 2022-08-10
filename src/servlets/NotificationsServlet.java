package servlets;

import com.google.gson.Gson;
import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


public class NotificationsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("MovementServlet");
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        if(usernameFromParameter != null && !usernameFromParameter.equals("null")) {
            String paymentViewsListByCustomerName = ContextListener.engine.getNotificationsAsString(usernameFromParameter);
            //System.out.println(paymentViewsListByCustomerName);
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                Map<String, Object> map = new HashMap<>();
                map.put("notifications", paymentViewsListByCustomerName);
                String json = gson.toJson(map);
                //System.out.println(json);
                out.println(json);
                out.flush();
            }
        }
    }

}
