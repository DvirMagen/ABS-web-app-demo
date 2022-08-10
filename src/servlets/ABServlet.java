package servlets;

import com.google.gson.Gson;
import constants.Constants;
import engine.tools.ABSManager;
import engine.tools.SingleClientEntry;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server_utills.ServletUtils;
import server_utills.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ABServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        response.setContentType("application/json");
        ABSManager systemManager = ServletUtils.getABSManager(getServletContext());
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        /*
        verify chat version given from the user is a valid number. if not it is considered an error and nothing is returned back
        Obviously the UI should be ready for such a case and handle it properly
         */
        int systemVersion = ServletUtils.getIntParameter(request, Constants.ABS_VERSION_PARAMETER);
        if (systemVersion == Constants.INT_PARAMETER_ERROR) {
            return;
        }

        /*
        Synchronizing as minimum as I can to fetch only the relevant information from the chat manager and then only processing and sending this information onward
        Note that the synchronization here is on the ServletContext, and the one that also synchronized on it is the chat servlet when adding new chat lines.
         */
        int systemManagerVersion = 0;
        List<SingleClientEntry> chatEntries;
        synchronized (getServletContext()) {
            systemManagerVersion = systemManager.getVersion();
            chatEntries = systemManager.getChatEntries(systemVersion);
        }

        // log and create the response json string
        SystemAndVersion cav = new SystemAndVersion(chatEntries, systemManagerVersion);
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(cav);
        logServerMessage("Server ABS version: " + systemManagerVersion + ", User '" + username + "' ABS version: " + systemVersion);
        logServerMessage(jsonResponse);

        try (PrintWriter out = response.getWriter()) {
            out.print(jsonResponse);
            out.flush();
        }
    }

    private void logServerMessage(String message){
        System.out.println(message);
    }

    private static class SystemAndVersion {

        final private List<SingleClientEntry> entries;
        final private int version;

        public SystemAndVersion(List<SingleClientEntry> entries, int version) {
            this.entries = entries;
            this.version = version;
        }
    }

}
