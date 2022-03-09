package md.cemirtan.magazinhardware;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public final class Core
{
	private static String connectionURL = "localhost:3306/Magazin Hardware";
	
	static String getConnectionURL()
	{
		return connectionURL;
	}
	
	static String getJdbcURL()
	{
		return "jdbc:mysql://" + connectionURL;
	}
	
	public static Connection getConnection(String username, String password) throws SQLException
	{
		return DriverManager.getConnection(getJdbcURL(), username, password);
	}
	
	public static ResultSet executeQuery(Connection connection, String query) throws SQLException
	{
		try (var ps = connection.prepareStatement(query))
		{
		// CachedRowSet stocheaza rezultatul interogarii (este valabil si offline)
			var rs = RowSetProvider.newFactory().createCachedRowSet();
			rs.populate(ps.executeQuery());
			return rs;
		}
	}
	
	public static boolean execute(Connection connection, String query) throws SQLException
	{
		try (var ps = connection.prepareStatement(query))
		{
			return ps.execute();
		}
	}
}
