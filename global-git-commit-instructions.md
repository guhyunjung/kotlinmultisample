# Global Git Commit Instructions

## 기본 원칙 (Basic Principles)
1.  **커밋 메시지는 영어로 작성합니다.** (Commit messages should be in English.)
2.  **명령형 현재 시제**를 사용합니다. (Use the imperative mood in the subject line.)
    *   예: "Fix bug" (O), "Fixed bug" (X), "Fixes bug" (X)
3.  **제목과 본문은 한 줄 띄워 분리**합니다. (Separate subject from body with a blank line.)
4.  **관련 이슈 번호**를 포함합니다. (Reference related issue numbers.)

## 커밋 메시지 구조 (Structure)
```
<type>(<scope>): <subject>

<body>

<footer>
```

## Type 종류 (Types)
*   **feat**: 새로운 기능 추가 (New feature)
*   **fix**: 버그 수정 (Bug fix)
*   **docs**: 문서 수정 (Documentation only changes)
*   **style**: 코드 포맷팅, 세미콜론 누락 등 코드 변경이 없는 경우 (Changes that do not affect the meaning of the code)
*   **refactor**: 리팩토링 (A code change that neither fixes a bug nor adds a feature)
*   **perf**: 성능 개선 (A code change that improves performance)
*   **test**: 테스트 코드 추가 (Adding missing tests or correcting existing tests)
*   **chore**: 빌드 업무 수정, 패키지 매니저 수정 등 (Changes to the build process or auxiliary tools and libraries such as documentation generation)

## 예시 (Examples)

### 기능 추가 (Feature)
```
feat(auth): add google login support

Implemented Google OAuth2 login flow.
Added necessary dependencies and configuration.

Closes #123
```

### 버그 수정 (Fix)
```
fix(ui): resolve layout issue on small screens

Fixed overlapping elements in the header component
when viewing on mobile devices.
```

### 리팩토링 (Refactor)
```
refactor(server): optimize database query for user search

Replaced N+1 query with a single join query for better performance.
```

