package com.example.ecommerce.inventory.management.ecommerce.inventory.management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Item;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.service.InventoryService;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @PostMapping("/add")
    public Item addItem(@RequestBody Item item) {
        return inventoryService.addItem(item);
    }

    @PostMapping("/reserve/{itemId}")
    public String reserveItem(@PathVariable Long itemId,@RequestParam String customer ,@RequestParam int quantity) {
        inventoryService.reserveItem(itemId, customer,quantity);
        return "Item reserved successfully.";
    }

    @GetMapping("/available/{itemId}")
    public int getAvailableStock(@PathVariable Long itemId) {
        return inventoryService.getAvailableQuantity(itemId);
    }

    @GetMapping("/all")
    public List<Item> getAllItems() {
        return inventoryService.getAllItems();
    }
    
 // Delete item by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteItem(@PathVariable Long id) {
        try {
            inventoryService.deleteItemById(id);
            return ResponseEntity.ok("Item deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}

