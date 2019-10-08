# ff-android

압력감지 시스템 - 안드로이드 클라이언트

## 개요

아두이노 센서 모듈로부터 전달받은 데이터를 분석, 시각화하여 사용자에게 보여줍니다.

## 환경

- 통합 개발 환경: Android Studio 버전 3.5 이상
- 주력 언어: Kotlin
- 타겟 안드로이드 버전: API 26 (Android 8.0)

## 아키텍쳐

 [클린 아키텍쳐](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)를 준수하는 것이 목표입니다. 타협이 있을 수 있으나 다음 원칙은 지켜야 합니다:
 
 - 중복 코드 금지
 - 의존성 규칙 준수 (의존성은 domain 방향으로만)
 - domain과 data, presentation의 구분
 
## 정책

### 패키지 이름

 프로젝트 내의 모든 모듈은 org.wbpbp.footfoot 아래에 위치해야 합니다.
 
### 의존 라이브러리 선언

 모든 의존성은 각 모듈의 gradle 스크립트 안에 선언합니다.
 
 
 
 
 ## 기타
 
  작성중 2019.10.9 00:05





