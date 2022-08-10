package servlets;

import com.google.gson.Gson;
import engine.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Set;

public class InvestmentLoansServlet extends HttpServlet {

        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
            String usernameFromQueryParameter = req.getParameter("username");
            Set<Loan> allLoans = ContextListener.engine.getAllLoans();
            Set<Loan> investedLoans = new HashSet<>();

            if (usernameFromQueryParameter != null){
                for (Loan loan : allLoans)
                {
                    for (Loan.Lender lender: loan.getLenders())
                    {
                        if(lender.getName().equalsIgnoreCase(usernameFromQueryParameter)){
                            investedLoans.add(loan);
                        }
                    }
                }
            }

            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                String json = gson.toJson(investedLoans);
                out.println(json);
                out.flush();
            }
        }
}
