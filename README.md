# preshoes-android

[![GitHub last commit](https://img.shields.io/github/last-commit/wbpbp/preshoes-android)](https://github.com/wbpbp/preshoes-android/commits)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/wbpbp/preshoes-android)](https://github.com/wbpbp/preshoes-android/releases/latest)
[![GitHub stars](https://img.shields.io/github/stars/wbpbp/preshoes-android?style=shield)](https://github.com/wbpbp/preshoes-android/stargazers)
[![GitHub issues](https://img.shields.io/github/issues/wbpbp/preshoes-android)](https://github.com/wbpbp/preshoes-android/issues)
![GitHub closed issues](https://img.shields.io/github/issues-closed/wbpbp/preshoes-android)
[![GitHub license](https://img.shields.io/github/license/wbpbp/preshoes-android)](https://github.com/wbpbp/preshoes-android/blob/master/LICENSE)

Preshoes - 안드로이드 클라이언트

## 개요

<img src="/docs/demo.gif" width="250px">

신발 센서 모듈로부터 전달받은 데이터를 분석, 시각화하여 사용자에게 보여줍니다.

## 의존성

이 앱은 [`wbpbp/preshoes-model`](https://github.com/WBPBP/preshoes-model) 프로젝트의 `model` 모듈을 사용합니다. 해당 프로젝트가 이 프로젝트와 같은 디렉토리에 존재하여야 합니다. `model` 모듈은 아래와 같이 사용됩니다.

~~~java
// settings.gradle
include ':model'
project(':model').projectDir = new File('../preshoes-model/model')
~~~

## 테스트 빌드

실제 센서 모듈과 연결할 수 없는 상황을 위해 `bluetooth` dimension에 대해 `fakeDevice`와 `realDevice` flavor를 지원합니다.

서버와 연결할 수 없는 상황을 위해 `server` dimension에 대해 `mockServer`와 `deployServer` flavor를 지원합니다.

예를 들어, 센서 모듈이 없는 상태에서 서버와의 연결을 테스트하고 싶다면 `fakeDeviceDeployServerDebug` 옵션으로 빌드를 진행할 수 있습니다.

## 업데이트 기록

### 2020.6.8 v0.1.0

- Realm DB 스레드 문제 해결.
- 베타테스트 종료.

### 2020.6.6 v0.1.0-beta05

- 저수준 데이터 분석기 출력 보완.

### 2020.6.6 v0.1.0-beta04

- 배터리 정보를 포함하는 새로운 프로토콜 적용.

### 2020.6.5 v0.1.0-beta03

- 센서 재배치.

### 2020.6.1 v0.1.0-beta02

- 서버 테스트 완료.
- 검사 시간 옵션 추가.
- 보고서 200 이외 응답 도착시 repository에서 throw.

### 2020.6.1 v0.1.0-beta01

- 기본 요구사항 구현 완료.
- 베타 테스트 시작.

## 라이센스

소스 코드에는 GPLv3 라이센스가 적용됩니다. 라이센스는 [이곳](https://github.com/wbpbp/preshoes-android/blob/master/LICENSE)에서 확인하실 수 있습니다.
