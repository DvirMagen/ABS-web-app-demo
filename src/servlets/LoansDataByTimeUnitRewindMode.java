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
import java.util.HashMap;
import java.util.List;

public class LoansDataByTimeUnitRewindMode extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Loan> allLoansDataInRewindMode = ContextListener.engine.getAllLoansDataInRewindMode();
        if(allLoansDataInRewindMode == null){
            resp.setStatus(404);
            return;
        }

        HashMap<String,Object> map = new HashMap<>();
        map.put("loans", allLoansDataInRewindMode);
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            out.println(json);
            out.flush();
        }
    }
}
