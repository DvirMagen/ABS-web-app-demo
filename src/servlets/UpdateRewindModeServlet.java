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

public class UpdateRewindModeServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int status = 200;
        String rewindModeFromParameter = req.getParameter(Constants.REWIND_MODE);
        if(rewindModeFromParameter != null)
        {
            if(rewindModeFromParameter.equalsIgnoreCase("true"))
            {
                ContextListener.engine.updateDataForRewindMode();
                ContextListener.engine.setRewindMode(true);
            }
            else if(rewindModeFromParameter.equalsIgnoreCase("false"))
            {
               ContextListener.engine.setRewindMode(false);
            }
            else
            {
                status = 404;
            }

        }

        if(status != 200) {
            resp.setStatus(status);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                HashMap<String, String> map = new HashMap<>();
                map.put("Not Found", "Page Not Found!");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }
    }
}
