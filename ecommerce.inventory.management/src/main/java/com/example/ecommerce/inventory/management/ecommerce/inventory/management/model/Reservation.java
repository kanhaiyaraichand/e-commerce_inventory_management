package com.example.ecommerce.inventory.management.ecommerce.inventory.management.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customer;

    private int quantity;

    private String status;

    private LocalDateTime timestamp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    // ==== Constructors ====

    // No-arg constructor (required by JPA)
    public Reservation() {
    }

    // All-arg constructor
    public Reservation(String customer, int quantity, String status, LocalDateTime timestamp, Item item) {
        this.customer = customer;
        this.quantity = quantity;
        this.status = status;
        this.timestamp = timestamp;
        this.item = item;
    }

    // ==== Getters and Setters ====

    public Long getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public Item getItem() {
        return item;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
