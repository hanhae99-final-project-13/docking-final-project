package com.sparta.dockingfinalproject.pet;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findAllByOrderByCreatedAtDesc(Pageable pageable);

  List<Pet> findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted isAdopted);

  Pet findByPetNo(String petNo);
}
