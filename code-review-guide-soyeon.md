
# Android 박소연 Code Review Guide 문서
## 안녕하세요 Flame팀 안드로이드 개발을 맡은 박소연입니다 :) 이번 프로젝트를 통해 안드로이드 개발에 더욱 흥미를 키울 수 있게 되었고 즐겁게 개발중입니다🥳

저는 FlameTalk에서 아래와 같은 기능을 담당하고 있습니다
- 로그인 회원가입 및 유저 인증 기능 개발
- 연락처 동기화하여 친구 추가 및 친구 상태 관리 (숨김, 차단친구)
- 유저 프로필 관리
- 프로필, 배경화면 이미지 히스토리 피드 구현
- 파일 서버 통신
- 친구 리스트 검색 기능 (Room을 이용한 내부 DB 기반 검색)
- 프로필 스티커 기능

# Code Review Index 

(항목을 클릭하면 빠르게 내용으로 이동합니다🏃‍♀️)
1. [Coroutine Deferred를 이용하여 비동기 통신 지연](#index_1)
2. [ViewModel을 이용한 Fragment간 데이터 공유](#index_2)
3. [SharedPreferences 대신 DataStore.preferences로 로컬 유저 정보 저장](#index_3)
4. [NetworkInterceptor](#index_4)
   - Hilt 구조에서의 token 자동 주입
   - request, response 요청 시 Debug Log 기록
5. [Android Navigation 적용](#index_5)
6. [Repository 패턴을 적용하여 NetworkModule과 RoomModule의 접근](#index_6)
7. [ RoomDB를 이용한 local data 기반 검색](#index_7)
8. [LiveData 대신 StateFlow를 이용](#index_8)
9. [코드의 재사용성에 대한 고민](#index_9)
   - 확장함수 적옹
   - AppBar의 layout의 include
10.  [주소록 전화번호 가져온 후 통신 요청 보내기](#index_10)

## 📚 파일 디렉터리 구조
```
└── flametalk_android
    ├── data
    │   ├── dummy             // 통신 전 UI 테스트를 위한 더미데이터 
    │   ├── model             // 재사용되는 data class 정의
    │   └── source          
    │          └── local      
    │                └── dao  // 로컬 RoomDB 데이터 접근 인터페이스 
    ├── di 
    ├── domain
    │   ├── entity            // DB에 저장할 데이터 모델
    │   └── repository        //  데이터 엑세스 레파지토리
    ├── network
    │   ├── request           //  API request body
    │   ├── response          //  API response body
    │   └── service           // 네트워크 통신 요청 인터페이스
    ├── ui                    // 화면 별 Fragment, ViewModel, Adapter
    └── util                  // 확장함수와 util클래스
```

======

<h1 id="index_1">1. Coroutine Deferred를 이용하여 비동기 통신의 타이밍 문제 해결</h1>

[코드로 바로 이동](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/profile/add/AddProfileViewModel.kt)

### 문제 배경: 
프로필을 생성하는 상황에서 파일 데이터를 통신하는 방식은 2가지가 있습니다.
1. API 요청을 보낼 때 다른 데이터와 Multipart/form으로 변환한 파일 데이터를 함께 요청
2. 파일 서버와 Client 선통신 후 response로 받은 S3 url을 body에 담아 API 요청

1번은 Client에서는 API들을 통해 큰 파일 데이터가 이동하게 되고 이는 서버 통신에 부담이 된다고 판단하여 파일 서버를 Client쪽에 두는 구조를 선택했습니다.
따라서 프로필 생성 시 파일 데이터가 있는 경우 파일 API의 통신 요청을 응답이 올때까지 기다린 후 응답으로 받은 파일의 주소(url)를 프로필 생성 요청에 넣어야 합니다.
<div align="center">
<img width="585" alt="스크린샷 2022-02-14 오전 3 13 58" src="https://user-images.githubusercontent.com/43838030/153768709-b789d04b-2a29-45b5-9cf6-a549833a967e.png"></div>

 Coroutine을 통해 비동기 통신을 하기 때문에 특정 작업의 마무리 시점을 보장받지 못합니다. 따라서 비동기 작업의 응답 시점을 알기 위해 async, await을 이용하여 '파일 생성 API'을 요청하고 Deferred를 리턴받습니다. 이때 Deferred 객체를 await()하면 해당 작업이 끝나기 전까지 다음 작업이 수행되지 않습니다. 따라서 await() 요청 다음에 '프로필 생성 API' 요청을 보냄으로써 비동기 통신의 타이밍 문제를 해결할 수 있었습니다.

자세한 문제 해결 과정은 개인 Notion에 기록했습니다. 
[자세한 문제 해결 과정](https://abrasive-ziconium-edb.notion.site/Coroutine-Coroutine-9b08fa3a58a6424e92cdf48f13ee7294)

</br></br>
<h1 id="index_2">
1. ViewModel을 이용한 Fragment간 데이터 공유</h1>
FlameTalk에서 뷰와 비즈니스 로직을 효과적으로 분리하기 위해 MVVM 아키텍처를 적용했습니다. MVVM이 MVP 아키텍처와 다른 가장 큰 차이점은 뷰와 뷰모델이 N:M관계를 이룰 수 있기 때문에 재사용성이 용이하다는 점 입니다.  
대개 개발자들이 Android MVVM에서 UI Controller와 ViewModel을 1:1로 쓰기도 하지만 MVVM의 특성을 살려보기 위해 프로필 편집 ↔️ 상태메세지 편집에서 ViewModel을 공유하여 데이터를 전달하도록 구현했습니다.

<div align="center">
<img src="https://user-images.githubusercontent.com/43838030/153770515-84f882be-0b1a-4881-9d4f-d8123d66e746.gif" width="250"></div>

(아래의 타이틀을 누르면 코드로 이동합니다.)

### [Add Profile](https://github.com/DevCamp2Flame/FlameTalk_Android/tree/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/profile/add) (Fragment & ViewModel)
프로필 생성 및 통신
### [Edit Profile](https://github.com/DevCamp2Flame/FlameTalk_Android/tree/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/profile/edit) (Fragment & ViewModel)
프로필 수정 및 통신
### [Profile Desc](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/profile/ProfileDescFragment.kt) (Fragment)
상태메세지 수정

<img width="908" alt="스크린샷 2022-02-14 오전 3 13 45" src="https://user-images.githubusercontent.com/43838030/153768700-e24cc965-c7b9-4e93-8508-e81476324773.png">

1) Fragment의 재사용

   ProfileDesc는 사용자의 텍스트 입력을 받는 역할로 Add와 Edit에서 접근할 때 동일하게 동작합니다. 따라서 ProfileDesc는 이전 뷰로부터 뷰 타입 정보를 args로 넘겨받고 Fragment를 재사용할 수 있습니다.
2) ViewModel의 공유

    Add Profile에서 상태메세지 수정을 누르면 ProfileDesc로 이동합니다. Profile Desc에서 입력한 데이터는 UI를 pop했을 경우 이전의 뷰로 데이터를 전달해야 합니다. 
    1. 데이터를 UI Controller 변수에 담아두고 직접 전달
    2. ViewModel의 변수에 저장하고 Fragment가 공유

    1번의 방법은 화면을 회전하는 경우 Fragment가 파괴되었다가 다시 생성되는 생명주기의 변화를 겪으며 데이터가 손실될 가능성이 있습니다. 
    ViewModel의 경우 참조하는 View가 UI 스택에서 완전히 제거되기 전까지 파괴되지 않기 때문에 2번의 경우는 생명주기로 부터 비교적 안전하게 데이터를 전달할 수 있습니다.

## 결과적으로 ProfileDesc는 이전 뷰로부터 뷰 타입 정보를 args로 넘겨받아 사용자가 입력한 데이터를 해당 뷰의 ViewModel의 변수에 저장합니다.


</br></br>

<h2 id="index_3">3. SharedPreferences 대신 DataStore.preferences로 로컬 유저 정보 저장</h2>
안드로이드에서 token과 같은 유저 정보를 디바이스 내에 임시 저장할 때 SharedPreferences를 쓸 수 있습니다. 그러나 최근 SharedPreferences의 PreferenceManager를 deprecated 시키며 DataStore의 preferences의 사용을 권장하고 있습니다.
DataStore의 Preferences는 프로토콜 버퍼를 이용하여 키-값 쌍을 저장할 수 있는 솔루션이며 Kotlin의 Coroutine과 Flow를 지원하여 이번 프로젝트에서 로그인한 유저 정보(닉네임, access-token 등)을 저장하는데 이용했습니다.

[UserPreferences 코드로 이동](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/data/source/local/UserPreferences.kt)

<h2 id="index_4">4. NetworkInterceptor</h2>

   1. Hilt 구조에서의 Header에 데이터 자동 주입

 의존성 주입에 관심이 생겨 이번 프로젝트에 처음으로 Hilt를 기반으로 DI 구조를 적용해봤습니다. Hilt를 적용하여 프로젝트 기반 구조를 적용했습니다. 네트워크 통신 요청 시 Header에 Content-Type과 ACCESS-TOKEN을 넣어줘야 합니다. 이는 api 통신 인터페이스에 직접 @Header로 선언하여 넣어줄 수도 있지만 반복되는 코드의 작성으로 보일러 플레이트라고 판단했습니다. 결과적으로 NetworkInterceptor에서 토큰을 자동 주입하도록 하고 이 객체를 OkHttp에 interceptor로 추가되도록 구현했습니다.
   
   2. Request, Response 로그 남기기

 api 통신 시 요청, 응답 데이터를 확인하기위해 ViewModel에서 직접 로그를 작성해야 하는데 이 작업을 자동화하기 위해 NetworkInterceptor에서 로그를 남기도록 했습니다.

   [NetworkInterceptor 코드로 이동](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/network/NetworkInterceptor.kt)
   
   [NetworkModule 코드로 이동](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/di/module/NetworkModule.kt)



<h2 id="index_5">5. Android Navigation 적용</h2>
FlameTalk 프로젝트는 Single Activity 기반의 구조로 Andriod Jetpack에서 권장하는 Navigation을 이용하여 화면간 이동하도록 구현했습니다. Activity 간 통신이 프로세스간 통신이므로 메모리를 공유하는 Fragment간 통신에 비해 퍼포먼스가 떨어져 상대적으로 무겁다고 할 수 있습니다. Fragment를 이용한 화면 구성은 앱 퍼포먼스를 향상시킵니다. Navigation Component는 1개의 Activity 위에 Fragment로 UI Controller를 구성하는것을 지향하고 있으며 이는 기존의 Activity를 이용한 UI 구현시 보다 앱 용량도 훨씬 줄일 수 있습니다.
또한 SafeArgs가 등장하며 Navigation을 통해 destination을 설정하여 Fragment를 전환할때도 데이터 전달이 가능하게 되었고, UI 백스택의 관리도 편리합니다.

[main_navigation.xml](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/res/navigation/main_navigation.xml)
```

<?xml version="1.0" encoding="utf-8"?>
<navigation xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/main_navigation"
    app:startDestination="@id/navigation_signin"> // 앱의 시작 뷰 설정

    <!--프로필 total 피드-->
    <fragment
        android:id="@+id/navigation_total_feed"
        android:name="com.sgs.devcamp2.flametalk_android.ui.feed.TotalFeedFragment" // 연결할 UI Controller
        android:label="TotalFeed"
        tools:layout="@layout/fragment_total_feed">                 // 연결할 layout

        <action
            android:id="@+id/action_feed_total_to_profile"           
            app:destination="@id/navigation_profile"                // 이동할 목적지
            app:popUpTo="@+id/navigation_profile"                   // pop될 경우 profile로 랜딩
            app:popUpToInclusive="true" />                          // 이 뷰 위에 다른 뷰가 pop될 때 같이 pop

        <argument
            android:name="profileId"                                // 다른 뷰에서 이 뷰로 넘겨줘야할 파라미터
            android:defaultValue="0L"
            app:argType="long" />
        <argument
            android:name="profileImage"
            android:defaultValue=""
            app:argType="string" />
    </fragment>

```
[Fragment에서 선언 방법](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/friend/FriendFragment.kt)
```
    // 친구리스트 > 멀티프로필 생성: 멀티 프로필 만들기
    binding.itemFriendAddProfile.root.setOnClickListener {
        /**파라미터를 없이 뷰 전환*/
        findNavController().navigate(R.id.navigation_add_profile)
    }

    // 내 프로필 미리보기 > 프로필 상세 보기 이동
    binding.lFriendMainUser.root.setOnClickListener {
        /**파라미터를 넣어 뷰 전환*/
        val friendToProfileDirections: NavDirections =
            FriendFragmentDirections.actionFriendToProfile(
                viewType = USER_DEFAULT_PROFILE, profileId = viewModel.userProfile.value!!.id
            )
        findNavController().navigate(friendToProfileDirections)
    }       
```



[Navigation에서 pop을 하는 2가지 방법](https://abrasive-ziconium-edb.notion.site/Navigation-navigateUp-popUpBackStack-175ea242757a4e28814fdddb3e534c36)

<h2 id="index_6">6. Repository 패턴을 적용하여 NetworkModule과 RoomModule의 접근 </h2>
[FriendRepository](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/domain/repository/FriendRepository.kt)
로그인 후 첫 화면에서 친구 목록 서버로부터 받아온 후 이를 RoomDB에 저장하여 이후 검색창에서 친구 리스트 검색에 이용합니다. 따라서 동일한 뷰에서 네트워크 요청과 내부 DB 조작을 하고있습니다.
FlameTalk은 Repository 패턴을 적용하여 NetworkModule과 RoomModule을 동일한 인터페이스로 접근하고 있습니다. 이때 api 통신 요청과 내부 DB 접근은 모두 시간이 오래걸릴 수 있는 작업이므로 백그라운드에서 실행되도록 지정해야 합니다.

## 따라서 Coroutine의 실행 스레드를 지정하기 위한 CoroutineModule을 생성하고 repository에서 withContext()를 통해 백그라운드에서 비동기 처리로 요청을 수행할 수 있습니다.

[CoroutineModule](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/di/module/CoroutineModule.kt)
```
@Module
@InstallIn(SingletonComponent::class)
class CoroutineModule {
    @Provides
    @Singleton
    fun provideIoDispatcher(): CoroutineDispatcher { // 백그라운드에서 실행되도록 지정
        return Dispatchers.IO
    }

    @Provides
    @Singleton
    fun provideExternalScope(): CoroutineScope {
        return GlobalScope
    }
}
```

[FriendRepository](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/domain/repository/FriendRepository.kt)
```
// 친구 리스트 로컬에 저장
    suspend fun insertAllFriends(vararg friends: FriendModel) = withContext(ioDispatcher) {
        friendDAO.get().insertAllFriends(*friends)
    }

    // 친구 리스트 전체 가져오기
    suspend fun getAllFriends() = withContext(ioDispatcher) {
        friendDAO.get().getAllFriends()
    }
```

</br>

<h2 id="index_7">7. RoomDB를 이용한 local data 기반 검색</h2>


<div align="center">
<img src="https://user-images.githubusercontent.com/43838030/153855126-a9537873-a680-418c-a453-28d9cc32d507.gif" width="250">
</div>


FlameTalk에서 친구 목록 검색은 한정된 친구 리스트 데이터를 기반으로한 검색이기 때문에 서버의 부담을 줄여주고자 클라이언트에서 로컬 검색으로 구현했습니다. 
로그인 후 첫 화면인 친구 목록 뷰의 초기화를 위해 서버로부터 친구 목록 데이터를 가져올 때 이를 RoomDB에 friend 테이블에 저장하고 있습니다.

[Entity - FriendModel](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/domain/entity/FriendModel.kt) friend 테이블에 저장될 Entity

[DAO(Data Access Object) - FriendDAO](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/data/source/local/dao/FriendDAO.kt) friend 테이블의 데이터에 접근할 수 있는 인터페이스

[Repository - FriendRepository](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/domain/repository/FriendRepository.kt) 친구 데이터를 네트워크와, 로컬에서 가져오는 repository

로컬의 데이터를 가져오는 작업 또한 오래걸리는 무거운 작업이므로 Coroutine을 이용하여 백그라운드 스레드를 이용한 비동기 작업으로 진행하도록 했습니다.
아래와 같이 Coroutine Flow
```
 // 친구 리스트 로컬에 저장
    suspend fun insertAllFriends(friends: List<FriendModel>) = withContext(ioDispatcher) {
        db.friendDao().insertAllFriends(friends)
    }

    // 친구 리스트 전체 가져오기
    suspend fun getAllFriends() = withContext(ioDispatcher) {
        db.friendDao().getAllFriends().flowOn(ioDispatcher)
    }
```
[ViewModel - SearchViewModel](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/search/SearchViewModel.kt) 실질적인 검색 비즈니스 로직을 수행

UI의 초기화와 뷰모델 생성 시 로컬 저장소로부터 검색에 쓰일 데이터를 가져오고 이를 map함수를 통해 검색어를 포함하는지 확인하여 검색을 구현했습니다. 문자열 알고리즘에서 가장 성능이 좋은 KMP 알고리즘은 O(n)의 시간복잡도를 가지고 있습니다. Android Framework의 contains를 설명하는 코드를 보면 contains는 내부적으로 indexOf를 이용하고 있으며 indexOf의 시간복잡도 또한 O(n)을 가지고 있어 결과적으로 contains를 이용하여 O(n) 성능을 가진 검색을 구현했습니다. KMP 알고리즘을 직접 구현하려 했으나 검색 UI에 이후에 채팅방 검색이 추가될 가능성이 있어 팀원과 협업을 위해 보다 가독성이 좋은 contains를 선택하게 되었습니다.


```
 init {
        // 뷰모델 생성 시 친구 전체 목록 가져옴
        viewModelScope.launch {
            friendRepository.get().getAllFriends().collectLatest {
                _allFriends.value = it
            }
        }
    }

    // 검색어 입력 후 이벤트 날릴 때 호출
    fun searchFriend(input: String) {
        var result: ArrayList<FriendModel> = arrayListOf()

        if (_allFriends.value.isNullOrEmpty()) {
            _message.value = "친구 데이터가 없습니다."
        } else {
            if (!input.isNullOrEmpty()) {
                _allFriends.value!!.map {
                    if (it.nickname.contains(input)) {
                        result.add(it)
                    }
                }
            } else {
            }
        }
        _searchedFriend.value = result
    }
```

</br>

<h2 id="index_8">8. LiveData 대신 StateFlow를 이용</h2>
LiveData는 Android에서 권장하는 AAC로 UI에서 ViewModel의 LiveData 객체를 관찰하여 데이터의 변경사항이 UI로 자동적으로 반영됩니다.
이전에 프로젝트에서 LivaData를 사용해봤지만 LivaData는 뷰를 반드시 거쳐야 데이터가 관찰되기 때문에 View 로직에 적용할때 유리한 것으로 알고있습니다. Flow는 Coroutine의 범위에 상관없이 Model 계층의 데이터의 수집하는 특성으로 Data 로직에 사용하기 좋습니다. StateFlow는 이 두 특성을 포함된 개념으로 UI 상태를 지켜보고 변경된 상태가 화면에 지속되도록 ViewModel에서 상태 지속할 수 있으며 flow의 데이터를 StateFlow 객체 저장할 수 있습니다.

- UI Controller에서 관찰하는 방법
```
// 로그인된 유저의 닉네임 띄움
        lifecycleScope.launch {
            viewModel.nickname.collectLatest {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), "${it}님 로그인 되었습니다.", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_friend)
                }
            }
        }

```
- [ViewModel](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/signin/SigninViewModel.kt)

[LivaData 대신 StateFlow 적용기](https://abrasive-ziconium-edb.notion.site/Flow-LiveData-StateFlow-14b57895d8f34f6ab2eb73a84d180578)

<h2 id="index_9">9. 코드의 재사용성에 대한 고민</h2>
1. 재사용을 위해 확장함수 적용
반복되는 기능의 구현으로 인한 보일러플레이트를 줄이고 코드의 가독성을 높이고자 util 디렉터리에 확장함수를 만들어 사용하고 있습니다. 확장함수의 일부 구현 사례입니다.

[ViewExt](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/util/ViewExt.kt)

2. AppBar layout의 재사용

앱 상단바의 경우 유사한 모양이 반복됩니다. 똑같은 파일을 여러개 만들지 않고 item으로 하나의 레이아웃을 만들고 이를 include하여 각각의 UI에 맞춰 이용하며 레이아웃의 재사용성을 높였습니다. 다만 include 또한 뷰 레이어를 한층 더 깊게 한다는 한계점이 있기 때문에 이후에 merge로 전환해볼 예정입니다.

[FriendFragment.kt](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/friend/FriendFragment.kt)

fragment_friend.xml
```
<include
        android:id="@+id/ab_friend"
        layout="@layout/ab_main"
        android:layout_width="match_parent"
        android:layout_height="70dp"
        app:layout_constraintTop_toTopOf="parent" />
``` 

<h2 id="index_10">10. 주소록 전화번호 가져온 후 통신 요청 보내기</h2>
이번 프로젝트에서 연락처 데이터를 가져와 서버로 '연락처 리스트 기반 친구 추가 API' 통신 요청을 보내는 기능을 구현했습니다. 처음엔 연락처 가져오는 작업을 Fragment에서 수행했으나 연락처 데이터가 많은 실기기에서 테스트 시 메인스레드의 부담이 생겨 백그라운드 스레드에서 동작하도록 변경했습니다(네트워크와 IO 작업과 같이 시간이 오래 걸리는 작업은 Background 동작해야 합니다. 오래 걸리는 작업으로 인해 UI 컴포넌트를 그리는 작업을 5초 이상 방해받으면 ANR이 발생하며 앱이 비정상종료됩니다.)
연락처를 가져오는 작업이 끝난 후 통신 요청을 보내야하기 때문에 Coroutine의 deferred를 이용하여 비동기 동작이 끝난 시점 이후에 친구 추가 통신 요청을 보내도록 구현했습니다.

[코드 바로가기](https://github.com/DevCamp2Flame/FlameTalk_Android/blob/develop/app/src/main/java/com/sgs/devcamp2/flametalk_android/ui/friend/FriendViewModel.kt)

[주소록 내 전화번호 가져오기 글](https://abrasive-ziconium-edb.notion.site/Contacts-4c9864307a3f4c6e902e707121256e11)


