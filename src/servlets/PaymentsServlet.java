package servlets;

import com.google.gson.Gson;
import engine.Loan;
import engine.PaymentView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PaymentsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String usernameFromParameter = req.getParameter("username");
        if (usernameFromParameter != null && !usernameFromParameter.equalsIgnoreCase("null"))
        {
            List<Loan> myLoans = ContextListener.engine.getThisYazLoansPaymentForCustomer(usernameFromParameter).stream()
                    .filter(loan -> !loan.getStatus().equalsIgnoreCase("finished"))
                    .collect(Collectors.toList());

            System.out.println("payment: "+myLoans.size());

            List<PaymentView> paymentViews =new ArrayList<>();

            for (Loan myLoan : myLoans) {
                for (Loan.Lender lender : myLoan.getLenders()) {
                    for (Loan.Payment payment : myLoan.getPayments()) {
                        if (!payment.getLenderName().equals(lender.getName())) continue;
                        if (!payment.isPayed()) {
                            PaymentView paymentView = new PaymentView(
                                    lender.getName(),
                                    myLoan.getId(),
                                    myLoan.getIntristPerPayment(),
                                    lender.getInvest(),
                                    myLoan.payToLender(lender.getName())+payment.getCurrentlyLeftToPay(),
                                    myLoan.getStatus());
                            if (paymentView.getUpcomingPayment()!=0)
                                paymentViews.add(paymentView);

                            break;
                        }
                    }
                }
            }

            resp.setContentType("application/json");
            try (PrintWriter out = resp.getWriter()) {
                Gson gson = new Gson();
                String json = gson.toJson(paymentViews);
                out.println(json);
                out.flush();
            }
        }

    }

}
