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

public class RewindModeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String actionFromParameter = req.getParameter("action");
        if (actionFromParameter != null && !actionFromParameter.isEmpty()){
            if (actionFromParameter.equals("active")){
                ContextListener.isRewindMode = true;
            } else if (actionFromParameter.equals("disable")){
                ContextListener.isRewindMode = false;
            }
        }

        Map<String,Boolean> map= new HashMap<>();
        map.put("RewindMode",ContextListener.isRewindMode);
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            out.println(json);
            out.flush();
        }
    }
}
