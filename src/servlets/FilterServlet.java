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
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static constants.Constants.*;

public class FilterServlet extends HttpServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("FilterServlet Get");
        String usernameFromParameter = req.getParameter(USERNAME);
        String amountForInvestFromParameter = req.getParameter(AMOUNT_INVEST);
        String loanCategoryFromParameter = req.getParameter(LOAN_CATEGORY);
        String minInterestRateFromParameter = req.getParameter(MIN_INTEREST_RATE);
        String minYazTimeFromParameter = req.getParameter(MIN_YAZ_FOR_LOAN);
        String maxPercentOwnershipTimeFromParameter = req.getParameter(MAX_PERCENT_OWNNERSHIP);
        String maxOpenLoansFromParameter = req.getParameter(MAX_OPEN_LOANS);
        int status = 200;
        Set<Loan> scrambleCollect = new HashSet<>(ContextListener.engine.getAllLoans());
        System.out.println("AllLoans: "+scrambleCollect);
        System.out.println("req.param: ");
        System.out.println("username:" +usernameFromParameter);
        System.out.println("amount: " + amountForInvestFromParameter);
        System.out.println("category: " + loanCategoryFromParameter);
        System.out.println("minInterest: " + minInterestRateFromParameter);
        System.out.println("minYazTime: " + minYazTimeFromParameter);
        System.out.println("maxPrecent: " +maxPercentOwnershipTimeFromParameter);
        System.out.println("maxOpenLoans: " + maxOpenLoansFromParameter);


        Stream<Loan> loanStream = scrambleCollect.stream().filter(loan -> !loan.getOwner().equalsIgnoreCase(usernameFromParameter) &&
                loan.getLenders().stream().noneMatch(lender -> lender.getName().equalsIgnoreCase(usernameFromParameter)))
                .filter(loan -> !loan.getStatus().equalsIgnoreCase("finished") && !loan.getStatus().equalsIgnoreCase("active"));
        System.out.println("loanStream Filter1: ");
//        for (Loan loan : loanStream.collect(Collectors.toSet()))
//        {
//            System.out.println("Loan: " + loan.toString());
//        }
        if (minInterestRateFromParameter != null){
            System.out.println("loanStream Filter minInterestRateFromParameter");
            loanStream = loanStream.filter(loan -> loan.getIntristPerPayment()>= Integer.parseInt(minInterestRateFromParameter));
        }
        if(loanCategoryFromParameter != null && !loanCategoryFromParameter.isEmpty()){
            System.out.println("loanStream Filter loanCategoryFromParameter");
            loanStream = loanStream.filter(loan-> loan.getCategory().equalsIgnoreCase(loanCategoryFromParameter));
        }
        if(minYazTimeFromParameter != null){
            System.out.println("loanStream Filter minYazTimeFromParameter");
            loanStream = loanStream.filter(loan -> loan.getPaysEveryYaz()>=Integer.parseInt(minYazTimeFromParameter));
        }
        if(maxPercentOwnershipTimeFromParameter != null){
            System.out.println("loanStream Filter maxPercentOwnershipTimeFromParameter");
            loanStream = loanStream.filter(loan ->
                    (Double.parseDouble(amountForInvestFromParameter) / loan.getCapital() * 100 <= Integer.parseInt(maxPercentOwnershipTimeFromParameter)));
            System.out.println("loanStream Filter5: ");

        }
        if(maxOpenLoansFromParameter != null){
            System.out.println("loanStream Filter maxOpenLoansFromParameter");
            Map<String, Integer> ownerSeen = ContextListener.engine.getOwnerSeen();
            loanStream = loanStream.filter(loan ->{
                Integer seenOrDefault = ownerSeen.getOrDefault(loan.getOwner(), -1);
                if (seenOrDefault == -1){
                    return false;
                }
                System.out.println("seenOrDefault Filter: " + seenOrDefault.toString());
                return seenOrDefault<=Integer.parseInt(maxOpenLoansFromParameter);
            });
        }
        System.out.println("loanStream Filter85: ");


        scrambleCollect = loanStream.collect(Collectors.toSet());
        for (Loan loan : scrambleCollect)
        {
            System.out.println("Loan: " + loan.toString());
        }
        resp.setContentType("application/json");
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(scrambleCollect);
            out.println(json);
            out.flush();
        }
    }
}
