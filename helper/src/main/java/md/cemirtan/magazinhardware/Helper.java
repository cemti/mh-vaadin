package md.cemirtan.magazinhardware;
import java.sql.Connection;
import java.sql.SQLException;

public final class Helper
{	
	// Permite la automatizarea interogarilor multiple
	// Este o forma de protectie impotriva injectiilor SQL
	public static int[] executeBatches(Connection connection, String query, Object[][] values, Integer[] types) throws SQLException
	{
		try (var s = connection.prepareStatement(query))
		{
			for (int j, i = 0; i < values.length; ++i)
			{
				for (j = 0; j < values[i].length; ++j)
				// Am utilizat metoda setObject ca sa pot insera si valori nule
					s.setObject(j + 1, values[i][j], types[j]);
				
				s.addBatch();
			}
			
			return s.executeBatch();
		}
	}
	
	public static String formatQuery(Connection connection, String query) throws SQLException
	{
		var s = Core.executeQuery(connection, query);
		var md = s.getMetaData();
		
		var sb = new StringBuilder();
		int n = md.getColumnCount();
		
		for (int i = 1; i <= n; ++i)
			sb.append(String.format("%-10s ", md.getColumnLabel(i)));
		
		while (s.next())
		{
			sb.append('\n');
			
			for (int i = 1; i <= n; ++i)
				sb.append(String.format("%-10s ", s.getString(i)));
		}
		
		return sb.toString();
	}
	
	// Afiseaza rezultatul tabelar al unei interogari
	// Se garanteaza o exceptie aruncata cand nu este tabelar
	public static void printQuery(Connection connection, String query) throws SQLException
	{
		System.out.println(formatQuery(connection, query));
	}
}
