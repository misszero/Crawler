package misszero.DB;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CrawlerDB extends DBBase {

    public void addLink(String code, int type, String url, String path) {

        Connection conn = null;

        try {

            conn = createConnection();
            Statement statement = conn.createStatement();

            String str = "insert into links (Code, Type, Url, Path, CreateTime) values ('%s', %s, '%s', '%s', now());";
            String sql = String.format(str, code, type, url, path);
            statement.execute(sql);

            conn.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public Set<String> getLinkUrls() {

        Set<String> urls = new HashSet<String>();
        Connection conn = null;

        try {

            conn = createConnection();
            Statement statement = conn.createStatement();

            String sql = "select * from links";

            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                String url = rs.getString("Url");
                urls.add(url);
            }

            rs.close();
            conn.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return urls;
    }
}
