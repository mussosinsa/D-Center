# Flamingo 2 설치 및 운영 가이드 (Java 11 기준)

이 문서는 **Flamingo 2를 신규 설치하고 운영 환경에 안정적으로 배포/운영**하기 위한 실무 기준을 정리합니다.

---

## 1) 대상 독자 및 범위

- 대상: 인프라/플랫폼 운영자, 백엔드 개발자, DevOps 담당자
- 범위:
  - 소스 기반 빌드 및 배포
  - Tomcat 기반 실행
  - 필수 설정 파일 점검
  - 운영/보안/장애 대응 기준

---

## 2) 사전 요구사항

### 2.1 소프트웨어 버전

- JDK: **11 LTS**
- Maven: **3.8+**
- Tomcat: **8.5+** (최소 7 이상 가능)
- DB: MySQL 5.1+ (권장: 호환되는 최신 LTS)
- Hadoop/Hive 연동 시 대상 클러스터 접근 가능해야 함

확인 명령어:

```bash
java -version
mvn -version
```

### 2.2 네트워크/권한

- 애플리케이션 서버 ↔ DB/Hadoop/Hive/Object Storage 통신 가능
- 운영 계정에 배포 경로 쓰기 권한
- 방화벽 및 Reverse Proxy 정책 사전 반영

---

## 3) 설치 디렉터리 표준

운영 배포는 전용 계정을 사용합니다.

```bash
# 예시
sudo useradd -m -s /bin/bash flamingo
sudo mkdir -p /opt/flamingo/{app,logs,data,run,conf,backup}
sudo chown -R flamingo:flamingo /opt/flamingo
```

권장 경로:

- 애플리케이션: `/opt/flamingo/app`
- 로그: `/opt/flamingo/logs`
- 런타임: `/opt/flamingo/run`
- 외부화 설정: `/opt/flamingo/conf`
- 백업: `/opt/flamingo/backup`

---

## 4) 소스 설치 및 빌드

### 4.1 소스 준비

```bash
git clone <REPO_URL> flamingo
cd flamingo
```

### 4.2 Java 11 환경 변수

`/etc/profile.d/flamingo-java.sh` 또는 서비스 유닛 환경 파일:

```bash
export JAVA_HOME=/usr/lib/jvm/java-11-openjdk
export PATH=$JAVA_HOME/bin:$PATH
```

검증:

```bash
echo "$JAVA_HOME"
java -version
```

### 4.3 Maven 저장소 정책

외부 통신 제한 환경은 사내 Nexus/Artifactory 미러 사용을 권장합니다.

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

### 4.4 빌드

```bash
mvn -DskipTests package
```

산출물(예):

- `flamingo2-web/target/*.war`
- 모듈별 `target/*.jar`

---

## 5) 애플리케이션 설치(배포)

### 5.1 Tomcat 배포

1. `flamingo2-web/target/*.war`를 Tomcat `webapps/`에 배포
2. 컨텍스트 경로 확인(예: `/flamingo2-web`)
3. Tomcat 재기동

예시:

```bash
cp flamingo2-web/target/flamingo2-web.war /opt/tomcat/webapps/
/opt/tomcat/bin/shutdown.sh
/opt/tomcat/bin/startup.sh
```

### 5.2 설정 파일 반영

기본적으로 아래 파일들을 환경에 맞게 점검/수정해야 합니다.

- `flamingo2-web/src/main/webapp/WEB-INF/app.properties`
- `flamingo2-web/src/main/webapp/WEB-INF/config.properties`
- `flamingo2-web/src/main/webapp/WEB-INF/hadoop.properties`

운영 시에는 소스 내 파일 직접 수정 대신,
- 배포 파이프라인에서 환경별 템플릿 주입 또는
- Tomcat 외부 경로 마운트 방식
을 권장합니다.

---

## 6) 운영 설정 핵심

### 6.1 JVM/Tomcat 권장값

`setenv.sh` 예시:

```bash
export CATALINA_OPTS="-Xms2g -Xmx2g -XX:+UseG1GC -Dfile.encoding=UTF-8 -Duser.timezone=Asia/Seoul"
```

