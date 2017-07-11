package hr.fer.zemris.java.gallery.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Razred koji implementira sučelje {@link ServletContextListener}. Unutar ovog
 * razreda u metodu {@link #contextInitialized(ServletContextEvent)} sprema se
 * referenca na primjerak sučelja {@link ServletContext} koji je servlet
 * container zgenerirao.
 * 
 * <p>
 * Razred je također implementiran u duhu oblikovnog obrasa jedinstveni objekt,
 * a kako bi se spremljeni primjerak sučelja {@link ServletContext} mogao
 * dohvatiti kroz cijelu aplikaciju
 * </p>
 * 
 * @see ServletContextListener
 * @see ServletContext
 * 
 * @author Davor Češljaš
 */
@WebListener
public class ServletContextProvider implements ServletContextListener {

	/**
	 * Statička varijabla koja predstavlja spremljeni primjerak sučelja
	 * {@link ServletContext}
	 */
	private static ServletContext context;

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		context = sce.getServletContext();
	}

	/**
	 * Metoda koja dohvaća spremljeni primjerak sučelja {@link ServletContext}.
	 *
	 * @return spremljeni primjerak sučelja {@link ServletContext}.
	 */
	public static ServletContext getContext() {
		return context;
	}
}
