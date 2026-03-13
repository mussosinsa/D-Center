# Flamingo 2 운영 설정 가이드 (Java 11 기준)

이 문서는 **실무 서비스 운영 환경**에서 Flamingo 2를 안정적으로 배포/운영하기 위한 기준 설정을 정리합니다.

## 1) 권장 런타임/빌드 기준

- JDK: **11 LTS** (개발/운영 동일 버전 권장)
- Maven: 3.8+
- Tomcat: 8.5+ (최소 7 이상 가능하나 운영은 8.5+ 권장)
- Linux: RHEL/CentOS/Ubuntu 계열

```bash
java -version
mvn -version
```

---

## 2) 서버 계정/디렉터리 표준

운영 배포는 전용 계정을 사용합니다.

```bash
# 예시
useradd -m -s /bin/bash flamingo
mkdir -p /opt/flamingo/{app,logs,data,run,conf}
chown -R flamingo:flamingo /opt/flamingo
```

권장 경로:

- 애플리케이션: `/opt/flamingo/app`
- 로그: `/opt/flamingo/logs`
- 임시/런타임: `/opt/flamingo/run`
- 설정: `/opt/flamingo/conf`

---

## 3) Java 11 환경 변수

`/etc/profile.d/flamingo-java.sh` 또는 서비스 유닛 환경 파일에 다음을 설정합니다.

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

검증:

```bash
echo "$JAVA_HOME"
java -version
```

---

## 4) Maven/의존성 운영 정책

프로젝트 POM은 Maven Central 기준으로 동작합니다. 운영망에서 외부 통신이 제한되면 사내 Nexus/Artifactory 미러를 사용하세요.

`~/.m2/settings.xml` 예시:

```xml
<settings>
  <mirrors>
    <mirror>
      <id>company-mirror</id>
      <name>Company Maven Mirror</name>
      <url>https://nexus.example.com/repository/maven-public/</url>
      <mirrorOf>*</mirrorOf>
    </mirror>
  </mirrors>
</settings>
```

> 운영 CI/CD에서는 `-U`(강제 업데이트) 사용을 최소화하고, 재현 가능한 빌드를 위해 의존성 캐시를 관리하세요.

---

## 5) 빌드/배포 기본 절차

### 5.1 빌드

```bash
mvn -DskipTests package
```

산출물(예):

- `flamingo2-web/target/*.war`
- 모듈별 `target/*.jar`

### 5.2 배포

- WAR: Tomcat `webapps/` 배포
- JAR/에이전트: systemd 서비스 또는 스케줄러로 운영

---

## 6) Tomcat 운영 권장값

`setenv.sh` 예시:

```bash
export CATALINA_OPTS="-Xms2g -Xmx2g -XX:+UseG1GC -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Seoul"
```

추가 권장:

- Access log 활성화
- `maxThreads`, `acceptCount`는 트래픽에 맞춰 조정
- 세션/업로드 크기 제한 명확화

---

## 7) 로그/모니터링 운영

- 애플리케이션 로그, 접근 로그, 배치 로그를 분리 보관
- 로그 롤링(일/용량 기준) 및 보존 기간 정책 설정
- 필수 모니터링 지표:
  - JVM Heap/GC
  - CPU/Load
  - 파일시스템 사용량
  - 주요 API 오류율

---

## 8) 보안/권한 운영

- 실행 계정 최소 권한 원칙 적용
- 설정 파일(`app.properties`, DB 계정 정보 등) 권한 640 이하 권장
- 외부 노출 포트 최소화(Reverse Proxy 경유)
- HTTPS/TLS 종료 지점 표준화

---

## 9) 장애 대응 체크리스트

1. `java -version`으로 Java 11 여부 확인
2. `JAVA_HOME` 및 서비스 유닛 환경 변수 확인
3. Maven 저장소 접근 여부/미러 설정 확인
4. DB/Hadoop/Hive 연결 설정 점검
5. 최근 배포 변경분(릴리즈 노트, 설정 diff) 확인

---

## 10) 운영 전 최종 점검

- [ ] Java 11 고정
- [ ] 운영/개발 환경 변수 일치
- [ ] 빌드 산출물 해시/버전 추적 가능
- [ ] 로그 롤링/보존 정책 적용
- [ ] 백업/복구 절차 리허설 완료
- [ ] 서비스 재기동(systemd/Tomcat) 절차 문서화

