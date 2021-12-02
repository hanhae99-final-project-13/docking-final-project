package com.sparta.dockingfinalproject.api;

import com.sparta.dockingfinalproject.post.pet.PetRepository;
import com.sparta.dockingfinalproject.post.pet.model.Pet;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@Component
public class PublicApiScheduler {

  private final PublicApiInitializer publicApiInitializer;
  private final PetRepository petRepository;

  public PublicApiScheduler(PublicApiInitializer publicApiInitializer,
      PetRepository petRepository) {
    this.publicApiInitializer = publicApiInitializer;
    this.petRepository = petRepository;
  }

  @Scheduled(cron = "0 0 0/1 * * *")
  public void updatePetStatus()
      throws InterruptedException, IOException, ParserConfigurationException, SAXException {
    System.out.println("공공 API 입양진행상태 업데이트 실행");

    URL url = publicApiInitializer.connectApi();
    NodeList nodeList = publicApiInitializer.getNodeList(url);
    List<Pet> newPetList = publicApiInitializer.getItemValueList(nodeList);

    for (Pet newPet : newPetList) {
      TimeUnit.SECONDS.sleep(1);

      String newPetStatus = newPet.getIsAdopted().toString();
      if (petRepository.findByPetNo(newPet.getPetNo()) != null) {
        Pet pet = petRepository.findByPetNo(newPet.getPetNo());
        pet.updateStatus(newPetStatus);
      }
    }

  }
}
