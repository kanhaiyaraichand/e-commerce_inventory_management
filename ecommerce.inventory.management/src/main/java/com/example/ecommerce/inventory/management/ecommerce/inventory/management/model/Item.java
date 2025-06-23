package com.example.ecommerce.inventory.management.ecommerce.inventory.management.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "item")
public class Item implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "reserved_quantity", nullable = false)
    private int reservedQuantity;

    // ==== Constructors ====

    // No-arg constructor (required by JPA)
    public Item() {
    }
    
    //two args
    public Item(String name,int totalQuantity) {
    	this.name=name;
    	this.totalQuantity=totalQuantity;
    }

    // All-arg constructor
    public Item(String name, int totalQuantity, int reservedQuantity) {
        this.name = name;
        this.totalQuantity = totalQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    // ==== Getters and Setters ====

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getReservedQuantity() {
        return reservedQuantity;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public void setReservedQuantity(int reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    // Convenience method
    public int getAvailableQuantity() {
        return totalQuantity - reservedQuantity;
    }
}
