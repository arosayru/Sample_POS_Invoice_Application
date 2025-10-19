package com.pos.dao;

import com.pos.model.Item;
import com.pos.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    public void addItem(Item item) throws SQLException {
        String sql = "INSERT INTO items (item_code, item_name, category, cost, wholesale_price, retail_price, label_price, credit_price, status, image_path) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getItemCode());
            ps.setString(2, item.getItemName());
            ps.setString(3, item.getCategory());
            ps.setDouble(4, item.getCost());
            ps.setDouble(5, item.getWholesalePrice());
            ps.setDouble(6, item.getRetailPrice());
            ps.setDouble(7, item.getLabelPrice());
            ps.setDouble(8, item.getCreditPrice());
            ps.setString(9, item.getStatus());
            ps.setString(10, item.getImagePath());

            ps.executeUpdate();
        }
    }

    public List<Item> getAllItems() throws SQLException {
        List<Item> items = new ArrayList<>();
        String sql = "SELECT * FROM items ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setItemCode(rs.getString("item_code"));
                item.setItemName(rs.getString("item_name"));
                item.setCategory(rs.getString("category"));
                item.setCost(rs.getDouble("cost"));
                item.setWholesalePrice(rs.getDouble("wholesale_price"));
                item.setRetailPrice(rs.getDouble("retail_price"));
                item.setLabelPrice(rs.getDouble("label_price"));
                item.setCreditPrice(rs.getDouble("credit_price"));
                item.setStatus(rs.getString("status"));
                item.setImagePath(rs.getString("image_path"));
                items.add(item);
            }
        }
        return items;
    }

    public void updateItem(Item item) throws SQLException {
        String sql = "UPDATE items SET item_code=?, item_name=?, category=?, cost=?, wholesale_price=?, retail_price=?, label_price=?, credit_price=?, status=? WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, item.getItemCode());
            ps.setString(2, item.getItemName());
            ps.setString(3, item.getCategory());
            ps.setDouble(4, item.getCost());
            ps.setDouble(5, item.getWholesalePrice());
            ps.setDouble(6, item.getRetailPrice());
            ps.setDouble(7, item.getLabelPrice());
            ps.setDouble(8, item.getCreditPrice());
            ps.setString(9, item.getStatus());
            ps.setInt(10, item.getId());

            ps.executeUpdate();
        }
    }

    public void deleteItem(int id) throws SQLException {
        String sql = "DELETE FROM items WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
