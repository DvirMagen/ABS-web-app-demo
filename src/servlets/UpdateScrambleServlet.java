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

import static constants.Constants.*;

public class UpdateScrambleServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromParameter = req.getParameter(USERNAME);
        String amountForInvestFromParameter = req.getParameter(AMOUNT_INVEST);
        String loanCategoryFromParameter = req.getParameter(LOAN_CATEGORY);
        String minInterestRateFromParameter = req.getParameter(MIN_INTEREST_RATE);
        String minYazTimeFromParameter = req.getParameter(MIN_YAZ_FOR_LOAN);
        String maxPercentOwnershipTimeFromParameter = req.getParameter(MAX_PERCENT_OWNNERSHIP);
        String maxOpenLoansFromParameter = req.getParameter(MAX_OPEN_LOANS);
        int status = 200;
        Set<Loan> scrambleCollect = new HashSet<>(ContextListener.engine.getAllLoans());
        System.out.println("1AllLoans: " + scrambleCollect);
        System.out.println("req.param: ");
        System.out.println("username:" + usernameFromParameter);
        System.out.println("amount: " + amountForInvestFromParameter);
        System.out.println("category: " + loanCategoryFromParameter);
        System.out.println("minInterest: " + minInterestRateFromParameter);
        System.out.println("minYazTime: " + minYazTimeFromParameter);
        System.out.println("maxPrecent: " + maxPercentOwnershipTimeFromParameter);
        System.out.println("maxOpenLoans: " + maxOpenLoansFromParameter);
        scrambleCollect.removeIf(loan -> loan.getOwner().equalsIgnoreCase(usernameFromParameter));
        System.out.println("2AllLoans: " + scrambleCollect);

        scrambleCollect.removeIf(loan -> loan.getLenders().stream().anyMatch(lender -> lender.getName().equalsIgnoreCase(usernameFromParameter)));
        System.out.println("3AllLoans: " + scrambleCollect);

        scrambleCollect.removeIf(loan -> !loan.getStatus().equalsIgnoreCase("new") && !loan.getStatus().equalsIgnoreCase("pending"));
        System.out.println("4AllLoans: " + scrambleCollect);

        if(loanCategoryFromParameter != null)
        {
            scrambleCollect.removeIf(loan -> !loan.getCategory().equalsIgnoreCase(loanCategoryFromParameter));
            System.out.println("5AllLoans: " + scrambleCollect);
        }
        if(minYazTimeFromParameter != null)
        {
            scrambleCollect.removeIf(loan -> loan.getIntristPerPayment() < Integer.parseInt(minInterestRateFromParameter));
            System.out.println("6AllLoans: " + scrambleCollect);
        }
        if(minYazTimeFromParameter != null)
        {
            scrambleCollect.removeIf(loan -> loan.getTotalYazTime() < Integer.parseInt(minYazTimeFromParameter));
            System.out.println("7AllLoans: " + scrambleCollect);

        }
        if(maxPercentOwnershipTimeFromParameter != null){
            scrambleCollect.removeIf(loan -> Double.parseDouble(amountForInvestFromParameter) / loan.leftToFund() * 100 > Integer.parseInt(maxPercentOwnershipTimeFromParameter));
            System.out.println("8AllLoans: " + scrambleCollect);

        }
        System.out.println("!!!!!!!######AllLoans: " + scrambleCollect);

        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(scrambleCollect);
            System.out.println("!!!!!!!######json: " + json);
            out.println(json);
            out.flush();
        }
    }
}
