package misszero.DB;

import misszero.Entities.LinkEntity;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class CrawlerDB extends DBBase {

    private Connection conn = null;

    public void connect() {
        this.conn = createConnection();
    }

    public void disconnect() {

        if(this.conn == null) {
            return;
        }

        try {

            this.conn.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addLink(String code, int type, String url) {

        try {

            Statement statement = this.conn.createStatement();

            String str = "insert into links (Code, Type, Url, CreateTime) values ('%s', %s, '%s', now());";
            String sql = String.format(str, code, type, url);
            statement.execute(sql);

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

    }

    public void updateLinkToDownloaded(String url, String content) {

        try {

            String sql = "update links set Downloaded=1, Content=? where Url=?;";
            PreparedStatement statement = this.conn.prepareStatement(sql);

            statement.setString(1, content);
            statement.setString(2, url);

            statement.execute();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Set<String> getLinkUrls() {

        Set<String> urls = new HashSet<String>();

        try {

            Statement statement = this.conn.createStatement();

            String sql = "select * from links";

            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                String url = rs.getString("Url");
                urls.add(url);
            }

            rs.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return urls;
    }

    public Set<LinkEntity> getLinks() {

        Set<LinkEntity> links = new HashSet<LinkEntity>();

        try {

            Statement statement = this.conn.createStatement();

            String sql = "select * from links";

            ResultSet rs = statement.executeQuery(sql);

            while(rs.next()) {
                LinkEntity link = new LinkEntity(rs.getString("Code"), rs.getInt("Type"), rs.getString("Url"), rs.getBoolean("Downloaded"), rs.getString("Path"));
                links.add(link);
            }

            rs.close();

        } catch(SQLException e) {
            e.printStackTrace();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return links;
    }

}
