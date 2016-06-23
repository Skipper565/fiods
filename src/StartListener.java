import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Created by sarhan on 6/23/16.
 */
@WebListener
public class StartListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent ev) {
        ServletContext servletContext = ev.getServletContext();
        servletContext.setAttribute("documentUtils", new DocumentUtils());
        servletContext.setAttribute("unzip", new UnZip());
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev) {
    }
}
