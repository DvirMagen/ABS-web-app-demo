package servlets;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import static constants.Constants.*;

public class NewLoanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromParameter = request.getParameter(USERNAME);
        String loanIdFromParameter = request.getParameter(LOAN_ID);
        String loanCategoryFromParameter = request.getParameter(LOAN_CATEGORY);
        String loanCapitalFromParameter = request.getParameter(LOAN_CAPITAL);
        String loanTotalYazTimeFromParameter = request.getParameter(LOAN_TOTAL_YAZ_TIME);
        String loanPaymentEveryYazTimeFromParameter = request.getParameter(LOAN_PAYMENT_EVERY_YAZ_TIME);
        String loanInstintPerPaymentFromParameter = request.getParameter(LOAN_INSTINT_PER_PAYMENT);
        int status = 200;
        status = ContextListener.engine.createNewLoanForCustomer(usernameFromParameter, loanIdFromParameter,
                loanCategoryFromParameter, loanCapitalFromParameter, loanTotalYazTimeFromParameter, loanPaymentEveryYazTimeFromParameter, loanInstintPerPaymentFromParameter );
            if (status == 200) {
//                response.setContentType("application/json");
//                try (PrintWriter out = response.getWriter()) {
//                    Gson gson = new Gson();
//
//                    String json = gson.toJson(allLoans);
//                    out.println(json);
//                    out.flush();
//                }
            }else{
                response.setStatus(status);
                response.setContentType("application/json");
                try (PrintWriter out = response.getWriter()) {
                    Gson gson = new Gson();
                    HashMap<String,String> map = new HashMap<>();
                    map.put("Error msg","Not Valid Process");
                    String json = gson.toJson(map);
                    out.println(json);
                    out.flush();
                }
            }
    }
}
