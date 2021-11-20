package shop.ourshopping.db;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

/*
@Getter
final class Info {
	final private String host = "5TDG4XuAA8WXUiaRrxPHxQ==:afTS8txIEThZAuS16C6KmP77NAA+tHBaHevHojk+Mbs50yls02b/3S5ID+lb4zs0Vt6+ADTCnYnb7M6nCNooQLrSriQ0FyWydZboumKeV7Y=";
	final private String user = "+5RU76UEWi8chkOz87weTA==:aENON48RnYhfG+UM/3/j8w==";
	final private String password = "iZ5HpcOTXajldzmIF7Q3Vw==:++X1/dLNh2pKBChalpfmgg==";
}
*/

// jdbc 연결, 해제, 롤백 동작을 정의
@Component
public class DBConnection {

	/*private static DB instance;

	public static DB getInstance() {
		if(instance == null) {
			instance = new DB();
//			try {
//				Class.forName("org.mariadb.jdbc.Driver");
//			} catch (Exception ex) {
//				ex.printStackTrace();
//			}
		}
		
		return instance;
	}*/

	@Autowired
	private ApplicationContext applicationContext;

	@Getter
	private Connection conn;
	@Getter
	@Setter
	private PreparedStatement ps;
	@Getter
	@Setter
	private CallableStatement cs;

	public Connection getConnection() {
		try {
			/*
  			SecretKeySpec key = Encryption.createSecretKey("mariadb".toCharArray());
  			Info sqlInfo = new Info();
  			return DriverManager.getConnection(Encryption.decrypt(sqlInfo.getHost(), key), Encryption.decrypt(sqlInfo.getUser(), key), Encryption.decrypt(sqlInfo.getPassword(), key));
		 	*/
			//ApplicationContext context = ApplicationContextUtils.getApplicationContext();
			DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");
			conn = dataSource.getConnection();
			conn.setAutoCommit(false);
	        return conn;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public void disConnection() {
		try {
			if (conn != null) {
				conn.setAutoCommit(true);
				conn.close();
			}
			if (ps != null) {
				ps.close();
			}
			if (cs != null) {
				cs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void rollback() {
		try {
			if (conn != null) {
				conn.rollback();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}