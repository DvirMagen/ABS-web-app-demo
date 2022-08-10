package server_utills;

import engine.client.ClientManager;
import engine.tools.ABSManager;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;

import static constants.Constants.INT_PARAMETER_ERROR;

public class ServletUtils {
    private static final String USER_MANAGER_ATTRIBUTE_NAME = "userManager";
    private static final String ABS_MANAGER_ATTRIBUTE_NAME = "abs_Manager";

    /*
    Note how the synchronization is done only on the question and\or creation of the relevant managers and once they exists -
    the actual fetch of them is remained un-synchronized for performance POV
     */
    private static final Object clientManagerLock = new Object();
    private static final Object absManagerLock = new Object();

    public static ClientManager getClientManager(ServletContext servletContext) {

        synchronized (clientManagerLock) {
            if (servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(USER_MANAGER_ATTRIBUTE_NAME, new ClientManager());
            }
        }
        return (ClientManager) servletContext.getAttribute(USER_MANAGER_ATTRIBUTE_NAME);
    }

    public static ABSManager getABSManager(ServletContext servletContext) {
        synchronized (absManagerLock) {
            if (servletContext.getAttribute(ABS_MANAGER_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ABS_MANAGER_ATTRIBUTE_NAME, new ABSManager());
            }
        }
        return (ABSManager) servletContext.getAttribute(ABS_MANAGER_ATTRIBUTE_NAME);
    }

    public static int getIntParameter(HttpServletRequest request, String name) {
        String value = request.getParameter(name);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException numberFormatException) {
            }
        }
        return INT_PARAMETER_ERROR;
    }
}
