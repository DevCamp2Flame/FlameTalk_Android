


Role :) 김현국
- repository패턴 ,유즈케이스 패턴 , clean arhitecture 도입 
- Stomp websocket을 통한 채팅
- Room Database를 통한 채팅내역, 채팅방 저장
- 오픈 프로필 생성 및 관리
- 구글 auth 회원가입  
- [ 개발중 ] Fcm Push Server 구현
---

## Info     
이번 프로젝트를 시작으로 처음 언어를 java에서 kotlin으로 구현을 하게 되었고,
또한 Hilt를 이용하여 의존성을 주입하고 clean architecture를 도입하게 되었습니다.

### Clean Architecture

+ [Data Layer]()
  - common
    * Wrapper Class
  - mapper
  - model
    * data model
    * request model
    * response model 
  - repository
    * repositoryImplement class
  - source
    * local
    * remote
+ [Domain Layer]()
  - entity
  - repository
    * repository interface
  - usecase
+ [Ui Layer]()

___   
## Stomp Websocket Flow
websocket관련한 library를 찾아보니 공식적인 라이브러리는 없었습니다.   
하지만 개인 개발자가 만들어 놓은 라이브러리 중 [krossbow library](https://joffrey-bion.github.io/krossbow/)가 coroutine flow
를 지원하여서 사용하게 되었습니다.   

## Connect Websocket
앱이 시작을 하게 되면 laucnh activity인 MainActivity를 처음 도달하게 되므로, 
MainActivity의 ViewModel에서 websocket connection을 진행하고, 연결된 stomp session을 저장했습니다. 

[WebSocket connection]()
[WebSocket Module]()

ChatFragment에서 MainActivity의 ViewModel을 activityViewModel로 공유하여서 연결된 stomp session의 정보를 가져왔습니다. webscoket이 연결되어 session이 존재한다면, stomp session을 사용하여 message를 보내도록 하였습니다.   

마찬가지로 webscoket이 연결되어 session이 존재한다면, ChatRoomListFragment에서 safe args로 전달받은 room_id로 메세지를 수신받은 destination을 지정한 뒤 해당 room으로 들어오는 메시지를 수신하고 room 데이터베이스에 저장했습니다.   

[Websocket push received message]()

## Room Database with chat message

채팅방을 클릭하여 입장을 했을 때, lastReadMessage를 함께 보내서, websocket이 연결되지 않았을 때 (앱을 사용중이지 않을 때 ) 수신되었던 메세지를 lastReadMessageId를 사용하여 이 메시지 아이디 이후에 채팅데이터List를 받아오고 이를 room Database에 저장합니다. 

[Recevied message with lastReadMessage]()

Websocket으로 메시지를 수신하게 되면 Room database에 chat table에 메세지를 저장합니다. 

[Websocket insert received message]()

## Observe Room Database
flow를 통하여 room data에서 메시지가 저장이 된다면, flow의 collect를 통해서 사용자의 ui에 메시지가 보이도록 하였습니다.

[Observe message from room database]()


## RecyclerView 간 아이템 클릭 데이터 처리
즐겨찾기 친구와 일반 친구들 중에 선택을 해서 선택된 친구로 이루어진 채팅방을 생성해서 하는 경우가 있었습니다.   
adapter와 fragment간 callback을 통해서, 선택된 사람을 전해받아서 viewModel에 업데이트를 해주는 로직을 작성했습니다.   
일반 친구 recyclerview와 즐겨찾기 친구 recyclerview, 선택된 친구 recyclerview가 각각 존재했기 때문에 선택된 친구 recyclerview에서 친구를 다시 클릭시 원래 있던 recyclerview에서도 activated를 지워줘야 했습니다.    
~~~
data class SelectedTable(var id: String, var position: Int, var adapterFlag: Int)
private var selectedMap = HashMap<String, SelectedTable>()
~~~
Map을 사용하여 어떤 recyclerview의 adapter에서 전달받은 아이템인지 관리하여 
해당 adapter에서 activate를 지울 수 있도록 로직을 구성하게 되었습니다. 

[RecyclerView Data passing]()

## ResponseWrapper와 Mapper

~~~
서버에서 받은 response의 형식
{
  "status": 200,
  "message": "메시지 조회 성공",
  "data":[
    {
        ~~
    }
}
{
    "status": 400,
    "message": "잘못된 요청입니다.",
    "error": "BAD_REQUEST",
    "code": "BAD_REQUEST",
    "timestamp": "2022-01-18T19:30:16.3072905",
}

~~~
서버에서 repsonse를 위와 같이 보내기 때문에 wrapper 클래스를 생성하였고, ui layer에서 일부만 쓰일 response data를 domain layer 의 entity로 변환해주는 mapper클래스를 사용했습니다. 
[ResponseWrapper]()
[Mapper]()

##  sealed Class, StateFlow 사용
~~~
sealed class Results<out T : Any, out U : Any> {
    data class Success<T : Any>(val data: T) : Results<T, Nothing>()
    data class Error<U : Any>(val message: String) : Results<Nothing, U>()
    object Loading : Results<Nothing, Nothing>()
}
sealed class UiState<out T : Any> {
    data class Success<out T : Any>(val data: T) : UiState<T>()
    data class Error(val error: String) : UiState<Nothing>()
    object Loading : UiState<Nothing>()
    object Init : UiState<Nothing>()
}
~~~
또한 stateFlow와 sealed class를 같이 사용하므로써 remote나 db crud 결과를 UiState를 변경하도록 하였습니다. 

~~~
override suspend fun createChatRoom(createChatRoomReq: CreateChatRoomReq): Flow<Results<ChatRoomEntity, WrappedResponse<CreateChatRoomRes>>>
~~~




## Repository implementation

---
## RoomData base 설계
<div align="center" style="display:flex;">
	<img src="https://postfiles.pstatic.net/MjAyMjAyMTNfMTE3/MDAxNjQ0NzUxNDUzNTYz.TjHAVhnwpY_W6e2BZNHzYww9zbo1r5VOOtZx7Kp3ODAg.x1ZVIfeEziBJ2xvk-toEyhvjMzN28thczyYrWlq_k9gg.PNG.boris0815/Screen_Shot_2022-02-13_at_20.22.02_PM.png?type=w966">
</div>

---
## 고찰
[DiffUtils 사용기](https://nonstop-angle-860.notion.site/DiffUtils-01b391ea8f304b8d994c36d7dbeae1e6)


