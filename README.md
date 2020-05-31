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

이 앱은 [`wbpbp/preshoes-model`](https://github.com/WBPBP/preshoes-model) 프로젝트의 `model` 모듈을 사용합니다. 해당 프로젝트가 이 프로젝트와 같은 디렉토리에 존재햐여야 합니다. `model` 모듈은 아래와 같이 사용됩니다.

~~~java
// settings.gradle
include ':model'
project(':model').projectDir = new File('../preshoes-model/model')
~~~

## 업데이트 기록

### 2020.6.1 v0.1.0-beta01

- 기본 요구사항 구현 완료.
- 베타 테스트 시작.

## 라이센스

소스 코드에는 GPLv3 라이센스가 적용됩니다. 라이센스는 [이곳](https://github.com/wbpbp/preshoes-android/blob/master/LICENSE)에서 확인하실 수 있습니다.