추가 권장:

- Access log 활성화
- `maxThreads`, `acceptCount`는 트래픽에 맞춰 조정
- 업로드 제한(`maxPostSize`, reverse proxy body size) 사전 정의

### 6.2 스토리지 운영 모드

`config.properties` 기준:

- `storage.mode=hadoop`: HDFS 전용
- `storage.mode=object`: Ceph/MinIO 전용
- `storage.mode=hybrid`: HDFS + Object 병행

예시:

```properties
storage.mode=hybrid
storage.hadoop.enabled=true
storage.object.enabled=true

storage.object.provider=minio
storage.object.endpoint=http://minio.example.com:9000
storage.object.bucket=flamingo
storage.object.access.key=***
storage.object.secret.key=***
storage.object.path.style.access=true
storage.object.ssl.verify=true
storage.object.timeout=30000
```

권장사항:

- TLS 적용 및 접근키 주기적 로테이션
- 데이터 분류 정책(원본/임시/메타) 문서화
- 장애 전환(fallback) 시나리오 사전 점검

---

## 7) 기동/중지/상태 확인

### 7.1 Tomcat 기준

```bash
/opt/tomcat/bin/startup.sh
/opt/tomcat/bin/shutdown.sh
ps -ef | grep tomcat
```

### 7.2 헬스 체크

- 로그인 페이지 또는 인덱스 페이지 HTTP 200 확인
- 주요 메뉴(HDFS Browser, Workflow, Monitoring) 접근 확인
- DB/Hadoop/Hive 연동 API 샘플 호출 확인

---

## 8) 로그 및 모니터링

- 애플리케이션 로그/접근 로그/배치 로그 분리
- 일 단위 + 용량 기준 롤링 정책 병행
- 최소 모니터링 지표:
  - JVM Heap/GC
  - CPU/Load
  - 파일시스템 사용량
  - 주요 API 오류율(4xx/5xx)

---

## 9) 보안 운영 가이드

- 실행 계정 최소 권한 원칙
- 비밀정보(계정/키)는 평문 커밋 금지
- 설정 파일 권한 최소화(예: 640)
- 외부 노출 포트 최소화, Reverse Proxy 경유
- HTTPS/TLS 종료 지점 표준화

---

## 10) 백업/복구 및 배포 전략

- 백업 대상:
  - DB
  - 운영 설정 파일
  - 사용자 업로드/워크플로우 메타데이터
- 배포 전략:
  - Blue/Green 또는 Rolling 배포 권장
  - 릴리즈 버전 및 산출물 해시 기록
  - 롤백 절차 문서화(직전 WAR 복원 + 설정 복원)

---

## 11) 장애 대응 체크리스트

1. `java -version`으로 Java 11 확인
2. `JAVA_HOME`, `CATALINA_OPTS` 확인
3. DB/Hadoop/Hive/Object Storage 연결 확인
4. 최근 릴리즈의 설정 변경(diff) 확인
5. 로그에서 최초 에러 시점/스택트레이스 우선 확인
6. 즉시 롤백 필요 여부 판단(영향 범위 기준)

---

## 12) 설치 후 운영 전 최종 점검

- [ ] Java 11 고정
- [ ] Maven/Tomcat 버전 정책 충족
- [ ] 환경별 설정값(app/config/hadoop.properties) 검증
- [ ] 로그 롤링/보존 정책 적용
- [ ] 백업/복구 리허설 완료
- [ ] 스토리지 모드(hadoop/object/hybrid) 검증
- [ ] 재기동 및 롤백 절차 문서화

---

## 부록) 빠른 시작(개발/검증 환경)

```bash
# 1) 빌드
mvn -DskipTests package

# 2) 배포
cp flamingo2-web/target/flamingo2-web.war /opt/tomcat/webapps/

# 3) 기동
/opt/tomcat/bin/startup.sh

# 4) 확인
curl -I http://localhost:8080/flamingo2-web/
```

운영 반영 전에는 반드시 스테이징에서 연동 항목(DB/Hadoop/Hive/Object Storage)까지 검증하세요.
