
package com.example.rental_platform.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.rental_platform.model.RentalItemImage;

@Repository
public interface RentalItemImageRepository
        extends JpaRepository<RentalItemImage, Long> {

}