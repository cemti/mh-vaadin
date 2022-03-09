package md.cemirtan.magazinhardware;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.function.Consumer;

public final class Util
{
	static
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
	}
	
	private static SessionFactory sessionFactory;
	
	public static Session getSession(String username, String password)
	{
		var p = new Properties();
		p.setProperty("hibernate.connection.username", username);
		p.setProperty("hibernate.connection.password", password);
		p.setProperty("hibernate.connection.url", Core.getJdbcURL());
	
		return new Configuration().configure().setProperties(p).buildSessionFactory().openSession();
	}

	public static void doSession(Consumer<Session> r)
	{
		try (var s = sessionFactory.openSession())
		{
			var t = s.beginTransaction();
			
			try
			{
				r.accept(s);
				t.commit();
				System.out.println();
			}
			catch (Exception e)
			{
				System.err.println(e.getMessage());
				t.rollback();
			}
		}
	}

	public static void main(String[] args)
	{
		System.out.println("Under construction.");
	}
}
