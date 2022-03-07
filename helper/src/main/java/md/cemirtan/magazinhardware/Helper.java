package md.cemirtan.magazinhardware;
import java.sql.SQLException;

public final class Helper
{	
	// Permite la automatizarea interogarilor multiple
	// Este o forma de protectie impotriva injectiilor SQL
	public static void ExecuteBatches(String query, Object[][] values, Integer[] types) throws SQLException
	{
		try (var s = Core.getConnection().prepareStatement(query))
		{
			for (int j, i = 0; i < values.length; ++i)
			{
				for (j = 0; j < values[i].length; ++j)
				// Am utilizat metoda setObject ca sa pot insera si valori nule
					s.setObject(j + 1, values[i][j], types[j]);
				
				s.addBatch();
			}
			
			s.executeBatch();
		}
	}
	
	// Afiseaza rezultatul tabelar al unei interogari
	// Se garanteaza o exceptie aruncata cand nu este tabelar
	public static void PrintQuery(String query) throws SQLException
	{
		var s = Core.ExecuteQuery(query);
		var md = s.getMetaData();
		
		int n = md.getColumnCount();
		
		for (int i = 1; i <= n; ++i)
			System.out.printf("%-10s ", md.getColumnLabel(i));
		
		while (s.next())
		{
			System.out.println();
			
			for (int i = 1; i <= n; ++i)
				System.out.printf("%-10s ", s.getString(i));
		}
		
		System.out.println();
	}
}
