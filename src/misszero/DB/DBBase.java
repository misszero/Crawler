package misszero.DB;


import java.sql.*;

public class DBBase {

    public Connection createConnection() {

        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://127.0.0.1:3306/crawler?useUnicode=true&characterEncoding=UTF-8";
        String user = "root";
        String password = "abc123";
        Connection conn = null;

        try {
            Class.forName(driver);

            conn = DriverManager.getConnection(url, user, password);

        } catch(ClassNotFoundException e) {

            System.out.println("Sorry,can`t find the Driver!");
            e.printStackTrace();

        } catch(SQLException e) {

            e.printStackTrace();

        } catch(Exception e) {

            e.printStackTrace();
        }

        return conn;
    }

}
