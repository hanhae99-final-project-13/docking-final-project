package com.sparta.dockingfinalproject.post;

import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import com.sparta.dockingfinalproject.post.repository.PostRepository;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PostDeleteScheduler {

  private final PostRepository postRepository;
  private final PetRepository petRepository;

  public PostDeleteScheduler(PostRepository postRepository, PetRepository petRepository) {
    this.postRepository = postRepository;
    this.petRepository = petRepository;
  }

  @Scheduled(cron = "0 0 3 * * *")
  public void deleteAdoptedPosts() {
    System.out.println("입양완료 3일 경과 Post 삭제");
    List<Pet> petList = petRepository.findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted.ADOPTED);

    for (Pet pet : petList) {
      LocalDateTime modifiedAt = pet.getModifiedAt();
      LocalDateTime now = LocalDateTime.now();

      long betweenDays = ChronoUnit.DAYS.between(modifiedAt, now);
      if (betweenDays >= 3) {
        postRepository.deleteById(pet.getPost().getPostId());
        petRepository.deleteById(pet.getPetId());
      }
    }

  }

  @Scheduled(cron = "0 0 3 * * *")
  public void deleteExpiredPosts() {
    System.out.println("보호종료 Post 삭제");
    List<Pet> petList = petRepository.findAllByIsAdoptedOrderByModifiedAtDesc(IsAdopted.EXPIRED);

    for (Pet pet : petList) {
      postRepository.deleteById(pet.getPost().getPostId());
      petRepository.deleteById(pet.getPetId());
    }
  }

}