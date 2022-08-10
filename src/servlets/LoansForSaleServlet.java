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
import java.util.HashSet;
import java.util.Set;

public class LoansForSaleServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromParameter = req.getParameter(Constants.USERNAME);
        Set<Loan> allLoans = new HashSet<>(ContextListener.engine.getAllLoans());
        if (usernameFromParameter != null){
            allLoans.removeIf(loan -> loan.getOwner().equals(usernameFromParameter));
            allLoans.removeIf(loan -> loan.getLenders().stream().anyMatch(lender -> lender.getName().equals(usernameFromParameter)));
        }
        allLoans.removeIf(loan -> !loan.isOnSale());
        allLoans.removeIf(loan -> !loan.getStatus().equalsIgnoreCase("active"));

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(allLoans);
            out.println(json);
            out.flush();
        }
    }
}
