package server_utills;

import constants.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public class SessionUtils {
    public static String getUsername (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getBalance (HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.BALANCE) : null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static int getTimeUnit(HttpServletRequest request)
    {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.YAZ_TIME) : null;
        return sessionAttribute != null ? (int) sessionAttribute : null;
    }

    public static void clearSession (HttpServletRequest request) {
        request.getSession().invalidate();
    }

}
