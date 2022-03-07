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
import java.util.function.Function;

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
				System.err.println(e.getMessage());
				t.rollback();
			}
		}
	}

	static Firma getFirma(Session s, String f)
	{
		return s.find(Firma.class, f);
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
			System.err.println("Eroare la conectare BD.");
			return;
		}

		List.<Consumer<Session>>of(
		// Create
			s ->
			{
				System.out.println("Tranzactia I:");
				Function<String, Firma> firma = f -> getFirma(s, f);
				
				var amd = firma.apply("AMD");
				var amdSet = amd.getProcesorSet();
				
				List.of(
					new Object[] { new Procesor(5873, amd, "Radeon R5", 0.5, 1750), 1, GPU.class },
					new Object[] { new Procesor(7531, amd, "Threadripper", 3.2, 7000), 16, CPU.class },
					new Object[] { new Procesor(9354, amd, "Turion 64", 1.2, 500), 2, CPU.class }
				).forEach(x ->
				{
					amdSet.add((Procesor)x[0]);
					s.merge(amd);
					
					try
					{
						var pu = ((Class<?>)x[2]).getConstructor(Procesor.class, int.class).newInstance(x[0], x[1]);
						s.persist(pu);
						System.out.println(pu);
					}
					catch (Exception e)
					{
						e.printStackTrace();
						System.exit(-1);
					}
				});
				
				var c = new Calculator(
					69782,
					s.find(CPU.class, 9354),
					s.find(GPU.class, 5211),
					new RAM(1087, firma.apply("Kingston"), 16, 18, 4),
					null,
					new Disk(1527, firma.apply("Toshiba"), null, 128)
				);
				
				s.persist(c);
				System.out.println(c);
			},
			s ->
			{
				System.out.println("Tranzactia II:");
				var cls = Calculator.class;
				var c = s.find(Calculator.class, 69782);
				
				try
				{
					cls.getMethod("setRamB", RAM.class)
						.invoke(c, cls.getMethod("getRamA").invoke(c));
					
					cls.getMethod("setDisk", Disk.class)
						.invoke(c, 
							Disk.class.getConstructor(int.class, Firma.class, Integer.class, int.class)
							.newInstance(1527, s.find(Disk.class, 1527).getFirma(), 5200, 1000));
					
					cls.getMethod("setGpu", GPU.class)
						.invoke(c, s.find(GPU.class, 5873));
				}
				catch (Exception e)
				{
					e.printStackTrace();
					System.exit(-1);
				}

				System.out.println(c);
			},
			s ->
			{
				System.out.println("Tranzactia III:");
				s.remove(s.find(Calculator.class, 69782));
				System.out.println("S-a sters calculatorul mentionat precedent.");
			}
		).forEach(Util::doSession);
	}
}
