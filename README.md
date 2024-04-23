## 프로젝트 소개

관심 있는 링크를 아카이빙하고, 허브를 통해서 다른 사람과 공유하기 위한 서비스입니다.
<br/>
[크롬 확장 프로그램 스토어](https://chromewebstore.google.com/detail/%EC%83%A4%EC%9A%B0%ED%8A%B8-%EB%A7%81%ED%81%AC/cgpaoefmiekiijmblfngfophoombnkmm?utm_source=ext_app_menu)
<br/>
[크롬 확장 프로그램 저장소](https://github.com/hseong3243/shout-link-extension)
<br/>
[프론트엔드 저장소](https://github.com/hseong3243/shout-link-front)

## 사용 기술

<div>
  <div>
  <img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"> 
  <img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white">
  <img src="https://img.shields.io/badge/MySQL-4479A1.svg?style=for-the-badge&logo=MySQL&logoColor=white">
  </div>
  <div>
  <img src="https://img.shields.io/badge/amazon ec2-FF9900?style=for-the-badge&logo=amazon ec2&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
  <img src="https://img.shields.io/badge/Nginx-009630?style=for-the-badge&logo=Nginx&logoColor=white">
  </div>
  <div>
  <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=github actions&logoColor=white">
  </div>
</div>

## 주요 기능

- 사용자 인증을 위한 로그인
- 링크 공유를 위한 허브 생성
- 링크 관리를 위한 링크 묶음과 링크 기능
- 도메인 별 아카이빙 된 링크 확인을 위한 도메인 검색
- 유사한 관심사를 가진 허브를 파악하기 위한 태그 기능
- 원활한 아키이빙을 위한 크롬 확장 프로그램 제공

## 코드 구조
![코드 아키텍처](https://github.com/hseong3243/shout-link/assets/48748265/96370d3b-b59e-4c66-a6b1-30a5c8eeef00)

- 소스 코드의 모든 의존성은 바깥에서 안쪽으로 향하도록 작업 하였습니다.
- 이와 같은 구조를 채택한 이유는 테스트하기 쉬운 코드를 작성하기 위해서입니다. 서비스 계층의 단위 테스트는 mockito를 이용하는 대신 stub 객체를 직접 구현하여 이용함으로써 강한 리팩토링 내성을 확보할 수 있었습니다.
- 서로 다른 도메인 간 발생하는 변경에 대한 영향을 최소화하기 위해 순환 참조가 발생하는 경우 인터페이스를 이용한 의존성 역전 원칙 적용 및 이벤트 발행을 통해 의존성을 제거하였습니다.
![shoutlink_code_architecture2 excalidraw 8](https://github.com/hseong3243/shout-link/assets/48748265/ff9047e2-309a-45b9-a6af-c128f3a4c2de)
![shoutlink_code_architecture2 excalidraw 9](https://github.com/hseong3243/shout-link/assets/48748265/228eeb75-e0a0-4ec5-9c6f-e4cc248df2e3)


## 시스템 구조
![시스템 아키텍처](https://github.com/hseong3243/shout-link/assets/48748265/f186dd0e-abff-4b81-89b1-0c5382007731)
- Github Actions를 활용하여 테스트, 배포 자동화를 구축하였습니다.
- AWS의 EC2 인스턴스로 배포가 이루어지며, MySQL을 위한 RDS 인스턴스를 사용 중입니다.
- 프리티어 종료 후에도 다른 인스턴스에 배포할 수 있도록 Docker를 사용하였습니다.
- 무료 인증서 발급 기관인 Let’s Encrypt로부터 SSL/TLS 인증서를 발급받아 HTTPS 통신을 수행합니다.

## 고민한 내용

### Trie 자료 구조를 이용한 도메인 검색어 자동 완성 제공
- 사용자는 도메인 검색을 통해서 해당 도메인에서 다른 사용자가 관심을 가지는 링크를 파악할 수 있어야 합니다.
- 서버는 분산 환경을 고려하지 않고 단일 인스턴스에서 실행 중입니다. 따라서 별도의 외부 저장소를 고려하기 보다 애플리케이션 단에서 캐싱하는 방법을 선택하였습니다.
- 문자열 검색에 있어 유용한 자료 구조인 Tire를 구현하고 최대 35자까지 입력받을 수 있도록 제한하였습니다.
- DB와 정합성을 맞추기 위하여 Spring Scheduler를 이용하여 1시간 마다 갱신을 수행합니다.
![shoutlink_code_architecture2 excalidraw 7](https://github.com/hseong3243/shout-link/assets/48748265/d49c6f31-a27e-4c91-8e9d-7739f2654b01)
### 생성형 AI를 이용한 태그 자동 생성 기능 구현
- 사용자가 태그를 통해서 유사한 관심사를 지닌 허브를 확인할 수 있어야 합니다.
- 사용자가 자신 또는 허브의 관심사를 나타내는 태그를 선택 또는 직접 생성하는 번거로움을 줄이기 위한 기능입니다.
- 생성된 링크 개수가 조건을 만족하는 경우(5개 이상, 5의 배수 개수마다, 태그가 만들어진지 하루가 지난 후) 링크 개수에 비례하여 최대 5개까지 생성합니다.
- 생성형 AI로는 Google Gemini API를 사용하였습니다. 이는 현 시점 무료로 사용할 수 있다는 점, 그러면서 GPT 3.5보다 높은 성능을 보인다는 점에서 선택하였습니다.
![shoutlink_code_architecture2 excalidraw 4](https://github.com/hseong3243/shout-link/assets/48748265/58502d8f-62dc-4e30-b731-7546ca026b9e)


