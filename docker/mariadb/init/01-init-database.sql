-- TripCut Database 초기화 스크립트

-- 데이터베이스가 없으면 생성 (docker-compose에서 이미 생성하지만 안전장치)
CREATE DATABASE IF NOT EXISTS tripcut 
    CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

-- 사용자가 없으면 생성하고 권한 부여
CREATE USER IF NOT EXISTS 'tripcut'@'%' IDENTIFIED BY 'tripcut!123';
CREATE USER IF NOT EXISTS 'tripcut'@'localhost' IDENTIFIED BY 'tripcut!123';

-- tripcut 사용자에게 권한 부여 (% 및 localhost 모두)
GRANT ALL PRIVILEGES ON tripcut.* TO 'tripcut'@'%';
GRANT ALL PRIVILEGES ON tripcut.* TO 'tripcut'@'localhost';

-- 권한 새로고침
FLUSH PRIVILEGES;

-- tripcut 데이터베이스 사용
USE tripcut;

-- 시간대 설정
SET time_zone = '+09:00';

-- 초기 데이터 삽입을 위한 테이블들 (Hibernate가 자동으로 생성하므로 생략)
-- 필요시 여기에 초기 데이터 INSERT 문 추가

-- 로그 메시지
SELECT 'TripCut Database initialization completed!' AS Message;