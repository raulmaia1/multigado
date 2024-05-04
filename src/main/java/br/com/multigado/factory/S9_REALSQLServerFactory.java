package br.com.multigado.factory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import br.com.multigado.dao.OrdemServicoDAOSQLServerJTDS;
import br.com.multigado.dao.ProdutoDAOSQLServerJTDS;

public class S9_REALSQLServerFactory {
	
	private static Connection con;

	public static Optional<Connection> getConection() {
		try {
			Class.forName("net.sourceforge.jtds.jdbc.Driver");

			String URL = geraURL();

			if ((con == null) || (con.isClosed())) {
				con = DriverManager.getConnection(URL,"sa", "Senha123");
			}
			return Optional.ofNullable(con);
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
			throw new RuntimeException();
		} 
	}

	public static Connection getCon() {
		return con;
	}
	
	private static String geraURL() {
		String URL = "jdbc:jtds:sqlserver://172.15.27.40:1433/S9_Real";
//		String URL = "jdbc:jtds:sqlserver://172.15.27.40:1433/S9_Real";
		return URL;
	}
	
	
	public static void main(String[] args) {
		S9_REALSQLServerFactory.getConection().ifPresent(System.out::println);
		
//		new OrdemServicoDAOSQLServerJTDS().ordemServicoAbertas().get().forEach(o ->{
//			DateTimeFormatter forPattern = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss");
//			DateTime dateTime1 = forPattern.parseDateTime(o.getDataGravacao());
//			
//			
//			DateTime dateTime2 = new DateTime();
//			
//			
//			Duration duration = new Duration(dateTime1, dateTime2);
//			
////			System.out.println(duration.getStandardDays());
////			System.out.println(duration.getStandardHours());
////			System.out.println(duration.getStandardMinutes()/60.00);
////			
////			System.out.println(Minutes.minutesBetween(dateTime1, dateTime2).getMinutes());
//			System.out.println(o.getDataGravacao());
//			System.out.println(o.getProdutoBean());
//			System.out.println(o.getDuracaoOS());
////			879/60 = 14
////			60 * 14 = 840
////			879 -840 = 39;
//			
//			new ProdutoDAOSQLServerJTDS().procuraProdutoPorID(2197).ifPresent(produtoBean ->{
//				System.out.println(produtoBean);
//			});
//			
//		});
		
	}
}
