# FlameTalk_Android
<div align="center" style="display:flex;">
	<img src="https://user-images.githubusercontent.com/43838030/153394342-37221ea4-b3cf-4dc4-81b6-4d0b9ed9e46b.png" width="200">
</div>


<div align="center">

### SGS DevCamp2 flame팀의 메신저 clone project입니다🔥
앱 서비스의 기본적인 회원 인증, 연락처 동기화를 통한 친구 추가, 채팅 프로필 커스텀, 검색 기능과 채팅 기능을 제공합니다.
<br/>채팅은 STOMP WebSocket 기반의 실시간 통신으로 이루어지며, FCM을 통해 메세지에 대한 푸시 알림 기능이 있습니다.
</div>

<br/>

## 1. Team member
| Member | Role |
| :-----------------------------------: | :---------------------------------------: | 
| [<img src="https://avatars.githubusercontent.com/paksuua" width="100">](https://github.com/paksuua) </br> <h4>[박소연](https://github.com/paksuua)</h4> |<div align="left"> **- 로그인 회원가입 및 유저 인증 기능 개발**</br> - 연락처 동기화하여 친구 추가 및 친구 상태 관리 (숨김, 차단친구) </br> - 유저 프로필 관리 </br> - 프로필, 배경화면 이미지 히스토리 피드 구현 </br> - 파일 서버 통신 </br> - 친구 리스트 검색 기능 (Room을 이용한 내부 DB 기반 검색) </br> - 프로필 스티커 기능 </div>|
|[<img src="https://avatars.githubusercontent.com/014967" width="100">](https://github.com/014967) </br> <h4> [김현국](https://github.com/014967) </h4> | <div align="left">  - 구글 Auth 회원가입  </br> - Stomp websocket을 통한 채팅 </br> - Room Database를 통한 채팅내역, 채팅방 저장 </br> - 오픈 프로필 생성 및 관리 </br> - [ 개발중 ] Fcm Push Server 구현 </div> |


## 2. Version Info

| Tools | Name | Version |
| :-----: | :-----:|:----: | 
| IDE | Android Studio | 11.0.11 |
| Language | Kotlin | 1.6.10 |


||API Level|
|---|---|
| TargetSDK | 31 (R) |
| MinimumSDK | 26 (Oreo)  |
| compileSDK | 31 (R)|

</br>

## 3. Library
### Android Jetpack 
|                  Library             |          Description   |
| ----------------------------------- | ------------------------------------------- |
| [Navigation](https://developer.android.com/guide/navigation/navigation-getting-started?hl=ko)   |  Fragment간 화면 전환 및 뷰 스택 관리. Single Activity 구조 적용  |
| [KTX](https://developer.android.com/kotlin/ktx)  | Android 생명주기 관리, 확장함수, Coroutine의 이용을 위한 Kotlin 확장 프로그램   |
| [Hilt, Dagger](https://developer.android.com/training/dependency-injection/hilt-android?hl=ko) | 클래스에 컨테이너를 제공하고 수명 주기를 자동으로 관리하는 Android DI 라이브러리 |
| [DataStore](https://developer.android.com/topic/libraries/architecture/datastore?hl=ko#preferences-datastore) |  프로토콜 버퍼를 사용하여 키-값 쌍 또는 유형이 지정된 객체를 저장할 수 있는 데이터 저장소 |
| [Coroutine](https://developer.android.com/kotlin/coroutines?hl=ko) | Android Jetpack과 호환되는 안드로이드 비동기 프로그래밍 솔루션 |
| Coroutine - [Flow](https://developer.android.com/kotlin/flow?hl=ko) | Coroutine 기반의 비동기식 데이터 스트림 |
| Coroutine - [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow?hl=ko) | Flow에서 최적으로 상태 업데이트를 내보내는 관찰 가능한 객체 |
| [Room](https://developer.android.com/training/data-storage/room?hl=ko) | Jetpack에서 권장하는 Android 내부 저장소 관련 솔루션. SQLite를 추상화한 라이브러리 |

### ETC
|                  Library             |          Description   |
| ----------------------------------- | ------------------------------------------- |
| [STOMP Krossbow](https://github.com/joffrey-bion/krossbow) | Coroutine 기반으로 Flow를 지원하는 STOMP Websocket 통신 라이브러리 |
| [Retrofit2](https://square.github.io/retrofit/)  | HTTP REST API 통신 라이브러리  |
| [OkHttp3](https://square.github.io/okhttp/)  | HTTP 기반으로  효율적으로 request/response를 할 수 있도록 지원하는 HTTP 클라이언트 |
| Firebase | [Google OAuth](https://firebase.google.com/docs/auth/android/google-signin?hl=ko) </br> [FCM Push 알림](https://firebase.google.com/docs/cloud-messaging?hl=ko) |
| [Glide](https://github.com/bumptech/glide) | 이미지 로드 및 캐싱 라이브러리 |
| [ViewPager2](https://developer.android.com/training/animation/screen-slide-2?hl=ko) | 스와이프할 수 있는 형식의 뷰 또는 프래그먼트를 지원  |
| [Timber](https://github.com/JakeWharton/timber)     |  로그를 출력하는 라이브러리 (배포 파일 생성 시 로그를 삭제시켜 줌)  |

</br>

## 4. Architecture
</br>
<div align="center">
<img width="396" alt="image"src="https://user-images.githubusercontent.com/43838030/153743074-eed1c053-23e5-4363-95c6-b8bca5784c24.png"></div>

| <h3>Architecture</h3> | <h3>Description</h3> |
|---|---|
| MVVM + AAC | View+ViewModel+Model의 구조로 뷰와 비즈니스 로직을 분리 </br> Android Architecture Components인 Room, Navigation, Fragments 이용 |
| Repository | 네트워크 API 통신과 내부 DB를 일관적인 인터페이스로 접근할 수 있는 아키텍처 패턴 |
