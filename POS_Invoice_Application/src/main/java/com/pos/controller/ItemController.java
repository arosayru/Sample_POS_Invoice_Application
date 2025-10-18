package com.pos.controller;

import com.pos.dao.ItemDAO;
import com.pos.model.Item;
import java.sql.SQLException;
import java.util.List;

public class ItemController {
    private final ItemDAO itemDAO;

    public ItemController() {
        this.itemDAO = new ItemDAO();
    }

    public void addItem(Item item) throws SQLException {
        if (item.getItemCode().isEmpty() || item.getItemName().isEmpty()) {
            throw new IllegalArgumentException("Item Code and Name are required!");
        }
        itemDAO.addItem(item);
    }

    public List<Item> getAllItems() throws SQLException {
        return itemDAO.getAllItems();
    }

    public void updateItem(Item item) throws SQLException {
        if (item.getId() <= 0) {
            throw new IllegalArgumentException("Invalid item ID!");
        }
        itemDAO.updateItem(item);
    }

    public void deleteItem(int id) throws SQLException {
        if (id <= 0) {
            throw new IllegalArgumentException("Invalid item ID!");
        }
        itemDAO.deleteItem(id);
    }
}
