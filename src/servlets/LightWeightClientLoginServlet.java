package servlets;

import constants.Constants;
import engine.client.ClientManager;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import server_utills.ServletUtils;
import server_utills.SessionUtils;
import listener.ContextListener;

import java.io.IOException;

import static constants.Constants.USERNAME;

public class LightWeightClientLoginServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");

        String usernameFromSession = SessionUtils.getUsername(request);
        ClientManager userManager = ServletUtils.getClientManager(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet

            String usernameFromParameter = request.getParameter(USERNAME);
            if (usernameFromParameter == null || usernameFromParameter.isEmpty()) {
                //no username in session and no username in parameter - not standard situation. it's a conflict

                // stands for conflict in server state
                response.setStatus(HttpServletResponse.SC_CONFLICT);
            } else {
                //normalize the username value
                usernameFromParameter = usernameFromParameter.trim();

                /*
                One can ask why not enclose all the synchronizations inside the userManager object ?
                Well, the atomic action we need to perform here includes both the question (isUserExists) and (potentially) the insertion
                of a new user (addUser). These two actions needs to be considered atomic, and synchronizing only each one of them, solely, is not enough.
                (of course there are other more sophisticated and performable means for that (atomic objects etc) but these are not in our scope)

                The synchronized is on this instance (the servlet).
                As the servlet is singleton - it is promised that all threads will be synchronized on the very same instance (crucial here)

                A better code would be to perform only as little and as necessary things we need here inside the synchronized block and avoid
                do here other not related actions (such as response setup. this is shown here in that manner just to stress this issue
                 */
                synchronized (this) {
                    if (userManager.isClientExists(usernameFromParameter)) {
                        String errorMessage = "Client already connected. Please try again later.";

                        // stands for unauthorized as there is already such user with this name
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        response.getOutputStream().print(errorMessage);
                    }
                    else {
                        //add the new user to the users list
                        userManager.addClient(usernameFromParameter, false);
                        if (ContextListener.engine.getCustomerDtoByName(usernameFromParameter) == null)
                            ContextListener.engine.addNewCustomer(usernameFromParameter);
                        //set the username in a session so it will be available on each request
                        //the true parameter means that if a session object does not exists yet
                        //create a new one
                        Double balance = ContextListener.engine.getCustomerDtoByName(usernameFromParameter).getBalance();
                        request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                        request.getSession(true).setAttribute(Constants.BALANCE, balance.toString());
                        //redirect the request to the chat room - in order to actually change the URL
                        System.out.println("On login, request URI is: " + request.getRequestURI());
                        response.setStatus(HttpServletResponse.SC_OK);
                    }
                }
            }
        } else {
            //user is already logged in
            String usernameFromParameter = request.getParameter(USERNAME);
            Double balance = ContextListener.engine.getCustomerDtoByName(usernameFromParameter).getBalance();
            request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
            request.getSession(true).setAttribute(Constants.BALANCE, balance.toString());
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
