package com.example.ecommerce.inventory.management.ecommerce.inventory.management;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Item;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Reservation;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.repository.ItemRepository;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.repository.ReservationRepository;
import com.example.ecommerce.inventory.management.ecommerce.inventory.management.service.InventoryService;

import jakarta.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.cache.CacheManager;

public class InventoryServiceTest {

    @Mock
    private ItemRepository itemRepo;

    @Mock
    private ReservationRepository reservationRepo;

    @InjectMocks
    private InventoryService inventoryService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddItem() {
        Item item = new Item();
        item.setId(1L);

        when(itemRepo.save(item)).thenReturn(item);

        Item saved = inventoryService.addItem(item);

        assertEquals(item, saved);
        verify(itemRepo).save(item);
    }

    @Test
    public void testReserveItem_Success() {
        Long itemId = 1L;
        String customer = "Alice";
        int quantity = 2;

        Item item = new Item();
        item.setId(itemId);
        item.setTotalQuantity(10);
        item.setReservedQuantity(3);

        when(itemRepo.findByIdForUpdate(itemId)).thenReturn(Optional.of(item));
        when(itemRepo.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));
        when(reservationRepo.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation reservation = inventoryService.reserveItem(itemId, customer, quantity);

        assertEquals(itemId, reservation.getItem().getId());
        assertEquals(customer, reservation.getCustomer());
        assertEquals(quantity, reservation.getQuantity());
        assertEquals("RESERVED", reservation.getStatus());
        assertNotNull(reservation.getTimestamp());

        assertEquals(5, item.getReservedQuantity()); // 3 + 2 reserved

        verify(itemRepo).save(item);
        verify(reservationRepo).save(reservation);
    }

    @Test
    public void testReserveItem_NotEnoughStock() {
        Long itemId = 1L;
        Item item = new Item();
        item.setId(itemId);
        item.setTotalQuantity(5);
        item.setReservedQuantity(4);

        when(itemRepo.findByIdForUpdate(itemId)).thenReturn(Optional.of(item));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inventoryService.reserveItem(itemId, "Bob", 2));
        assertEquals("Not enough stock", ex.getMessage());
    }

    @Test
    public void testCancelReservation_Success() {
        Long reservationId = 100L;

        Item item = new Item();
        item.setId(1L);
        item.setReservedQuantity(5);

        Reservation reservation = new Reservation();
        reservation.setId(reservationId);
        reservation.setItem(item);
        reservation.setQuantity(3);

        when(reservationRepo.findById(reservationId)).thenReturn(Optional.of(reservation));
        when(itemRepo.save(any(Item.class))).thenAnswer(i -> i.getArgument(0));

        inventoryService.cancelReservation(reservationId);

        assertEquals(2, item.getReservedQuantity()); // 5 - 3

        verify(itemRepo).save(item);
        verify(reservationRepo).delete(reservation);
    }

    @Test
    public void testCancelReservation_NotFound() {
        when(reservationRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.cancelReservation(123L));
        assertEquals("Reservation not found", ex.getMessage());
    }

    @Test
    public void testGetAllItems() {
        List<Item> items = Arrays.asList(new Item(), new Item());
        when(itemRepo.findAll()).thenReturn(items);

        List<Item> result = inventoryService.getAllItems();

        assertEquals(2, result.size());
        verify(itemRepo).findAll();
    }

    @Test
    public void testDeleteItemById_Success() {
        Long itemId = 1L;

        when(itemRepo.existsById(itemId)).thenReturn(true);
        doNothing().when(itemRepo).deleteById(itemId);

        assertDoesNotThrow(() -> inventoryService.deleteItemById(itemId));

        verify(itemRepo).deleteById(itemId);
    }

    @Test
    public void testDeleteItemById_ItemNotFound() {
        Long itemId = 999L;

        when(itemRepo.existsById(itemId)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.deleteItemById(itemId));
        assertEquals("Item with ID " + itemId + " does not exist", ex.getMessage());
    }

    @Test
    public void testGetAvailableQuantity() {
        Long itemId = 1L;
        Item item = new Item();
        item.setTotalQuantity(10);
        item.setReservedQuantity(3);

        when(itemRepo.findById(itemId)).thenReturn(Optional.of(item));

        int available = inventoryService.getAvailableQuantity(itemId);

        assertEquals(7, available);
    }

    @Test
    public void testGetAvailableQuantity_ItemNotFound() {
        when(itemRepo.findById(anyLong())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> inventoryService.getAvailableQuantity(1L));
        assertEquals("Item not found", ex.getMessage());
    }
}


