package servlets;

import com.google.gson.Gson;
import engine.tools.ABSManager;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;
import server_utills.ServletUtils;
import server_utills.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.Constants.YAZ_TIME;

public class YazTimeServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        HashMap<String,Object> map = new HashMap<>();
//        Integer current_yaz_time = 0;
//        current_yaz_time = ContextListener.engine.getTimeUnit();
//        map.put(YAZ_TIME, current_yaz_time);
//        try (PrintWriter out = resp.getWriter()) {
//            Gson gson = new Gson();
//            String json = gson.toJson(map);
//            out.println(json);
//            out.flush();
//        }
        ABSManager absManager = ServletUtils.getABSManager(getServletContext());
        String mFromParameter = req.getParameter("m");
        if(mFromParameter != null) {
            if (mFromParameter.equalsIgnoreCase("increase")) {
                ContextListener.engine.updateYazTime(false);
            }
        }

        HashMap<String,Object> map = new HashMap<>();
        int current_yaz_time = ContextListener.engine.getRealTimeUnit();
        map.put(YAZ_TIME, current_yaz_time);
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            out.println(json);
            out.flush();
        }

    }
}
