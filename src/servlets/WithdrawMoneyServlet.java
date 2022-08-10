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

public class WithdrawMoneyServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int status = 200;
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        double money_to_charge = Double.parseDouble(req.getParameter(Constants.WITHDRAW_MONEY));
        if(usernameFromParameter != null) {
            status = ContextListener.engine.withdrawMoneyFromCustomerAccount(usernameFromParameter, money_to_charge);
        }
        if (status == 200) {
            resp.setStatus(status);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                HashMap<String, Object> map = new HashMap<>();
                map.put(Constants.USERNAME, usernameFromParameter);
                map.put("balance", ContextListener.engine.getCustomerDtoByName(usernameFromParameter).getBalance());
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
                map.put("Not Found", "Customer Not Found!");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }

    }

}
