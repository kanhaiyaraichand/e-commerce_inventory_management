package com.example.ecommerce.inventory.management.ecommerce.inventory.management.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.inventory.management.ecommerce.inventory.management.model.Reservation;

import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	@Modifying
	@Transactional
	@Query("DELETE FROM Reservation r WHERE r.item.id = :itemId")
	void deleteByItemId(@Param("itemId") Long itemId);

}
