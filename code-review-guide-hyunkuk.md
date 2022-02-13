---
__Role :)__
- repository패턴 ,유즈케이스 패턴 , clean arhitecture 도입 
- Stomp websocket을 통한 채팅
- Room Database를 통한 채팅내역, 채팅방 저장
- 오픈 프로필 생성 및 관리
- 구글 auth 회원가입  
- [ 개발중 ] Fcm Push Server 구현

## Info
---
이번 프로젝트를 시작으로 처음 언어를 java에서 kotlin으로 구현을 하게 되었고,
또한 Hilt를 이용하여 의존성을 주입하고 clean architecture를 도입하게 되었습니다.


## Clean Architecture

+ Data Layer
  - common
    * Wrapper Class
  - mapper
  - model
    * data model
      - request model
      - response model 
  - repository
    * repositoryImplement class
  - source
    * local
    * remote
+ Domain Layer
  - entity
  - repository
    * repository interface
  - usecase
+ Ui Layer

___   
## Stomp Websocket Flow
websocket관련한 library를 찾아보니 공식적인 라이브러리는 없었습니다.   
하지만 개인 개발자가 만들어 놓은 라이브러리 중 [krossbow library](https://joffrey-bion.github.io/krossbow/)가 coroutine flow
를 지원하여서 사용하게 되었습니다.   

## Connect Websocket
앱이 시작을 하게 되면 laucnh activity인 MainActivity를 처음 도달하게 되므로, 
MainActivity의 ViewModel에서 websocket connection을 진행하고, 연결된 stomp session을 저장했습니다. 

[WebSocket connection]()

ChatFragment에서 MainActivity의 ViewModel을 activityViewModel로 공유하여서 연결된 stomp session의 정보를 가져왔습니다. webscoket이 연결되어 session이 존재한다면, stomp session을 사용하여 message를 보내도록 하였습니다.   

[Websocket push message]()

마찬가지로 webscoket이 연결되어 session이 존재한다면, ChatRoomListFragment에서 safe args로 전달받은 room_id로 메세지를 수신받은 destination을 지정한 뒤 해당 room으로 들어오는 메시지를 수신하고 room 데이터베이스에 저장했습니다.   

[Websocket received message]()

## Room Database with chat message

채팅방을 클릭하여 입장을 했을 때, lastReadMessage를 함께 보내서, websocket이 연결되지 않았을 때 ( 앱을 사용중이지 않을 때 ) lastReadMessage 이후에 채팅데이터를 받아오고 이를 room Database에 저장합니다. 

[Recevied message with lastReadMessage]()

Websocket으로 메시지를 수신하게 되면 Room database에 chat table에 메세지를 저장합니다. 

[Websocket insert received message]()

## Observe Room Database
flow를 통하여 room data에서 메시지가 저장이 된다면, flow의 collect를 통해서 사용자의 ui에 메시지가 보이도록 하였습니다.

[Observe message from room database]()


## Repository pattern, UseCase pattern with Hilt
의존성 주입이라는 것을 처음 공부를 해보고 이를 클린아키텍쳐와 함께 사용하게 되었습니다. 
Di directory 내부에 repositoryModule과 UseCaseModule을 만들었습니다.
repositoryModule은 domain layer에서 생성한 repository에 data layer의 repositoryImpl를 주입합니다.  
UseCaseModule에서는 repository를 UseCase에 주입합니다.

[RepositoryImpl class]()   
[RepositoryModule]()   
[UseCase class]()   
[UseCaseModule]()  

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




