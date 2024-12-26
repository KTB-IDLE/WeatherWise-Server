# 실시간 날씨 정보와 AI가 결합된 새로운 날씨 플랫폼, WeatherWise

</br>

</br>

### 🔖 프로젝트 개요
  - **주제** : GitHub 데이터를 활용한 LLM 기반 이력서 자동 생성 서비스
  - **대상** : 이력서 작성에 어려움을 느끼는 예비 개발자

</br>

### 🎯 목표
  - **AI 기반 이력서 생성 및 수정**: 사용자의 GitHub 데이터를 분석하여 실제 기업에 제출 가능한 이력서를 AI와 함께 손쉽게 제작 및 수정
  - **커뮤니티 환경 제공**: 사용자 간 이력서를 공유하고, 피드백을 주고받을 수 있는 협력의 장 제공


</br>

### 📚 기술 스택

### **Frontend**
- ⚛️ **React**: 사용자 인터페이스(UI) 개발을 위한 라이브러리

### **Backend**
- 🌱 **Spring Boot**: 강력한 백엔드 애플리케이션 개발 프레임워크
- 🐬 **MySQL**: 데이터 저장 및 관리에 사용된 관계형 데이터베이스
- 🚀 **Redis**: 캐싱 및 실시간 데이터 저장
- 💬 **Kafka**: 메시지 큐 및 데이터 스트리밍 도구로 실시간 채팅 및 알림 구현

### **DevOps / Infrastructure**
- 🛠️ **Jenkins**: CI/CD 자동화를 위한 도구
- 📦 **Docker**: 애플리케이션 컨테이너화
- ☸️ **Kubernetes**: 컨테이너 오케스트레이션 및 관리
- 🐙 **Argo**: DevOps 워크플로우 관리
- ☁️ **AWS**: 클라우드 인프라 호스팅
- 🔀 **Nginx**: 리버스 프록시 및 웹 서버

### **AI / Automation**
- 🐍 **Python**: 데이터 처리 및 AI 모델 개발
- 🔗 **LangChain**: AI 어플리케이션 구축을 위한 프레임워크

### **Collaboration / Tools**
- 🐙 **GitHub**: 버전 관리 및 소스 코드 관리
- 📝 **Notion**: 프로젝트 관리 및 문서화
- 🎮 **Discord**: 팀 커뮤니케이션
- 🎨 **Figma**: UI/UX 디자인

</br>


### 🔗 주요 기능

## 1️⃣ 회원 및 인증 관리 시스템
- **카카오 OAuth2 로그인**: 카카오 API를 사용해 소셜 로그인을 구현.
- **Spring Security와 JWT**:
  - JWT를 통한 토큰 기반 인증 및 권한 관리.
  - Access Token과 Refresh Token을 활용해 세션 유지.
- **비밀번호 암호화**: BCrypt 암호화 알고리즘을 사용해 안전한 사용자 비밀번호 저장.
- **닉네임 중복 방지**: 회원가입 시 닉네임의 고유성을 보장.

---

## 2️⃣ 미션 생성 및 인증
- **미션 생성**:
  - 관리자가 미션을 생성해 사용자들에게 제공.
  - 미션 데이터는 JPA로 관리되며, MySQL 데이터베이스에 저장.
- **미션 인증**:
  - 사용자가 미션을 완료하면 인증 요청을 제출.
  - Redis를 사용해 인증 데이터의 임시 저장 및 빠른 검증 제공.
  - 동시성 제어를 통해 다중 사용자 요청에 대한 데이터 일관성 보장.

---

## 3️⃣ 커뮤니티
- **게시글 위치 기반 필터링**:
  - 사용자 위치 데이터를 받아 반경 5km 이내 게시글 목록 제공.
  - MySQL의 공간 데이터를 활용한 효율적인 검색 구현.
- **좋아요와 안좋아요 버튼**:
  - Redis 캐싱을 활용해 실시간으로 좋아요/안좋아요 수 업데이트.
  - 사용자 경험을 향상시키는 빠른 응답 속도 제공.
- **투표 시 동시성 제어**:
  - 분산 환경에서 데이터 정합성을 유지하기 위해 Redis와 락(lock) 메커니즘 사용.
  - 동일 사용자가 중복 투표를 방지.

---

## 4️⃣ 기상특보 오픈채팅
- **기상특보 데이터 업데이트**:
  - Spring Scheduler를 활용해 1시간마다 기상청 API 호출.
  - 기상특보 발령 시 '시/도' 기준으로 실시간 단체 오픈채팅방 자동 생성.
