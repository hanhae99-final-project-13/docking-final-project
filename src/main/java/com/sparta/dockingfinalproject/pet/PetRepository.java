package com.sparta.dockingfinalproject.pet;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {

  Page<Pet> findAllByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime startDt,
      LocalDateTime endDt, Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndAddressLikeOrderByCreatedAtDesc(LocalDateTime startDt,
      LocalDateTime endDt, String address, Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtDesc(
      LocalDateTime startDt, LocalDateTime endDt, String ownerType, Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtDesc(
      LocalDateTime startDt, LocalDateTime endDt, String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByOrderByCreatedAtDesc(Pageable pageable);

  Page<Pet> findAllByAddressLikeOrderByCreatedAtDesc(String address, Pageable pageable);

  Page<Pet> findAllByOwnerTypeContainingOrderByCreatedAtDesc(String ownerType, Pageable pageable);

  Page<Pet> findAllByOwnerTypeAndAddressLikeOrderByCreatedAtDesc(String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenOrderByCreatedAtAsc(LocalDateTime startDt, LocalDateTime endDt,
      Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndAddressLikeOrderByCreatedAtAsc(LocalDateTime startDt,
      LocalDateTime endDt, String address, Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndOwnerTypeContainingOrderByCreatedAtAsc(
      LocalDateTime startDt, LocalDateTime endDt, String ownerType, Pageable pageable);

  Page<Pet> findAllByCreatedAtBetweenAndOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(
      LocalDateTime startDt, LocalDateTime endDt, String ownerType, String address,
      Pageable pageable);

  Page<Pet> findAllByOrderByCreatedAtAsc(Pageable pageable);

  Page<Pet> findAllByAddressLikeOrderByCreatedAtAsc(String address, Pageable pageable);

  Page<Pet> findAllByOwnerTypeContainingOrderByCreatedAtAsc(String ownerType, Pageable pageable);

  Page<Pet> findAllByOwnerTypeContainingAndAddressLikeOrderByCreatedAtAsc(String ownerType,
      String address, Pageable pageable);

  List<Pet> findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted isAdopted);
}
