package md.cemirtan.magazinhardware;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;

public final class Core
{
	private static Connection connection;
	
	static Connection getConnection()
	{
		return connection;
	}
	
	static void Connect(String username, String password) throws SQLException
	{
		if (connection != null)
			connection.close();
	
		connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Magazin Hardware", username, password);
	}
	
	static ResultSet ExecuteQuery(String query) throws SQLException
	{
		try (var ps = connection.prepareStatement(query))
		{
		// CachedRowSet stocheaza rezultatul interogarii (este valabil si offline)
			var rs = RowSetProvider.newFactory().createCachedRowSet();
			rs.populate(ps.executeQuery());
			return rs;
		}
	}
	
	static boolean Execute(String query) throws SQLException
	{
		try (var ps = connection.prepareStatement(query))
		{
			return ps.execute();
		}
	}
}
