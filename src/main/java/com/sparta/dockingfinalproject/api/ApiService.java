package com.sparta.dockingfinalproject.api;

import com.sparta.dockingfinalproject.pet.Pet;
import com.sparta.dockingfinalproject.pet.PetRepository;
import com.sparta.dockingfinalproject.post.Post;
import com.sparta.dockingfinalproject.post.PostRepository;
import com.sparta.dockingfinalproject.user.User;
import com.sparta.dockingfinalproject.user.UserRepository;
import com.sparta.dockingfinalproject.user.UserService;
import com.sparta.dockingfinalproject.user.dto.SignupRequestDto;
import com.sparta.dockingfinalproject.user.dto.UserRequestDto;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


@Component
@RequiredArgsConstructor
public class ApiService {

  private final PetRepository petRepository;
  private final PostRepository postRepository;
  private final UserRepository userRepository;
  private final UserService userService;

  UserRequestDto userRequestDto = new UserRequestDto();
  String username = "";
  User user = new User();

  //API 접속하여 XML 받기
  @PostConstruct
  public List<Pet> addApiList() throws IOException, ParserConfigurationException, SAXException {
    //http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?bgnde=20210101&endde=20211026&pageNo=1&numOfRows=50&upkind=417000&ServiceKey=emAh2BSy7Cp9iKEgAVpuhP4TUmO6CSHXjRKB3fKJO7SdCYceeDIy%2BhxUTnjeLIv6LpAUoWPuvGcWDhePFxZfWQ%3D%3D
    StringBuilder urlBuilder = new StringBuilder(
        "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic"); /*URL*/
    urlBuilder.append(
        "?" + URLEncoder.encode("bgnde", "UTF-8") + "=" + URLEncoder.encode("20210101",
            "UTF-8")); /*시작페이지*/
    urlBuilder.append("&" + URLEncoder.encode("endde", "UTF-8") + "=" + URLEncoder.encode(today(),
        "UTF-8")); /*종료페이지*/
    urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1",
        "UTF-8")); /*페이지 번호*/
    urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("100",
        "UTF-8")); /*페이지당 보여줄 개수*/
    urlBuilder.append("&" + URLEncoder.encode("upkind", "UTF-8") + "=" + URLEncoder.encode("417000",
        "UTF-8")); /*축종코드:개*/
    urlBuilder.append("&" + URLEncoder.encode("ServiceKey", "UTF-8")
        + "=emAh2BSy7Cp9iKEgAVpuhP4TUmO6CSHXjRKB3fKJO7SdCYceeDIy%2BhxUTnjeLIv6LpAUoWPuvGcWDhePFxZfWQ%3D%3D"); /*Service Key*/
    URL url = new URL(urlBuilder.toString());

    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Content-type", "application/json");
    System.out.println("Response code: " + conn.getResponseCode());

    BufferedReader bufferedReader;
    if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
      bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
    } else {
      bufferedReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
    }

    StringBuilder stringBuilder = new StringBuilder();
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      stringBuilder.append(line);
    }
    bufferedReader.close();
    conn.disconnect();

    return getNodeList(url);
  }


  //XML을 DATA화
  private List<Pet> getNodeList(URL url)
      throws ParserConfigurationException, SAXException, IOException {

    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

    String parsingUrl = url.toString();
    Document doc = dBuilder.parse(parsingUrl);

    doc.getDocumentElement().normalize();

    System.out.println("Root tag : " + doc.getDocumentElement().getNodeName());

    NodeList nodeList = doc.getElementsByTagName("item");
    System.out.println("리스트 계 : " + nodeList.getLength());

    List<Pet> pet = saveItemValue(nodeList);

    return pet;
  }


  // item 하위노드 값 꺼내기
  private static String getItemValue(String tag, Element eElement) {
    NodeList childNodeList = null;
    Node childNodeValue = null;
    try {
      childNodeList = eElement.getElementsByTagName(tag).item(0).getChildNodes();
      childNodeValue = (Node) childNodeList.item(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (childNodeValue == null) {
      return null;
    }
    return childNodeValue.getNodeValue();
  }


  // 아이템 정보 추출하여 db에 저장
  private List<Pet> saveItemValue(NodeList nodeList) {

    ArrayList<Pet> list = new ArrayList<>();
    String isAdopted = null;
    Pet pet = new Pet();

    //관리자 로그인 정보 가져오기
    adminLogin();
    username = userRequestDto.getUsername();
    user = userRepository.findByUsername(username).orElseThrow(
        () -> new IllegalArgumentException("유저가 존재하지 않습니다"));

    // for문 돌며 아이템값 pet, post db에 저장하기
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node nNode = nodeList.item(i);
      Element eElement = (Element) nNode;

      String petNo = getItemValue("desertionNo", eElement);
      String breed = getItemValue("kindCd", eElement);

      String sex = getItemValue("sexCd", eElement);
      if (!sex.equalsIgnoreCase("f") && !sex.equalsIgnoreCase("m")) {
        continue;
      }

      String age = getItemValue("age", eElement);
      String weight = getItemValue("weight", eElement);
      String lostLocation = getItemValue("happenPlace", eElement);
      String ownerType = getItemValue("careNm", eElement);

      String careAddr = getItemValue("careAddr", eElement);
      String careAddr1 = careAddr.split("\\s")[0];
      String careAddr2 = careAddr.split("\\s")[1].split("\\s")[0];
      String address = careAddr1 + " " + careAddr2;

      String phone = getItemValue("careTel", eElement);
      String img = getItemValue("popfile", eElement);
      String extra = getItemValue("specialMark", eElement);

      String processState = getItemValue("processState", eElement);
      if (processState.contains("종료")) {
        isAdopted = "보호종료";
      } else {
        isAdopted = "보호중";
      }

      System.out.printf(
          "%s번째 [ %s // %s // %s // %s // %s // %s // %s // %s // %s // %s // %s // %s ]", i, petNo,
          breed, sex, age, weight, lostLocation, ownerType, address, phone, img, extra, isAdopted);

      pet = Pet.builder()
          .breed(breed)
          .sex(sex)
          .age(age)
          .weight(weight)
          .lostLocation(lostLocation)
          .ownerType(ownerType)
          .address(address)
          .phone(phone)
          .img(img)
          .extra(extra)
          .isAdopted(isAdopted)
          .petNo(petNo)
          .build();

      list.add(pet);
      petRepository.save(pet);

      Post post = new Post(pet, user);
      postRepository.save(post);

    }

    return list;
  }


  // 아이템 정보 추출하여 map에 저장
  private void createItemValueMap(NodeList nList) {
    List<Map<String, String>> list = new ArrayList<>();
    for (int i = 0; i < nList.getLength(); i++) {
      Map<String, String> map = new HashMap<>();
      Node nNode = nList.item(i);
      Element eElement = (Element) nNode;

      map.put("petNo", getItemValue("desertionNo", eElement));
      map.put("breed", getItemValue("kindCd", eElement));
      map.put("sex", getItemValue("sexCd", eElement));
      map.put("age", getItemValue("age", eElement));
      map.put("weight", getItemValue("weight", eElement));
      map.put("lostLocation", getItemValue("happenPlace", eElement));
      map.put("ownerType", getItemValue("careNm", eElement));
      map.put("address", getItemValue("careAddr", eElement));
      map.put("phone", getItemValue("careTel", eElement));
      map.put("img", getItemValue("popfile", eElement));
      map.put("extra", getItemValue("specialMark", eElement));
      map.put("isAdopted", getItemValue("processState", eElement));

      list.add(map);

      System.out.println(map);
    }
  }


  //오늘 날짜 계산하기
  public static String today() {
    SimpleDateFormat dtf = new SimpleDateFormat("yyyyMMdd");
    Calendar calendar = Calendar.getInstance();

    java.util.Date datesssObj = calendar.getTime();
    String formattedDate = dtf.format(datesssObj);
    System.out.println(formattedDate);

    return formattedDate;
  }


  private void adminLogin() {
    // 관리자 계정 생성
    SignupRequestDto signupRequestDto = new SignupRequestDto();

    signupRequestDto.setUsername("administrator");
    signupRequestDto.setPassword("docking1023");
    signupRequestDto.setPwcheck("docking1023");
    signupRequestDto.setNickname("관리자");
    signupRequestDto.setEmail("administrator@sparta.com");
	signupRequestDto.setPhoneNumber("0107777777");

    userService.registerUser(signupRequestDto);

    // 관리자 로그인
    userRequestDto.setUsername("administrator");
    userRequestDto.setPassword("docking1023");

//    user = userService.login(userRequestDto);
  }

}
