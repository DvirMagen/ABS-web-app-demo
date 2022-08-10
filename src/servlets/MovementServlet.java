package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.Customer;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovementServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
     //   System.out.println("MovementServlet");
        String username = req.getParameter(Constants.USERNAME);
        List<Customer.Movement> movementListByCustomerName = ContextListener.engine.getMovementListByCustomerName(username);
     //   System.out.println(movementListByCustomerName);
        List<String> strings =
                movementListByCustomerName.stream().map(Customer.Movement::toString).collect(Collectors.toList());
      //  System.out.println(strings);
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            Map<String,Object> map=new HashMap<>();
            map.put("movements",strings);
            String json = gson.toJson(map);
          //  System.out.println(json);
            out.println(json);
            out.flush();
        }
    }

}
