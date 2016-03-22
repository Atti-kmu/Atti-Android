# Atti-Android [![Build Status](https://travis-ci.org/Atti-kmu/Android.svg?branch=develop)](https://travis-ci.org/Atti-kmu/Android)

# 환경설정
```
[File] - [Project Structure] - [app] - [Dependenies] - [+] - [File Dependency] 에서 sktplayrtc-client.2.2.4.jar 추가
[File] - [Project Structure] - [app] - [Dependenies] - [+] - [File Dependency] 에서 libjingle_peerconnection.r9720.3.jar 추가
```

# TCP/IP 통신 Android Build
```
1. 핸드폰과 PC연결
2. adb tcpip <port>
3. adb connect <ip>:<port>
4. 핸드폰을 분리한 뒤 3번을 다시 입력
5. 핸드폰 연결완료
```
