package com.sparta.dockingfinalproject.pet;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenOrderByCreatedAtDesc(IsAdopted isAdopted, LocalDateTime startDt,
      LocalDateTime endDt, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndAddressLikeOrderByCreatedAtDesc(IsAdopted isAdopted, LocalDateTime startDt,
      LocalDateTime endDt, String address, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtDesc(IsAdopted isAdopted, LocalDateTime startDt, LocalDateTime endDt, String ownerType, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtDesc(IsAdopted isAdopted, LocalDateTime startDt, LocalDateTime endDt, String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByIsAdoptedOrderByCreatedAtDesc(IsAdopted isAdopted, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndAddressLikeOrderByCreatedAtDesc(IsAdopted isAdopted, String address, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndOwnerTypeContainingOrderByCreatedAtDesc(IsAdopted isAdopted, String ownerType, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndOwnerTypeAndAddressLikeOrderByCreatedAtDesc(IsAdopted isAdopted, String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenOrderByCreatedAtAsc(IsAdopted isAdopted, LocalDateTime startDt, LocalDateTime endDt,
      Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndAddressLikeOrderByCreatedAtAsc(IsAdopted isAdopted, LocalDateTime startDt,
      LocalDateTime endDt, String address, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtAsc(IsAdopted isAdopted, LocalDateTime startDt, LocalDateTime endDt, String ownerType, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(IsAdopted isAdopted, LocalDateTime startDt, LocalDateTime endDt, String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByIsAdoptedOrderByCreatedAtAsc(IsAdopted isAdopted, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndAddressLikeOrderByCreatedAtAsc(IsAdopted isAdopted, String address, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndOwnerTypeContainingOrderByCreatedAtAsc(IsAdopted isAdopted, String ownerType, Pageable pageable);

  Page<Pet> findAllByIsAdoptedAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(IsAdopted isAdopted, String ownerType,
      String address, Pageable pageable);

  List<Pet> findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted isAdopted);

  List<Pet> findAllByIsAdoptedOrderByPetNoDesc(IsAdopted isAdopted);

  Pet findByPetNo(String petNo);
}
