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
  <img src="https://img.shields.io/badge/amazon rds-527FFF?style=for-the-badge&logo=amazon rds&logoColor=white">
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=Docker&logoColor=white">
  <img src="https://img.shields.io/badge/Nginx-009630?style=for-the-badge&logo=Nginx&logoColor=white">
  </div>
  <div>
  <img src="https://img.shields.io/badge/github actions-2088FF?style=for-the-badge&logo=github actions&logoColor=white">
  </div>
</div>

## 주요 기능

- 회원가입, 로그인
- 링크 공유를 위한 허브 생성
- 링크 관리를 위한 링크 묶음, 링크 추가, 삭제
- 링크 도메인 검색
- 사용자, 허브 태그 자동 생성
- 원활한 아키이빙을 위한 크롬 확장 프로그램 제공

## 시스템 구조
![시스템 아키텍처](https://github.com/hseong3243/shout-link/assets/48748265/f186dd0e-abff-4b81-89b1-0c5382007731)
- Github Actions를 활용하여 테스트, 배포 자동화를 구축
- 무료 인증서 발급 기관인 Let’s Encrypt로부터 SSL/TLS 인증서를 발급받아 HTTPS 통신을 수행

## 고민한 내용

### Trie 자료 구조를 이용한 링크 도메인 검색어 자동 완성 구현
- **목적**
  - 요구사항: 사용자는 링크 도메인(예. github.com) 검색을 통해 도메인과 연관된 링크를 알 수 있습니다.
- **해결**
  - 문자열 검색에 유리한 Trie 자료 구조를 구현하여 애플리케이션 단에서 검색어를 캐싱
  - TrieNode의 구현은 멀티스레드 환경에서의 동시성을 제어하기 위해 ConcurrentHashMap, AtomicBoolean을 사용
  - ExecutorService, CountDownLatch를 활용하여 테스트 코드를 통한 동시성 검증을 수행
  - 캐시 전략은 write-through를 적용하고 애플리케이션 시작시 캐시 워밍을 수행하여 DB와의 정합성 유지

### Gemini API를 이용한 태그 자동 생성 기능 구현
- **목적**
    - 요구사항: 사용자는 태그를 통해서 유사한 관심사를 지닌 허브를 확인할 수 있어야 합니다.
- **해결**
  - 사용자, 공유 허브의 관심사를 나타내기 위해 태그를 직접 선택하는 번거로움을 줄이기 위해 생성형 AI를 활용
  - 새로운 링크 추가 시 아카이빙 한 링크의 총 갯수가 조건을 만족했을 때 자동 태그 생성
  - Spring Event를 이용해 링크 생성과 태그 생성이라는 서로 다른 관심사를 분리
  - 태그 이벤트 리스너가 링크 생성 이벤트를 받아 자동 태그 생성을 호출

![shoutlink_code_architecture2 excalidraw 4](https://github.com/hseong3243/shout-link/assets/48748265/58502d8f-62dc-4e30-b731-7546ca026b9e)