- **실시간 채팅**:
  - **Redis**:
    - 최신 채팅 메시지 100개를 캐싱하여 빠른 채팅 로드 제공.
    - 사용자 간 실시간 메시지 교환 성능 향상.
  - **WebSocket + STOMP**:
    - 실시간 양방향 통신 구현.
    - 채팅방 내 메시지 브로드캐스팅 및 실시간 알림 기능 제공.
- **WebFlux와 R2DBC를 활용한 확장**:
  - WeatherWise-Server-Chatting 레포지토리에서 MSA 구조로 구현.
  - WebFlux와 R2DBC를 사용해 비동기 처리로 확장성과 성능 향상.

</br>

### 🌈 개선 사항

  #### 1️⃣ 채팅 성능 개선 - <ins>실행 시간 약 84% 감소</ins>

  </br>

  **문제 상황**
  - 정기 결제 및 사용권 초기화를 배치로 처리하고 있는데, 현재는 배치 로직도 엄청 복잡하지 않고, 데이터 양도 많지 않지만, 나중에 데이터가 많아지게될 경우 성능 저하가 우려됨

  **1. 순차처리 → 병렬처리**
  - 기존에 순차적으로 진행하던 작업을 병렬로 처리
  - 성능 개선은 확실히 됐지만, 로그에 각각의 update 쿼리가 개별적으로 실행되고 있는 것을 확인

  **2. JPA → JDBC 전환**
  - 쿼리가 개별적으로 실행되면서 배치로 일괄처리 하는것의 이점을 전혀 살리지 못하고 있다고 판단
  - JPA의 객체 매핑 기능이 매우 편리하긴 하나, 트랜잭션 관리, 변경 감지 등의 추가적인 기능이 필요하지 않고, 오히려 오버헤드가 발생해 성능이 저하되는 것을 확인
  - 기존 JpaItemWriter을 JdbcBatchItemWriter로 변경해 설정해둔 Chunk Size를 그대로 반영해 Batch Update가 되도록 수정해 성능을 개선

  **결과**
  
  <img width="742" alt="스크린샷 2024-12-17 15 33 20" src="https://github.com/user-attachments/assets/ef411e3b-e757-47df-8572-8102f83dc10d" />

</br>  

  #### 2️⃣ 선착순 쿠폰 이벤트 (모놀리식 -> MSA 마이그레이션) - <ins>TPS 약 2배 향상</ins>

</br>
  
  **문제 상황**
  - Resume 모듈에서 좋아요를 누르거나, 댓글을 작성하는 경우, Notification 모듈로 알림 생성 요청을 보내는데, 동기로 요청을 보내다 보니 요청이 많아질수록 응답속도와 처리량이 저하됨
  - 알림 시스템 특성상 다수의 사용자와 대규모 트래픽을 처리해야할 것으로 예상되는데, 기존 동기 방식으로는 감당할 수 없을 것 같아 테스트 진행

</br>
  
  **JMeter 쓰레드 그룹 설정**  

  <img width="343" alt="스크린샷 2024-12-03 16 35 00" src="https://github.com/user-attachments/assets/fce290f1-b10e-4de5-9594-0bffb3143263">

</br>
  
  **1. WebClient(blocking)**  
  
  <img width="821" alt="스크린샷 2024-12-03 16 37 00" src="https://github.com/user-attachments/assets/e2b810d2-9524-4c30-8518-2bc7ffca16c3">
<img width="1018" alt="스크린샷 2024-12-03 16 35 32" src="https://github.com/user-attachments/assets/857e290a-99dc-4de4-bc47-a8be415ccd8c">

**평균 응답속도** : 1304ms  
**99% 선 (최악의 경우)** : 4155ms  
**TPS** : 365.4/sec  

</br>
  
  **2. WebClient(non-blocking)**  
  
  <img width="817" alt="스크린샷 2024-12-03 16 52 31" src="https://github.com/user-attachments/assets/2da3d7af-d668-4d46-a1ca-a9639f75c9d3">
<img width="1508" alt="스크린샷 2024-12-03 16 52 44" src="https://github.com/user-attachments/assets/63562a46-24f8-4ee5-9ff2-dc5d51e3af07">

**평균 응답속도** : 987ms  
**99% 선 (최악의 경우)** : 4162ms  
**TPS** : 477.8/sec  

