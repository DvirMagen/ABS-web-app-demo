package servlets;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import listener.ContextListener;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientsInformationServlet extends HttpServlet {
    private class ClientInfo implements Serializable {
        public String name;
        public double balance;

        public ClientInfo() {
        }

        public ClientInfo(String name, double balance) {
            this.name = name;
            this.balance = balance;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getBalance() {
            return balance;
        }

        public void setBalance(double balance) {
            this.balance = balance;
        }
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Set<ClientInfo> clientInfoSet = ContextListener.engine.getAllCustomers().stream()
                .map(customerDto -> new ClientInfo(customerDto.getName(), customerDto.getBalance())).collect(Collectors.toSet());

        HashMap<String,Object> map = new HashMap<>();
        map.put("clients", clientInfoSet);
        try (PrintWriter out = resp.getWriter()) {
            Gson gson = new Gson();
            String json = gson.toJson(map);
            out.println(json);
            out.flush();
        }
    }
}
