# 🍚 HonBob Backend

> 혼자 밥 먹는 사람들을 위한 레시피 공유 플랫폼 백엔드 API

## 📖 프로젝트 소개

HonBob은 1인 가구를 위한 레시피 공유 및 관리 플랫폼입니다. 사용자들이 간편한 요리 레시피를 공유하고, 즐겨찾기하며, 리뷰를 남길 수 있는 서비스를 제공합니다.

## 🛠 기술 스택

### Backend
- **Java 21** - 최신 LTS 버전
- **Spring Boot 3.2.5** - 웹 애플리케이션 프레임워크
- **Spring Security** - 인증 및 보안
- **Spring Data JPA** - 데이터 접근 계층
- **Spring WebFlux** - 비동기 웹 클라이언트

### Database
- **MySQL** - 관계형 데이터베이스

### Authentication
- **JWT** - JSON Web Token 인증
- **Kakao Login API** - 소셜 로그인

### Build Tool
- **Gradle** - 의존성 관리 및 빌드

### Other Libraries
- **Lombok** - 코드 간소화
- **Validation** - 입력 검증

## 🏗 프로젝트 구조

```
src/main/java/honbob/honbob/
├── HonbobApplication.java          # 메인 애플리케이션 클래스
├── controller/                     # REST API 컨트롤러
│   ├── KakaoLoginController.java   # 카카오 로그인 API
│   └── GlobalLoginController.java  # 일반 로그인 API
├── domain/                         # 엔티티 클래스
│   ├── Member.java                 # 회원 엔티티
│   ├── Recipe.java                 # 레시피 엔티티
│   ├── RecipeIngredient.java       # 레시피 재료 엔티티
│   └── Favorite.java               # 즐겨찾기 엔티티
├── dto/                           # 데이터 전송 객체
│   ├── kakao/                     # 카카오 API 관련 DTO
│   ├── UserLoginResponse.java     # 로그인 응답 DTO
│   └── UserProfileRequest.java    # 프로필 요청 DTO
├── repository/                    # 데이터 접근 계층
│   └── MemberRepository.java      # 회원 리포지토리
├── service/                       # 비즈니스 로직
│   └── kakao/                     # 카카오 로그인 서비스
│       ├── KakaoTokenService.java
│       └── LoginService.java
└── global/                        # 글로벌 설정
    ├── exception/                 # 예외 처리
    │   ├── ExceptionType.java
    │   ├── BusinessException.java
    │   └── GlobalExceptionHandler.java
    └── response/                  # 공통 응답 형식
        ├── ResponseBody.java
        └── ResponseUtil.java
```

## 🚀 시작하기

### 사전 요구사항
- Java 21 이상
- MySQL 8.0 이상
- Gradle 7.0 이상

### 설치 및 실행

1. **레포지토리 클론**
   ```bash
   git clone https://github.com/honbob-log/honbob-backend.git
   cd honbob-backend
   ```

2. **데이터베이스 설정**
   ```bash
   # MySQL 데이터베이스 생성
   mysql -u root -p
   CREATE DATABASE honbob;
   ```

3. **환경 설정**
   `src/main/resources/application.properties` 파일을 설정하세요:
   ```properties
   spring.application.name=honbob
   
   # Database Configuration
   spring.datasource.url=jdbc:mysql://localhost:3306/honbob
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   
   # JPA Configuration
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   
   # Kakao OAuth Configuration
   kakao.client-id=your_kakao_client_id
   kakao.redirect-uri=your_redirect_uri
   
   # JWT Configuration
   jwt.secret=your_jwt_secret_key
   jwt.expiration=86400000
   ```

4. **애플리케이션 실행**
   ```bash
   ./gradlew bootRun
   ```

5. **API 확인**
   - 서버가 `http://localhost:8080`에서 실행됩니다
   - API 문서는 추후 Swagger를 통해 제공 예정

## 🔐 인증

### 카카오 로그인
```http
GET /api/auth/kakao/login
```
카카오 로그인 URL을 반환합니다.

```http
GET /auth/login/kakao?code={authorization_code}
```
카카오 인가 코드를 통해 로그인 처리합니다.

## 📊 주요 기능

### 👤 회원 관리
- 카카오 소셜 로그인
- JWT 기반 인증
- 사용자 프로필 관리

### 🍳 레시피 관리
- 레시피 등록/조회/수정/삭제
- 재료 정보 관리
- 조리 시간 및 난이도 설정
- 조회수, 좋아요, 즐겨찾기 기능

### ⭐ 소셜 기능
- 레시피 즐겨찾기
- 리뷰 작성 및 평점
- 레시피 좋아요

## 🔧 개발 도구

### 코드 품질
- **Lombok**: 보일러플레이트 코드 감소
- **Validation**: 입력 데이터 검증
- **Exception Handling**: 통합 예외 처리

### 테스트
```bash
./gradlew test
```

## 📝 API 문서

API 문서는 추후 Swagger/OpenAPI를 통해 제공될 예정입니다.

## 🤝 기여하기

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 라이선스

이 프로젝트는 라이선스가 명시되지 않았습니다. 

## 📞 문의

프로젝트 관련 문의사항이 있으시면 이슈를 등록해주세요.

---

**Made with ❤️ for people who eat alone**