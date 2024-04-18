## 프로젝트 소개

관심 있는 링크를 아카이빙하고, 허브를 통해서 다른 사람과 공유하기 위한 서비스입니다.
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

- 소스 코드의 모든 의존성은 안쪽을 향합니다.
- 패키지간 순환 의존성이 발생하지 않도록 인터페이스를 이용해 의존성을 역전, 이벤트 발행을 통해 의존성을 끊었습니다.

## 시스템 구조
![시스템 아키텍처](https://github.com/hseong3243/shout-link/assets/48748265/f186dd0e-abff-4b81-89b1-0c5382007731)

## 고민한 내용

- Trie 자료 구조를 이용한 도메인 검색어 자동 완성 제공
  - 사용자는 도메인 검색을 통해서 도메인 별로 어떤 링크가 많이 아카이빙 되었는지 파악할 수 있어야 합니다.
  - Trie 자료 구조를 구현하여 루트 도메인(예. github.com)의 문자열 최대 35자까지 애플리케이션 단에서 캐싱
  - Spring Scheduler를 활용하여 1시간마다 정합성 갱신 수행
- 자동 태그 생성을 위한 생성형 AI 이용
  - 사용자는 태그를 통해서 유사한 관심사를 가지고 있는 허브를 쉽게 파악할 수 있어야 합니다.
  - 사용자가 직접 태그를 선택 또는 생성하는 번거로움을 줄이기 위한 기능
  - 태그 이벤트 리스너는 사용자 또는 허브 링크 추가 시 발행되는 링크 생성 이벤트를 구독
  - 생성된 링크 개수가 조건을 만족하면(5개 이상 시, 5의 배수 개 마다, 하루에 한 번) 링크 개수에 비례하여 최대 5개의 태그를 생성
  - 생성형 AI로는 Google Gemini API를 사용
  ![생성형 ai를 이용한 태그 생성](https://github.com/hseong3243/shout-link/assets/48748265/1532ad81-da23-4146-8915-aebb64b74b0b)
- DIP, 이벤트 발행을 통한 패키지 순환 의존성 제거
  - 패키지 간 순환 참조가 발생하는 경우 인터페이스를 이용하여 의존성을 역전
  - Spring Event를 활용하여 관심사를 분리하고 의존성을 제거
  ![의존성 관리](https://github.com/hseong3243/shout-link/assets/48748265/b6865037-01e7-490f-a8fa-6817af51e3bb)


