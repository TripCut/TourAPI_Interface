# TripCut API Interface

Spring Boot 기반의 여행 추천 API 인터페이스 프로젝트입니다.

## 기술 스택

- Spring Boot 3.2.3
- Spring Security
- JWT Authentication
- H2 Database
- Prometheus
- Grafana
- Loki
- Node Exporter

## 모니터링 스택

프로젝트는 다음과 같은 모니터링 도구들을 사용합니다:

- **Prometheus**: 메트릭 수집
- **Grafana**: 메트릭 및 로그 시각화
- **Loki**: 로그 수집
- **Node Exporter**: 시스템 메트릭 수집

## 실행 방법

### 1. 로그 디렉토리 생성

```bash
sudo mkdir -p /var/log/spring-boot
sudo chmod 777 /var/log/spring-boot
```

### 2. 프로젝트 빌드

```bash
./gradlew clean build
```

### 3. 모니터링 스택 실행

```bash
docker-compose up -d
```

### 4. 접속 정보

- **Spring Boot Application**: http://localhost:8080
- **Grafana**: http://localhost:3000 (admin/admin)
- **Prometheus**: http://localhost:9090
- **Loki**: http://localhost:3100
- **Node Exporter**: http://localhost:9100/metrics

## Grafana 설정

### 1. 데이터 소스 추가

1. Grafana에 로그인 (http://localhost:3000)
2. Configuration > Data Sources > Add data source
3. 다음 데이터 소스들을 추가:
   - **Prometheus**: http://prometheus:9090
   - **Loki**: http://loki:3100

### 2. 대시보드 추가

#### Node Exporter 대시보드
1. 왼쪽 메뉴에서 "+" 버튼 클릭
2. "Import" 선택
3. 다음 ID 중 하나 입력:
   - Node Exporter Full: 1860
   - Node Exporter for Prometheus Dashboard: 11074
4. Prometheus를 데이터 소스로 선택
5. "Import" 클릭

## 로그 모니터링

### 로그 쿼리 예시

Grafana의 Explore 섹션에서 다음 LogQL 쿼리를 사용할 수 있습니다:

- `{job="spring-boot"}` - Spring Boot 애플리케이션의 모든 로그
- `{job="spring-boot"} |= "ERROR"` - 에러 로그만 필터링
- `{job="spring-boot"} |~ ".*Exception.*"` - 예외가 포함된 로그

## 메트릭 모니터링

다음과 같은 메트릭을 모니터링할 수 있습니다:

### 시스템 메트릭 (Node Exporter)
- CPU 사용량
- 메모리 사용량
- 디스크 I/O
- 네트워크 트래픽
- 시스템 부하
- 파일시스템 사용량
- 프로세스 상태

### 애플리케이션 메트릭 (Spring Boot Actuator)
- JVM 메트릭 (메모리, GC, 스레드 등)
- HTTP 요청 메트릭 (응답 시간, 요청 수 등)
- 데이터베이스 메트릭 (연결 풀, 쿼리 시간 등)
- 커스텀 메트릭 (TimedAspect를 사용한 메서드 실행 시간 등)

## 문제 해결

### 로그가 보이지 않는 경우
1. 로그 디렉토리 권한 확인
2. Promtail 설정 확인
3. Loki 연결 상태 확인

### 메트릭이 보이지 않는 경우
1. Spring Boot Actuator 엔드포인트 확인 (/actuator/prometheus)
2. Prometheus 타겟 상태 확인
3. Node Exporter 상태 확인

## 컨테이너 중지

모든 컨테이너를 중지하려면:

```bash
docker-compose down
```

## 주의사항

- 프로덕션 환경에서는 보안 설정을 적절히 구성해야 합니다.
- 로그 보관 기간과 저장소 크기를 모니터링하고 관리해야 합니다.
- 메트릭 수집 간격과 보관 기간을 환경에 맞게 조정해야 합니다. 