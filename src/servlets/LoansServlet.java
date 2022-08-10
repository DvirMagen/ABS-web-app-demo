package servlets;

import com.google.gson.Gson;
import engine.Loan;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import listener.ContextListener;

import static constants.Constants.USERNAME;

public class LoansServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String owner = req.getParameter("owner");


        Set<Loan> allLoans = ContextListener.engine.getAllLoans();

        if (owner != null){
            allLoans = new HashSet<>(allLoans);
            allLoans.removeIf(loan -> !loan.getOwner().equals(owner));
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
