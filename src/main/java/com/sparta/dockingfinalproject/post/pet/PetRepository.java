package com.sparta.dockingfinalproject.post.pet;

import com.sparta.dockingfinalproject.post.pet.model.IsAdopted;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
  List<Pet> findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted isAdopted);

  Pet findByPetNo(String petNo);
}