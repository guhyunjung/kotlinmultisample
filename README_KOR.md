이것은 Android, Web, Desktop (JVM)을 대상으로 하는 Kotlin Multiplatform 프로젝트입니다.

* [/composeApp](./composeApp/src)는 Compose Multiplatform 애플리케이션 전체에서 공유될 코드를 위한 것입니다.
  여러 개의 하위 폴더를 포함하고 있습니다:
    - [commonMain](./composeApp/src/commonMain/kotlin)은 모든 플랫폼에 공통인 코드를 위한 것입니다.
    - 다른 폴더들은 폴더 이름에 표시된 플랫폼에만 컴파일될 Kotlin 코드를 위한 것입니다.
      예를 들어, Kotlin 앱의 iOS 부분에 Apple의 CoreCrypto를 사용하고 싶다면,
      [iosMain](./composeApp/src/iosMain/kotlin) 폴더가 그러한 호출을 하기에 적절한 장소입니다.
      마찬가지로 Desktop (JVM) 특정 부분을 편집하려면, [jvmMain](./composeApp/src/jvmMain/kotlin)
      폴더가 적절한 위치입니다.

* [/shared](./shared/src)는 프로젝트의 모든 대상 간에 공유될 코드를 위한 것입니다.
  가장 중요한 하위 폴더는 [commonMain](./shared/src/commonMain/kotlin)입니다. 원하신다면,
  여기의 플랫폼 특정 폴더에도 코드를 추가할 수 있습니다.

* [/webApp](./webApp)은 웹 React 애플리케이션을 포함하고 있습니다. [shared](./shared) 모듈에서
  생성된 Kotlin/JS 라이브러리를 사용합니다.

### Android 애플리케이션 빌드 및 실행

Android 앱의 개발 버전을 빌드하고 실행하려면, IDE의 도구 모음에서 실행 구성을 사용하거나 터미널에서 직접 빌드하세요:

- macOS/Linux에서
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- Windows에서
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Desktop (JVM) 애플리케이션 빌드 및 실행

Desktop 앱의 개발 버전을 빌드하고 실행하려면, IDE의 도구 모음에서 실행 구성을 사용하거나 터미널에서 직접 실행하세요:

- macOS/Linux에서
  ```shell
  ./gradlew :composeApp:run
  ```
- Windows에서
  ```shell
  .\gradlew.bat :composeApp:run
  ```

### 웹 애플리케이션 빌드 및 실행

웹 앱의 개발 버전을 빌드하고 실행하려면, IDE의 도구 모음에서 실행 구성을 사용하거나 터미널에서 직접 실행하세요:

1. [Node.js](https://nodejs.org/en/download) 설치 (`npm` 포함)
2. Kotlin/JS 공유 코드 빌드:
    - macOS/Linux에서
      ```shell
      ./gradlew :shared:jsBrowserDevelopmentLibraryDistribution
      ```
    - Windows에서
      ```shell
      .\gradlew.bat :shared:jsBrowserDevelopmentLibraryDistribution
      ```
3. 웹 애플리케이션 빌드 및 실행
   ```shell
   npm install
   npm run start
   ```

---

[Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)에 대해 자세히 알아보세요…

