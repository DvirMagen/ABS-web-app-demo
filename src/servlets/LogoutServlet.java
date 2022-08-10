package servlets;

import engine.client.ClientManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server_utills.ServletUtils;
import server_utills.SessionUtils;

import java.io.IOException;

public class LogoutServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String usernameFromSession = SessionUtils.getUsername(request);
        ClientManager clientManager = ServletUtils.getClientManager(getServletContext());

        if (usernameFromSession != null) {
            System.out.println("Clearing session for " + usernameFromSession);
            clientManager.removeClient(usernameFromSession);
            SessionUtils.clearSession(request);

            // used mainly for the web version. irrelevant in the desktop client version
            response.sendRedirect(request.getContextPath() + "/index.html");
        }
    }
}
