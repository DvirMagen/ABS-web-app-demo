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
import java.util.List;
import java.util.Set;

public class CategoriesServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println(req.toString());
        HashMap<String,Object> map = new HashMap<>();
        Set<String> categories = ContextListener.engine.getAllCategories();
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(categories);
            out.println(json);
            out.flush();
        }
    }

}
