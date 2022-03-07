package md.cemirtan.magazinhardware;
import org.hibernate.cfg.Configuration;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import java.util.List;
import java.util.Scanner;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.function.Consumer;
import java.util.stream.IntStream;

public final class Util
{
	static
	{
		Logger.getLogger("org.hibernate").setLevel(Level.OFF);
	}
	
	private static SessionFactory sessionFactory;

	static void doSession(Consumer<Session> r)
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
				System.out.println(e.getMessage());
				t.rollback();
			}
		}
	}
	
	static void read(Session s, String q)
	{
		s.flush();
		s.createNativeQuery(q, Procesor.class).getResultList().forEach(System.out::println);
	}

	public static void main(String[] args)
	{
		try (var stdin = new Scanner(System.in))
		{
			var p = new Properties();
			
			System.out.print("Username: ");
			p.setProperty("hibernate.connection.username", stdin.nextLine());
			
			System.out.print("Password: ");
			p.setProperty("hibernate.connection.password", stdin.nextLine());
			
			sessionFactory = new Configuration().configure().setProperties(p).buildSessionFactory();
		}
		catch (HibernateException e)
		{
			System.out.println("Eroare la conectare BD.");
			return;
		}

		List.<Consumer<Session>>of(
		// Create
			s ->
			{
				System.out.println("Tranzactia I:");

				List.of(
					new Procesor(9561, "Nvidia", "rTx TiTaN", 1.5, 7500),
					new Procesor(5873, "AMD", "Radeon R5", 0.5, 750),
					new Procesor(7531, "AMD", "Threadripper", 3.2, 7000)
				).forEach(s::persist);

				read(s, "SELECT * FROM Procesor");
			},
		// Update
			s ->
			{
				System.out.println("Tranzactia II:");

				s.find(Procesor.class, 5873).setPret(1750);
				s.find(Procesor.class, 9561).setModel("Titan RTX");

			// Simulez un scenariu
				read(s, "SELECT * FROM Procesor WHERE ID IN (5873, 9561)");
			},
		// Delete
			s ->
			{
				System.out.println("Tranzactia III:");

				IntStream.of(9561, 5873, 7531).forEach(x -> s.remove(s.find(Procesor.class, x)));

				read(s, "SELECT * FROM Procesor");
			}
		).forEach(Util::doSession);
	}
}
