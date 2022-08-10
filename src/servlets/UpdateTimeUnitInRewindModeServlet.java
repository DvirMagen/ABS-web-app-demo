package servlets;

import com.google.gson.Gson;
import engine.tools.ABSManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;
import server_utills.ServletUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.Constants.TIME_UNIT_REWIND_MODE;
import static constants.Constants.YAZ_TIME;

public class UpdateTimeUnitInRewindModeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int status = 200;
        ABSManager absManager = ServletUtils.getABSManager(getServletContext());
        String rewindActionFromParameter = req.getParameter("rewind_action");
        if(rewindActionFromParameter != null) {
            if (rewindActionFromParameter.equalsIgnoreCase("increase")) {
                ContextListener.engine.updateYazTime(false);
            }
            else if(rewindActionFromParameter.equalsIgnoreCase("decrease")){
                status = ContextListener.engine.decreaseTimeUnit();
            }
            else
                status = 400;
        }
        else
            status = 404;

        if(status == 200) {
            HashMap<String, Object> map = new HashMap<>();
            int current_yaz_time = ContextListener.engine.getTimeUnit();
            map.put(TIME_UNIT_REWIND_MODE, current_yaz_time);
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }
        else {
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
