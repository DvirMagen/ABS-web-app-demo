package servlets;

import constants.Constants;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;

public class PayPaymentServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int status = 200;
        int flag = 0;
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        String lenderNameFromParameter = req.getParameter("lender_name");
        String loanIdFromParameter = req.getParameter("loan_id");
        String moneyToPayFromParameter = req.getParameter("money_to_pay");
        if(usernameFromParameter != null)
        {
            synchronized (this) {
                ContextListener.engine.paySelectedLoan(loanIdFromParameter, usernameFromParameter,
                        lenderNameFromParameter, Double.parseDouble(moneyToPayFromParameter));
            }
        }
    }
}
