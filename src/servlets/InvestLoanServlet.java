package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import static constants.Constants.*;

public class InvestLoanServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("InvestLoanServlet Get");
        String usernameFromParameter = req.getParameter(USERNAME);
        String investedAmountFromParameter = req.getParameter(INVESTED_AMOUNT);
        String loanidFromParameter = req.getParameter(LOAN_ID);
        double investment = Double.parseDouble(investedAmountFromParameter);
        System.out.println("usernameFromParameter="+usernameFromParameter);
        System.out.println("investedAmountFromParameter="+investedAmountFromParameter);
        System.out.println("loanidFromParameter="+loanidFromParameter);
        int status = 200;
        if(usernameFromParameter != null && loanidFromParameter != null) {
            System.out.println("investMoneyToLoan");
            status = ContextListener.engine.investMoneyToLoan(usernameFromParameter, loanidFromParameter, investment);
        }
        if (status == 200) {
            Set<Loan> allLoans = ContextListener.engine.getAllLoans();

            if (usernameFromParameter != null){
                allLoans = new HashSet<>(allLoans);
                allLoans.removeIf(loan -> loan.getLenders().isEmpty());
                for(Loan loan : allLoans)
                {
                    boolean isInvested = false;
                    for (Loan.Lender lender : loan.getLenders())
                    {
                        if(lender.getName().equalsIgnoreCase(usernameFromParameter))
                            isInvested = true;
                    }
                    if(!isInvested)
                        allLoans.remove(loan);
                }
                resp.setContentType("application/json");
                try (PrintWriter out = resp.getWriter()) {
                    Gson gson = new Gson();
                    String json = gson.toJson(allLoans);
                    out.println(json);
                    out.flush();
                }
            }
        }
        else {
            resp.setStatus(status);
            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                HashMap<String, String> map = new HashMap<>();
                map.put("Not Found", "Loan Not Found!");
                String json = gson.toJson(map);
                out.println(json);
                out.flush();
            }
        }
    }

}