</br>
  
  **3. Kafka**
  
  <img width="815" alt="스크린샷 2024-12-03 16 47 37" src="https://github.com/user-attachments/assets/ae488926-c6e6-46b5-b9ff-6091cd53a2ef">
<img width="1507" alt="스크린샷 2024-12-03 16 48 15" src="https://github.com/user-attachments/assets/bf180818-607f-49b6-9c57-162a157a5c96">

**평균 응답속도** : 608ms  
**99% 선 (최악의 경우)** : 2245ms  
**TPS** : 761.3/sec  

</br>

**결론**
확장성 측면에서는 Kafka가 훨씬 더 좋을 것으로 예상되나, 트래픽이 엄청나게 몰리는 것이 아니고 비용적인 측면을 고려해야 한다면 WebClient 비동기 요청도 괜찮을 것 같음

---

   
</br>

  **<개선 전>**
  <img width="1337" alt="스크린샷 2024-11-12 11 06 11" src="https://github.com/user-attachments/assets/c8d814f9-6273-4dc6-86c2-750fbef3c266">
<img width="1338" alt="스크린샷 2024-11-12 11 55 36" src="https://github.com/user-attachments/assets/090f7855-1126-44f4-8f0b-520a9c4dfac2">
<img width="1335" alt="스크린샷 2024-11-12 13 30 15" src="https://github.com/user-attachments/assets/305cc866-441a-4364-991f-79983b0294d6">

  **<개선 후>**
  <img width="1338" alt="스크린샷 2024-11-12 13 56 30" src="https://github.com/user-attachments/assets/17f06a25-d0d9-4939-a7d5-0cd354680550">
<img width="1337" alt="스크린샷 2024-11-12 11 53 50" src="https://github.com/user-attachments/assets/0a5ab0d1-569a-4c08-b5a7-c83ec3eeabf0">
<img width="1334" alt="스크린샷 2024-11-12 13 53 27" src="https://github.com/user-attachments/assets/7279f68c-686d-48f9-bfa1-52d9a901905f">

</br>



</br>

### 🚀 트러블 슈팅

  #### 1️⃣ **조회수 집계 구현시 동일 사용자가 새로고침할 때마다 계속해서 조회수가 증가**
  - 특정한 조건 없이 해당 이력서를 조회할 때마다 조회수가 올라가는 것은 조회수 정보의 신뢰성을 떨어뜨릴 수 있다고 판단
  - Redis에 이력서 ID와 사용자 IP 주소를 합친 문자열을 1시간 동안 저장하도록 설정해 해결
    - 처음 이력서를 조회한 경우 조회수가 증가하지만 그 이후 1시간 동안은 동일 이력서를 조회해도 조회수 집계되지 않음
    - Redis의 TTL설정을 통해 편리하게 관리 가능
  #### 2️⃣ **MongoDB에서 페이지네이션 적용시 10만건 데이터 기준 총 8000 페이지에서 약 4000 페이지부터 메모리 관련 에러 발생**
  - MongoDB는 기본적으로 쿼리나 집계 파이프라인 수행시 메모리에서만 작업을 처리하는 것을 확인
  - 이 과정에서 대량의 문서를 정렬하고 집계하다보니 메모리를 초과해 에러가 발생하던 것
    - 특히 페이징 처리시 전체 페이지와 전체 문서 개수 집계 과정에서 메모리가 초과됨
  - allowDiskUse(true) 설정을 통해 디스크를 추가로 사용해 연산을 완료할 수 있도록 허용해 해결
  #### 3️⃣ **배치 작업 시 Kafka와 Spring Batch의 트랜잭션 충돌 문제 발생**
  - Payment 모듈의 배치작업과 Member 모듈의 배치작업을 순차적으로 진행해야 하는 상황
    - Payment 모듈의 작업 완료 이벤트를 Kafka를 통해 받으면 Member 모듈이 작업을 시작
    - 그러나 Kafka의 트랜잭션과 Spring Batch의 트랜잭션이 충돌하면서 배치작업이 실패
  - Kafka 이벤트 핸들러의 Transactional 설정을 Propagation.NOT_SUPPORTED 로 설정해 기존에 생성된 트랜잭션이 있든 없은 트랜잭션 없이 진행하도록 설정
  - Batch Job을 실행시키는 로직을 별도의 메서드로 빼고, Propagation.REQUIRES_NEW 로 설정하여 새로운 트랜잭션에서 실행하도록 설정해 문제를 해결

