# docking-final-project


:house_with_garden:Intro 
========================

너도 가족을 찾니?
나도 가족을 찾아!

유기견 입양을 생각하고 있다면, 입양의 A to Z

우리의 운명적인 만남 "GETTING개팅:laughing:"

:ledger:메인기능
=========================
- 유기견 반려견 교육자료와 퀴즈 제공
- 개인으로서 유기견 분양 게시글을 올릴 수 있으며, 보호소에 등록된 유기견들도 조회
- 사용자가 직접 입양 신청서를 업로드 가능
- 문자 서비스
- JWT로그인, 소셜로그인
- 알림 서비스를 통해 입양 후 케어 서비스 제공

🚀DEMO
==========================

|                                                                                                           회원가입:closed_lock_with_key: |로그인:key:|
|--------------------------------------------------------------------------------------------------------------------------------|---|
|<img src = "https://user-images.githubusercontent.com/80088918/141507977-bfd22590-af0f-4e9e-bca0-e1d4f1aa9581.gif" width="200" >|<img src = "https://user-images.githubusercontent.com/80088918/141508027-5c43efea-d180-49b7-8077-af8c81016e9d.gif" width="200" >|




|                                                                                                           교육이수:mortar_board: |
|--------------------------------------------------------------------------------------------------------------------------------|
|<img src = "https://user-images.githubusercontent.com/80088918/141513256-af9a1583-3563-450a-9e1c-6a9a6210ce94.gif" width="200" >|


|                                                                                                            분양 게시글 작성 :pencil2:|입양 신청서 작성:pencil2:|
|--------------------------------------------------------------------------------------------------------------------------------|---|
|<img src = "https://user-images.githubusercontent.com/80088918/141517814-c28b2afa-2bdc-4fcb-a60c-59d69c48c2ed.gif" width="200" >|<img src = "https://user-images.githubusercontent.com/80088918/141517851-b9c256ed-4eeb-4420-9ee2-9524b03cbeef.gif" width="200" >|



|                                                                                                         검색조회 :crystal_ball:|
|--------------------------------------------------------------------------------------------------------------------------------|
|<img src = "https://user-images.githubusercontent.com/80088918/141520147-9d1c7156-ba30-4b7b-8bc2-f3c7881e27af.gif" width="200" >|


|                                                                                                         댓글 CRUD :crystal_ball:|
|--------------------------------------------------------------------------------------------------------------------------------|
|<img src = "https://user-images.githubusercontent.com/80088918/141599414-81b08db4-2954-4c43-8b8c-cb942425dd8c.gif" width="200" >|



## :mag_right: Project Architecture :mag_right:

<img src = "https://user-images.githubusercontent.com/80088918/141522884-f5682b71-43f3-41d4-aa24-221e2865f6e2.jpg" width="500" >



## FrontEnd  - 어려웠던 점:balloon:

1. Footer를 원하는 페이지에만 나타나게 하기가 어려웠습니다.  
    
    Footer는 라우트가 아닌 컴포넌트 였기 때문에  Footer에서 location, match, history 를 사용하여 원하는 페이지에서만 나오게 예외처리 할 수가 없습니다. 
    
    해결 방법으로 Footer에 withRouter 라는 Hook을 사용하여 props로 history 받아와 pathname으로 원하는 페이지에만 나타나게 구현하였습니다.
    
2. 리덕스 스토어에 저장된 데이터가 렌더링 보다 먼저 불러져서 undefined  오류가 뜨는 경우가 많았습니다.  이것의 해결 방법으로 데이터가 아직 불러와지지 못 했다면 로딩 화면을 리턴해주게 하였고 그렇지 않다면 통과하게 했으며 이 방식을 통해 해결 하엿습니다.
(리턴문을 사용할 수 없는 경우에는 부모에게서 props를 통해 데이터를 받아오는 방식으로 처리했습니다. 이 경우에는 undefined이 뜨지 않습니다.)


## BackEnd - 어려웠던 점:balloon:


#### jpa 순환참조 오류

<img src = "https://user-images.githubusercontent.com/80088918/141600277-6a7a1dd4-a333-43d6-9efc-7410e8286f60.jpg" width="500" >

 -  application 에 `spring.jackson.serialization.fail-on-empty-beanse=false`
<img src = "https://user-images.githubusercontent.com/80088918/141600308-62caac66-c9ba-4299-b5ad-b2d4a7a069de.jpg" width="500" >

 - @JasonManagedReference, @JsonBackReference추가
<img src = "https://user-images.githubusercontent.com/80088918/141600314-b932f7d8-aa26-4e99-bffd-f8e0e868dc72.jpg" width="500" >
