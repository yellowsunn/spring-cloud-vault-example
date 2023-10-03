# spring-cloud-vault-example
Vault를 사용한 비밀 정보 관리

## 0. 실제 적용한 Vault Config 예시
[src/main/resources/application.yml](https://github.com/yellowsunn/spring-cloud-vault-example/blob/main/src/main/resources/application.yml)

## 1. Vault 서버 설정 하기
*Vault 서버가 사전에 있다는 가정 아래서 진행*

#### 데이터 추가
```bash
# Key-Value 저장소에 데이터 추가
vault kv put kv/application/mysql username=foo password=bar
```

#### AppRole 역할 추가
> 어플리케이션에서 제한된 정책을 가진 인증 수단으로 AppRole을 사용
* AppRole 생성
  ```bash
  vault write auth/approle/role/my-role \
	  role_id=4950d923-f95e-46fd-9139-6c98e654326b \
	  secret_id=70048d62-11fd-4fa0-ac68-0b68a0c59b54
  ```
* ACL Policy 추가
 ![스크린샷 2023-10-03 오후 4 25 46](https://github.com/yellowsunn/spring-cloud-vault-example/assets/43487002/d2e85876-8ae5-4d8b-83de-355fc3346940)

* AppRole에 Policy 연결
  ```
  vault write auth/approle/role/my-role token_policies="my-policy"
  ```

## 2. Spring Boot 프로젝트에 Vault 서버 연동하기
```kotlin
// build.gradle.kts

dependencies {
  implementation("org.springframework.cloud:spring-cloud-starter-vault-config")
}

```

```yml
# application.yml
spring:
  config:
    import:
      - vault://kv/dev/application/mysql?prefix=mysql.
  cloud:
    vault:
      host: localhost
      port: 8200
      scheme: http
      authentication: approle
      app-role:
        role-id: 4950d923-f95e-46fd-9139-6c98e654326b
        secret-id: 70048d62-11fd-4fa0-ac68-0b68a0c59b54
        role: my-role
  datasource:
    hikari:
      driver-class-name: com.mysql.cj.jdbc.Driver
      username: ${mysql.username}
      password: ${mysql.password}
```
* `spring.config.import`에 vault://... 와 같이 Vault 시크릿 정보를 바인딩할 수 있으며 prefix를 앞에 붙일 수도 있다.
  * `vault://kv/dev/application/mysql` -> ${username}, ${password} 로 조회 가능
  * `vault://kv/dev/application/mysql?prefix=hello.` -> ${hello.username}, ${hello.password} 로 조회 가능
