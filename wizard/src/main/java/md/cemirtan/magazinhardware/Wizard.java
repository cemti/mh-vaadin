package md.cemirtan.magazinhardware;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Collections;

public final class Wizard
{
	public static void main(String[] args)
	{
		String username, password;
		
		try (var stdin = new java.util.Scanner(System.in))
		{
			System.out.print("Username: ");
			username = stdin.nextLine();
			
			System.out.print("Password: ");
			password = stdin.nextLine();
		}

		try
		{
			Core.Connect(username, password);
		}
		catch (SQLException e)
		{
			System.out.println("Eroare la conectare BD.");
			return;
		}
		
		try
		{
		// Inserez valori noi in tabela Calculator
			Helper.ExecuteBatches(
				"INSERT INTO Calculator VALUES (?, ?, ?, ?, ?, ?)",
				new Object[][]
				{
					{ 18907, 5347, 6124, 8465, 5214, 4126 },
					{ 68421, 5714, 8736, 2634, null, 4126 },
					{ 20671, 3461, 3012, 1568, 1568, 5672 }
				},
				Collections.nCopies(6, Types.INTEGER).toArray(new Integer[0])
			);

		// Afisez tabela Calculator
			Helper.PrintQuery("SELECT * FROM Calculator");
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
	}
}
