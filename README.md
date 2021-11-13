# getting-final-project


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

<img src = "https://user-images.githubusercontent.com/80088918/141607475-bc0c659b-dc59-4d9a-b7f8-0e5114a56e69.jpg" width="700" >



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


#### 리팩토링 
- 리팩토링 전
```java
publicMap<String,Object> home(UserDetailsImpluserDetails) {
    Pageablepageable =PageRequest.of(0, 6);
    Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
    List<Post> posts = postPage.getContent();
    List<PostDetailResponseDto> postList = new ArrayList<>();
    for (Postpost : posts) {
        PostDetailResponseDtopostDetailResponseDto =PostDetailResponseDto.getPostDetailResponseDto(post);
        postList.add(postDetailResponseDto);
    }
    Map<String,Object> data = new HashMap<>();
    data.put("postList", postList);
    data.put("alarmCount",alarmRepository.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser()));
    returnSuccessResult.success(data);
}

@Transactional
publicMap<String,Object> getPost(LongpostId,UserDetailsImpluserDetails) {
    PostfindPost = bringPost(postId);
    LonguserId =userDetails.getUser().getUserId();
    Optional<Wish> findWish = null;

    boolean heart = false;
    if (userId != null) {
        if (userDetails!= null) {
            findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);
            if (findWish.isPresent()) {
                heart = true;
            }
            findPost.addViewCount();
        }
	PostDetailResponseDtopostResponseDto =PostDetailResponseDto.getPostDetailResponseDto(findPost, heart);

	ArrayList<CommentResultDto> commentDtoList = new ArrayList<>();
	List<CommentResponseDto> commentResponseDto = commentRepository.findAllByPost(findPost);
        for (CommentResponseDtocrd : commentResponseDto) {
	LongcommentId = crd.getCommentId();
	Stringcomment = crd.getComment();
	LocalDateTimecreatedAt = crd.getCreatedAt();
	LocalDateTimemodifiedAt = crd.getModifiedAt();
	Stringnickname = crd.getUser().getNickname();
	Stringurl = crd.getUser().getUserImgUrl();
	CommentResultDtocommentResultDto = new CommentResultDto(commentId, comment, nickname, url, createdAt, modifiedAt);
        commentDtoList.add(commentResultDto);
    }
    Map<String,Object> data = new HashMap<>();
    data.put("post", postResponseDto);
    data.put("commentList", commentDtoList);
    returnSuccessResult.success(data);
  }
}
```

- 리팩토링 후
```java
publicMap<String,Object> home(UserDetailsImpluserDetails) {
    List<Post> posts = getPagePostSix();  
    
    Map<String,Object> data = new HashMap<>();
    data.put("postList", getPostList(posts));
    data.put("alarmCount", getAlarmCount(userDetails));

    returnSuccessResult.success(data);
}

private List<Post> getPagePostSix() {
    Pageable pageable = PageRequest.of(0, 6);
    Page<Post> postPage = postRepository.findAllByOrderByCreatedAtDesc(pageable);
    return postPage.getContent();
}

private List<PostPreviewDto> getPostList(List<Post> posts) {
    List<PostPreviewDto> postList = new ArrayList<>();
    for (Post post : posts) {
      PostPreviewDto postPreviewDto = PostPreviewDto.of(post);
      postList.add(postPreviewDto);
    }
    return postList;
}

private int getAlarmCount(UserDetailsImpl userDetails) {
    if (userDetails != null) {
      return alarmRepository.findAllByUserAndStatusTrueOrderByCreatedAtDesc(userDetails.getUser()).size();
    }
    return 0;
}

@Transactional
public Map<String, Object> getPost(Long postId, UserDetailsImpl userDetails) {
  Post findPost = bringPost(postId);
  boolean heart = getHeart(userDetails, findPost);

  findPost.addViewCount();

  PostDetailResponseDto postResponseDto = PostDetailResponseDto.getPostDetailResponseDto(findPost, heart);

  Map<String, Object> data = new HashMap<>();
  data.put("post", postResponseDto);
  data.put("commentList", getCommentList(findPost));

  return SuccessResult.success(data);
}

private Post bringPost(Long postId) {
  return postRepository.findById(postId).orElseThrow(
      () -> new DockingException(ErrorCode.POST_NOT_FOUND)
  );
}

private boolean getHeart(UserDetailsImpl userDetails, Post findPost) {
    if (userDetails != null) {
        Optional<Wish> findWish = wishRepository.findAllByUserAndPost(userDetails.getUser(), findPost);
        if (findWish.isPresent()) {
            return true;
        }
    }
    return false;
}

private ArrayList<CommentResultDto> getCommentList(Post findPost) {
  ArrayList<CommentResultDto> commentDtoList = new ArrayList<>();

  List<CommentResponseDto> commentResponseDto = commentRepository.findAllByPost(findPost);
  for (CommentResponseDto crd : commentResponseDto) {
    CommentResultDto commentResultDto = getCommentResult(crd);
    commentDtoList.add(commentResultDto);
  }
  return commentDtoList;
}
```
