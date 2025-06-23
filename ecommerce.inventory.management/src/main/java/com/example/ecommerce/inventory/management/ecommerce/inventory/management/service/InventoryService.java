package com.example.ecommerce.inventory.management.ecommerce.inventory.management.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Item;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Reservation;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.repository.ItemRepository;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.repository.ReservationRepository;

import jakarta.transaction.Transactional;

@Service
public class InventoryService {

    @Autowired
    private ItemRepository itemRepo;

    @Autowired
    private ReservationRepository reservationRepo;
    
    public Item addItem(Item item) {
        return itemRepo.save(item);
    }


    @Transactional
    public Reservation reserveItem(Long itemId, String customer, int quantity) {
        // Pessimistic locking ensures no race condition on stock
        Item item = itemRepo.findByIdForUpdate(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        int available = item.getTotalQuantity() - item.getReservedQuantity();
        if (available < quantity) {
            throw new RuntimeException("Not enough stock");
        }

        item.setReservedQuantity(item.getReservedQuantity() + quantity);
        itemRepo.save(item);

        Reservation reservation = new Reservation();
        reservation.setItem(item);
        reservation.setCustomer(customer);
        reservation.setQuantity(quantity);
        reservation.setStatus("RESERVED");
        reservation.setTimestamp(LocalDateTime.now());

        Reservation saved = reservationRepo.save(reservation);

        updateAvailabilityCache(itemId);

        return saved;
    }

    @Transactional
    public void cancelReservation(Long reservationId) {
        Reservation res = reservationRepo.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        Item item = res.getItem();
        item.setReservedQuantity(item.getReservedQuantity() - res.getQuantity());
        itemRepo.save(item);

        reservationRepo.delete(res);

        updateAvailabilityCache(item.getId());
    }
    
 // Get all items
    public List<Item> getAllItems() {
        return itemRepo.findAll();
    }
    
    

    // Delete item by ID
    @CacheEvict(value = "item-availability", key = "#itemId")
    @Transactional
    public void deleteItemById(Long itemId) {
    	if (!itemRepo.existsById(itemId)) {
            throw new RuntimeException("Item with ID " + itemId + " does not exist");
        }
        
        // Delete all reservations linked to the item
        reservationRepo.deleteByItemId(itemId);
        
        // Now delete the item
        itemRepo.deleteById(itemId);
    }

    @Cacheable(value = "item-availability", key = "#itemId")
    public int getAvailableQuantity(Long itemId) {
        Item item = itemRepo.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        return item.getTotalQuantity() - item.getReservedQuantity();
    }

    @CacheEvict(value = "item-availability", key = "#itemId")
    public void updateAvailabilityCache(Long itemId) {
        // This method will evict cache for the item so fresh data will be read next time
    }
    
    
}

