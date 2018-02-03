package db.db.mysql;

import db.DBConnection;
import entity.Item;
import external.TicketMasterAPI;

import java.sql.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        if (conn == null) {
            return;
        }
        String query = "INSERT IGNORE INTO history (user_id, item_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            for (String itemId : itemIds) {
                statement.setString(1, userId);
                statement.setString(2, itemId);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void unsetFavoriteItems(String userId, List<String> itemIds) {
        if (conn == null) {
            return;
        }
        String query = "DELETE FROM history WHERE user_id = ? and item_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(query);
            for (String itemId : itemIds) {
                statement.setString(1, userId);
                statement.setString(2, itemId);
                statement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Set<String> getFavoriteItemIds(String userId) {
        if (conn == null) {
            return new HashSet<>();
        }
        Set<String> favoriteItems = new HashSet<>();
        try {
            String sql = "SELECT item_id from history WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String itemId = rs.getString("item_id");
                favoriteItems.add(itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return favoriteItems;
    }

    @Override
    public Set<Item> getFavoriteItems(String userId) {
        if (conn == null) {
            return new HashSet<>();
        }
        Set<String> itemIds = getFavoriteItemIds(userId);
        Set<Item> favoriteItems = new HashSet<>();
        try {
            for (String itemId : itemIds) {
                String sql = "SELECT * from items WHERE item_id = ? ";
                PreparedStatement statement = conn.prepareStatement(sql);
                statement.setString(1, itemId);
                ResultSet rs = statement.executeQuery();
                Item.ItemBuilder builder = new Item.ItemBuilder();

                // Because itemId is unique and given one item id there should
                // have
                // only one result returned.
                if (rs.next()) {
                    builder.setItemId(rs.getString("item_id"));
                    builder.setName(rs.getString("name"));
                    builder.setRating(rs.getDouble("rating"));
                    builder.setAddress(rs.getString("address"));
                    builder.setImageUrl(rs.getString("image_url"));
                    builder.setUrl(rs.getString("url"));
                    builder.setCategories(getCategories(itemId));
                }
                favoriteItems.add(builder.build());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return favoriteItems;
    }

    @Override
    public Set<String> getCategories(String itemId) {
        if (conn == null) {
            return new HashSet<>();
        }
        Set<String> categories = new HashSet<>();
        try {
            String sql = "SELECT category FROM categories WHERE item_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, itemId);
            // a return value to receive the result
            ResultSet rs = statement.executeQuery();
            // rs is a pointer that point to -1 position of the table in DB (header)
            // an iterator
            while (rs.next()) {
                categories.add(rs.getString("category"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return categories;
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
        if (conn == null) {
            return null;
        }
        String name = "";
        try {
            String sql = "SELECT first_name, last_name from users WHERE user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                name = String.join(" ", rs.getString("first_name"), rs.getString("last_name"));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return name;
    }

    @Override
    public boolean verifyLogin(String userId, String password) {
        if (conn == null) {
            return false;
        }
        try {
            String sql = "SELECT user_id from users WHERE user_id = ? and password = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
