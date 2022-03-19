package md.cemirtan.magazinhardware;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.Map;

public final class Core
{
	private static final Map<String, String> urlMap = Map.of(
		"mysql", "//localhost:3306/Magazin Hardware",
		"h2", "~/mh_demo"
	);
	
	static String getJdbcURL(String dbms)
	{
		return String.format("jdbc:%s:%s", dbms, urlMap.get(dbms));
	}
	
	public static Connection getConnection(String dbms, String username, String password) throws SQLException
	{
		return DriverManager.getConnection(getJdbcURL(dbms), username, password);
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
