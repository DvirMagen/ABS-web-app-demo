package listener;

import engine.Engine;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
@WebListener
public class ContextListener implements ServletContextListener {

    public static Engine engine;
    public static boolean isRewindMode = false;
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("My web app is being initialized :)");
        // TODO: create a new Engien here
        engine = new Engine();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

        System.out.println("My web app is being destroyed ... :(");
    }
}
