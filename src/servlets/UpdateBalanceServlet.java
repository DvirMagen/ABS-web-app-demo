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

public class UpdateBalanceServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int status = 200;
        int flag = 0;
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        String actionFromParameter = req.getParameter(Constants.ACTION_ON_BALANCE);
        Double amount = Double.parseDouble(req.getParameter(Constants.AMOUNT));
        if(usernameFromParameter != null)
        {
            if(actionFromParameter.equalsIgnoreCase(Constants.CHARGE))
            {
                status = ContextListener.engine.loadMoneyToCustomerAccount(usernameFromParameter, amount);
            }
            else if(actionFromParameter.equalsIgnoreCase(Constants.WITHDRAW))
            {
                status = ContextListener.engine.withdrawMoneyFromCustomerAccount(usernameFromParameter, amount);
            }
            else if(actionFromParameter.equalsIgnoreCase(Constants.UPDATE))
            {
                status = 200;
            }
            else{
                flag = 1;
                status = 400;
            }

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
                if(flag == 0)
                    map.put("Not Found", "Customer Not Found!");
                else if (flag == 1)
                    map.put("Not Found", "Invalid Action!");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }
    }
}
