package misszero.DB;

import misszero.Entities.LinkEntity;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CrawlerDB extends DBBase {

    public void addLink(String code, int type, String url) {

        Connection conn = null;

        try {

            conn = createConnection();
            Statement statement = conn.createStatement();

            String str = "insert into links (Code, Type, Url, CreateTime) values ('%s', %s, '%s', now());";
            String sql = String.format(str, code, type, url);
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

    public void updateLinkToDownloaded(String url, String path) {

        Connection conn = null;

        try {

            conn = createConnection();
            Statement statement = conn.createStatement();

            String str = "update links set Downloaded=1, Path='%s' where Url='%s';";
            String sql = String.format(str, path, url);
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

    public Set<LinkEntity> getLinks() {

        Set<LinkEntity> links = new HashSet<LinkEntity>();
        Connection conn = null;

        try {

            conn = createConnection();
            Statement statement = conn.createStatement();

            String sql = "select * from links";

            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                LinkEntity link = new LinkEntity(rs.getString("Code"), rs.getInt("Type"), rs.getString("Url"), rs.getBoolean("Downloaded"), rs.getString("Path"));
                links.add(link);
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

        return links;
    }

}
