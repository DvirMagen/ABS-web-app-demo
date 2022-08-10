package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.Constants.YAZ_TIME;

public class DataSystemServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("DataSystemServlet doGet");
        //System.out.println(req.toString());
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
