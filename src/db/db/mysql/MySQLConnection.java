package db.db.mysql;

import db.DBConnection;
import entity.Item;
import external.TicketMasterAPI;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import java.sql.Connection;

/**
 * project: TicketProber
 *
 * @author YubaiTao on 01/02/2018.
 */
public class MySQLConnection implements DBConnection{

    private Connection conn;

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(MySQLDBUtility.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void setFavoriteItems(String userId, List<String> itemIds) {

    }

    @Override
    public void unsetFavoriteItems(String userId, List<String> itemIds) {

    }

    @Override
    public Set<String> getFavoriteItemIds(String userId) {
        return null;
    }

    @Override
    public Set<Item> getFavoriteItems(String userId) {
        return null;
    }

    @Override
    public Set<String> getCategories(String itemId) {
        return null;
    }

    @Override
    public List<Item> searchItems(double lat, double lon, String term) {
        TicketMasterAPI tmAPI = new TicketMasterAPI();
        List<Item> items = tmAPI.search(lat, lon, term);
        for (Item item : items) {
            // Save the item into our own db.
            saveItem(item);
        }
        return items;
    }

    @Override
    public void saveItem(Item item) {
        if (conn == null) {
            return;
        }
        try {
            String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, item.getItemId());
            statement.setString(2, item.getName());
            statement.setDouble(3, item.getRating());
            statement.setString(4, item.getAddress());
            statement.setString(5, item.getImageUrl());
            statement.setString(6, item.getUrl());
            statement.setDouble(7, item.getDistance());
            statement.execute();

            sql = "INSERT IGNORE INTO categories VALUES (?, ?)";
            statement = conn.prepareStatement(sql);
            for (String category : item.getCategories()) {
                statement.setString(1, item.getItemId());
                statement.setString(2, category);
                statement.execute();
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public String getFullname(String userId) {
        return null;
    }

    @Override
    public boolean verifyLogin(String userId, String password) {
        return false;
    }
}
