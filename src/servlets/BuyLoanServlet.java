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

public class BuyLoanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String loanIdFromParameter = req.getParameter("loan_id");
        String newOwnerFromParameter = req.getParameter("new_owner");
        String oldOwnerFromParameter = req.getParameter("old_owner");
        int flag = 0;
        if (loanIdFromParameter != null && newOwnerFromParameter != null && oldOwnerFromParameter != null) {
           flag =  ContextListener.engine.transferLoanOwnership(loanIdFromParameter,newOwnerFromParameter, oldOwnerFromParameter);
            if (flag != 0)
            {
                resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                resp.setContentType("application/json");
                try (PrintWriter out = resp.getWriter()) {
                    Gson gson = new Gson();
                    HashMap<String, String> map = new HashMap<>();
                    map.put("Not Allowed", "You Don't Have Enough Money!");
                    String json = gson.toJson(map);
                    out.println(json);
                    out.flush();
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                HashMap<String, String> map = new HashMap<>();
                map.put("Not Found", "Customer Not Found!");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }
    }
}
