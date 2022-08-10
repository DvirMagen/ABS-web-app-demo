package servlets;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.Constants.*;

public class AddNotificationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = request.getParameter(USERNAME);
        String notificationFromParameter = request.getParameter(NOTIFICATION);
        int status = 200;
        status = ContextListener.engine.addNewNotification(usernameFromParameter,notificationFromParameter);
        if (status == 200) {

        } else{
            response.setStatus(status);
            response.setContentType("application/json");
            try (PrintWriter out = response.getWriter()) {
                Gson gson = new Gson();
                HashMap<String,String> map = new HashMap<>();
                map.put("Error msg","Not Valid Process");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }

    }
}
