package com.sparta.dockingfinalproject.api;

import com.sparta.dockingfinalproject.pet.IsAdopted;
import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@RequiredArgsConstructor
@Component
public class PublicApiScheduler {

  private final PublicApiInitializer publicApiInitializer;
  private final PetRepository petRepository;

  @Scheduled(cron = "0 0 0/1 * * *")
  public void updatePetStatus()
      throws InterruptedException, IOException, ParserConfigurationException, SAXException {
    System.out.println("공공 API 입양상태 업데이트 실행");

    List<Pet> petList = petRepository.findAllByIsAdoptedOrderByPetNoDesc(IsAdopted.of("abandoned"));

    URL url = publicApiInitializer.connectApi();
    NodeList nodeList = publicApiInitializer.getNodeList(url);
    List<Pet> newPetList = publicApiInitializer.getProcessState(nodeList);

    for (int i = 0; i < petList.size(); i++) {

      TimeUnit.SECONDS.sleep(1);

      Pet newPet = newPetList.get(i);
      String newPetStatus = newPet.getIsAdopted().toString();
      Pet pet = petRepository.findByPetNo(newPet.getPetNo());
      String petStatus = pet.getIsAdopted().toString();

      if (petStatus.equals(newPetStatus)) {
        continue;
      }
      pet.updateStatus(newPetStatus);
    }

  }
}
