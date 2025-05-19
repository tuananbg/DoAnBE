-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: localhost    Database: company_management
-- ------------------------------------------------------
-- Server version	8.0.33
create database company_management;
use company_management;

-- UNLOCK TABLES;
/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;


CREATE TABLE `JOB_GROUP`
(
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `CODE`          VARCHAR(100) NOT NULL,
    `NAME`          VARCHAR(255) NOT NULL,
    `DESCRIPTION`   VARCHAR(500) NULL,
    `STATUS`        INT                   DEFAULT NULL,
    `CREATED_BY`    VARCHAR(255) NOT NULL,
    `CREATED_DATE`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_DATE` TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `MODIFIED_BY`   VARCHAR(255)          DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

INSERT INTO `JOB_GROUP` (`CODE`, `NAME`, `DESCRIPTION`, `STATUS`, `CREATED_BY`, `CREATED_DATE`)
VALUES ('01', 'Kinh doanh', 'Nhóm công việc kinh doanh', 1, 'admin', NOW()),
       ('02', 'Kỹ thuật', 'Nhóm công việc kỹ thuật', 1, 'admin', NOW()),
       ('03', 'Nhân sự', 'Nhóm công việc nhân sự', 1, 'admin', NOW());



/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `POSITION_CATEGORY`
(
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `CODE`          VARCHAR(50)  NOT NULL,
    `NAME`          VARCHAR(255) NOT NULL,
    `DESCRIPTION`   VARCHAR(255) NULL,
    `STATUS`        INT                   DEFAULT NULL,
    `CREATED_BY`    VARCHAR(255) NOT NULL,
    `CREATED_DATE`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_DATE` TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `MODIFIED_BY`   VARCHAR(255)          DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
INSERT INTO `POSITION_CATEGORY` (`CODE`, `NAME`, `DESCRIPTION`, `STATUS`, `CREATED_BY`, `CREATED_DATE`)
VALUES ('01', 'Trưởng phòng', 'Chức danh Trưởng phòng ban', 1, 'admin', NOW()),
       ('02', 'Phó phòng', 'Chức danh Phó phòng ban', 1, 'admin', NOW()),
       ('03', 'Chuyên viên', 'Chức danh chuyên môn nghiệp vụ', 1, 'admin', NOW()),
       ('04', 'Nhân viên', 'Chức danh nhân viên thông thường', 1, 'admin', NOW());


/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;


--
-- Table structure for table `department`
--

DROP TABLE IF EXISTS `DEPARTMENT`;

CREATE TABLE `DEPARTMENT`
(
    `ID`              BIGINT       NOT NULL AUTO_INCREMENT,
    `DEPARTMENT_CODE` VARCHAR(255)          DEFAULT NULL,
    `DEPARTMENT_NAME` VARCHAR(255)          DEFAULT NULL,
    `STATUS`          INT                   DEFAULT NULL,
    `CREATED_BY`      VARCHAR(255) NOT NULL,
    `CREATED_DATE`    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_DATE`   TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `MODIFIED_BY`     VARCHAR(255)          DEFAULT NULL,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 56
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng phòng ban';

DROP TABLE IF EXISTS `POSITION`;
CREATE TABLE `POSITION`
(
    `ID`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `POSITION_NAME`        VARCHAR(255)          DEFAULT NULL,
    `POSITION_CATEGORY_ID` BIGINT       NULL,
    `JOB_GROUP_ID`         BIGINT       NULL,
    `DEPARTMENT_ID`        BIGINT                DEFAULT NULL,
    `POSITION_CODE`        VARCHAR(255)          DEFAULT NULL,
    `POSITION_DESCRIPTION` VARCHAR(255)          DEFAULT NULL,
    `CREATED_BY`           VARCHAR(255) NOT NULL,
    `CREATED_DATE`         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`          VARCHAR(255) NULL,
    `MODIFIED_DATE`        TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`               INT                   DEFAULT '1',
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_EMPLOYEE_POSITION_DEPARTMENT` FOREIGN KEY (`DEPARTMENT_ID`) REFERENCES `DEPARTMENT` (`ID`),
    CONSTRAINT `FK_POSITION_CATEGORY` FOREIGN KEY (`POSITION_CATEGORY_ID`) REFERENCES `POSITION_CATEGORY` (`ID`),
    CONSTRAINT `FK_POSITION_JOB_GROUP` FOREIGN KEY (`JOB_GROUP_ID`) REFERENCES `JOB_GROUP` (`ID`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 8
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

--
-- Table structure for table `EMPLOYEE_INFO`
--

DROP TABLE IF EXISTS `EMPLOYEE_INFO`;

CREATE TABLE `EMPLOYEE_INFO`
(
    `ID`                BIGINT       NOT NULL AUTO_INCREMENT COMMENT 'ID thông tin nhân viên',
    `DATE_OF_BIRTH`     DATE                  DEFAULT NULL COMMENT 'Ngày sinh',
    `GENDER`            TINYINT               DEFAULT NULL COMMENT 'Giới tính',
    `PLACE_OF_BIRTH`    VARCHAR(255)          DEFAULT NULL COMMENT 'Nơi sinh',
    `TAX_CODE`          VARCHAR(255)          DEFAULT NULL COMMENT 'Mã số thuế',
    `INSURANCE_NUMBER`  VARCHAR(255)          DEFAULT NULL COMMENT 'Số bảo hiểm',
    `ACCOUNT_NUMBER`    VARCHAR(255)          DEFAULT NULL COMMENT 'Số tài khoản ngân hàng',
    `PERMANENT_ADDRESS` VARCHAR(100)          DEFAULT NULL COMMENT 'Địa chỉ thường trú',
    `IDENTITY_NUMBER`   VARCHAR(255)          DEFAULT NULL COMMENT 'Số CMND/CCCD',
    `MOBILE`            VARCHAR(255)          DEFAULT NULL COMMENT 'Số điện thoại di động',
    `EMAIL`             VARCHAR(255)          DEFAULT NULL COMMENT 'Email',
    `CREATED_BY`        VARCHAR(255) NOT NULL,
    `CREATED_DATE`      TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`       VARCHAR(255) NULL,
    `MODIFIED_DATE`     TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin chi tiết nhân viên';


--
-- Table structure for table `EMPLOYEE`
--

DROP TABLE IF EXISTS `EMPLOYEE`;

CREATE TABLE `EMPLOYEE`
(
    `ID`               BIGINT       NOT NULL AUTO_INCREMENT,
    `CODE`             VARCHAR(255)          DEFAULT NULL,
    `FULL_NAME`        VARCHAR(255)          DEFAULT NULL,
    `AVATAR`           varchar(5000)         DEFAULT NULL,
    `STATUS`           BIGINT                DEFAULT NULL,
    `EMPLOYEE_INFO_ID` BIGINT                DEFAULT NULL,
    `POSITION_ID`      BIGINT                DEFAULT NULL,
    `DEPARTMENT_CODE`  VARCHAR(255)          DEFAULT NULL,
    `CREATED_BY`       VARCHAR(255) NOT NULL,
    `CREATED_DATE`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`      VARCHAR(255) NULL,
    `MODIFIED_DATE`    TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,

    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_EMPLOYEE_POSITION` FOREIGN KEY (`POSITION_ID`) REFERENCES `POSITION` (`ID`),
    CONSTRAINT `FK_EMPLOYEE_EMPLOYEE_INFO` FOREIGN KEY (`EMPLOYEE_INFO_ID`) REFERENCES `EMPLOYEE_INFO` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin nhân viên';


/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

DROP TABLE IF EXISTS `ATTENDANCE`;

CREATE TABLE `ATTENDANCE`
(
    `ID`             BIGINT       NOT NULL AUTO_INCREMENT,
    `EMPLOYEE_ID`    BIGINT                DEFAULT NULL,
    `WORKING_DAY`    DATETIME(6)  NOT NULL,
    `CHECK_IN_TIME`  DATETIME(6)           DEFAULT NULL,
    `CHECK_OUT_TIME` DATETIME(6)           DEFAULT NULL,
    `WORKING_TIME`   DOUBLE                DEFAULT NULL,
    `WORKING_POINT`  DOUBLE                DEFAULT NULL,
    `TOTAL_PENALTY`  BIGINT                DEFAULT NULL,
    `CREATED_BY`     VARCHAR(255) NOT NULL,
    `CREATED_DATE`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`    VARCHAR(255) NULL,
    `MODIFIED_DATE`  TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`         INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_ATTENDANCE_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng chấm công';


DROP TABLE IF EXISTS `ATTENDANCE_LEAVE`;

CREATE TABLE `ATTENDANCE_LEAVE`
(
    `ID`             BIGINT       NOT NULL AUTO_INCREMENT,
    `LEAVE_CATEGORY` VARCHAR(255)          DEFAULT NULL,
    `START_DAY`      DATETIME(6)           DEFAULT NULL,
    `END_DAY`        DATETIME(6)           DEFAULT NULL,
    `TOTAL_TIME`     BIGINT                DEFAULT NULL,
    `DESCRIPTION`    TEXT,
    `EMPLOYEE_ID`    BIGINT                DEFAULT NULL,
    `REVIEWER_ID`    BIGINT                DEFAULT NULL,
    `CREATED_BY`     VARCHAR(255) NOT NULL,
    `CREATED_DATE`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`    VARCHAR(255) NULL,
    `MODIFIED_DATE`  TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`         INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_ATTENDANCE_LEAVE_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
    CONSTRAINT `FK_ATTENDANCE_LEAVE_REVIWER` FOREIGN KEY (`REVIEWER_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng đăng ký nghỉ phép';


DROP TABLE IF EXISTS `ATTENDANCE_OT`;
CREATE TABLE `ATTENDANCE_OT`
(
    `ID`             BIGINT       NOT NULL AUTO_INCREMENT,
    `START_DAY`      DATETIME(6)           DEFAULT NULL,
    `START_TIME`     DATETIME(6)           DEFAULT NULL,
    `END_TIME`       DATETIME(6)           DEFAULT NULL,
    `TOTAL_TIME`     DOUBLE                DEFAULT NULL,
    `EMPLOYEE_ID`    BIGINT                DEFAULT NULL,
    `FOLLOW_ID`      BIGINT                DEFAULT NULL,
    `DESCRIPTION_OT` VARCHAR(1000)         DEFAULT NULL,
    `CREATED_BY`     VARCHAR(255) NOT NULL,
    `CREATED_DATE`   TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`    VARCHAR(255) NULL,
    `MODIFIED_DATE`  TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`         INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_ATTENDANCE_OT_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
    CONSTRAINT `FK_ATTENDANCE_OT_FLLOW_ID` FOREIGN KEY (`FOLLOW_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='Bảng chấm công OT';


CREATE TABLE `EMPLOYEE_CONTRACTS`
(
    `ID`                      BIGINT       NOT NULL AUTO_INCREMENT,
    `EMPLOYEE_ID`             BIGINT,
    `EMPLOYEE_CODE`           VARCHAR(150),
    `CONTRACT_TYPE_DISPLAY`   VARCHAR(255),
    `CONTRACT_NUMBER`         VARCHAR(50),
    `CONTRACT_SIGN_DATE`      DATE,
    `CONTRACT_EFFECTIVE_DATE` DATE,
    `CONTRACT_END_DATE`       DATE,
    `CONTRACT_TERM_DISPLAY`   VARCHAR(255),
    `DESCRIPTION`             NVARCHAR(255),
    `STATUS`                  INT                   DEFAULT 1,
    `CREATED_BY`              VARCHAR(255) NOT NULL,
    `CREATED_DATE`            TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`             VARCHAR(255) NULL,
    `MODIFIED_DATE`           TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `SALARY_RATE`             FLOAT,
    `BASIC_SALARY_INSURANCE`  BIGINT,
    `APPLY_BUSINESS_SALARY`   VARCHAR(100),
    `BASIC_SALARY`            DECIMAL(19, 2),
    `CONTRACT_TYPE`           VARCHAR(50),
    `ATTACH_FILE`             VARCHAR(255),
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_EMPLOYEE_CONTRACTS_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;

-- Thêm COMMENT cho các cột trong MySQL
ALTER TABLE `EMPLOYEE_CONTRACTS`
    COMMENT 'Bảng lưu thông tin hợp đồng của nhân viên';

DROP TABLE IF EXISTS `PROJECT`;

/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `PROJECT`
(
    `ID`                   BIGINT       NOT NULL AUTO_INCREMENT,
    `PROJECT_CODE`         VARCHAR(255) NOT NULL,
    `PROJECT_NAME`         VARCHAR(255) NOT NULL,
    `PROJECT_DESCRIPTION`  VARCHAR(1000)         DEFAULT NULL,
    `START_DAY`            TIMESTAMP             DEFAULT NULL,
    `END_DAY`              TIMESTAMP             DEFAULT NULL,
    `CREATED_BY`           VARCHAR(255) NOT NULL,
    `PROJECT_MANAGER_CODE` VARCHAR(255) NOT NULL,
    `CLIENT_NAME`          VARCHAR(255) NOT NULL,
    `CREATED_DATE`         TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`          VARCHAR(255) NULL,
    `MODIFIED_DATE`        TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`               INT                   DEFAULT 1,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin dự án';

--
-- Table structure for table `TASK`
--
DROP TABLE IF EXISTS `TASK`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `TASK`
(
    `ID`               BIGINT       NOT NULL AUTO_INCREMENT,
    `TASK_NAME`        VARCHAR(255) NOT NULL,
    `TASK_CODE`        VARCHAR(255) NOT NULL,
    `TASK_DESCRIPTION` TEXT,
    `START_DAY`        TIMESTAMP             DEFAULT NULL,
    `END_DAY`          TIMESTAMP             DEFAULT NULL,
    `PROJECT_ID`       BIGINT                DEFAULT NULL,
    `EMPLOYEE_ID`      BIGINT                DEFAULT NULL,
    `MANAGER_CODE`     VARCHAR(255)          DEFAULT NULL,
    `PRIORITY`         INT                   DEFAULT NULL,
    `CREATED_BY`       VARCHAR(255) NOT NULL,
    `CREATED_DATE`     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`      VARCHAR(255) NULL,
    `MODIFIED_DATE`    TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`           INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_TASK_PROJECT` FOREIGN KEY (`PROJECT_ID`) REFERENCES `PROJECT` (`ID`),
    CONSTRAINT `FK_TASK_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng công việc';
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

LOCK TABLES `TASK` WRITE;
/*!40000 ALTER TABLE `TASK`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `TASK`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `task`
--

UNLOCK TABLES;


DROP TABLE IF EXISTS `ACCOUNT`;

/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `ACCOUNT`
(
    `ID`            BIGINT    NOT NULL AUTO_INCREMENT,
    `CODE`          VARCHAR(255) UNIQUE COMMENT 'Mã tài khoản',
    `PASSWORD`      VARCHAR(255) COMMENT 'Mật khẩu',
    `STATUS`        INT COMMENT 'Trạng thái tài khoản',
    `PW_EXP_DATE`   DATE COMMENT 'Ngày hết hạn mật khẩu',
    `NUM_PW_WRONG`  INT            DEFAULT 0 COMMENT 'Số lần nhập sai mật khẩu',
    `CREATED_BY`    VARCHAR(50),
    `CREATED_DATE`  TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`   VARCHAR(50)    DEFAULT NULL,
    `MODIFIED_DATE` TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `EMPLOYEE_ID`   BIGINT    NOT NULL,
    `ACCOUNT`       VARCHAR(255) COMMENT 'Tên tài khoản đăng nhập',
    `OTP`           VARCHAR(255) COMMENT 'Mã lấy mật khẩu',
    `YMD`           INT COMMENT 'Mã thời gian YMD (yyyymmdd)',
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_ACCOUNT_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin tài khoản người dùng';

CREATE TABLE `ROLE`
(
    `ID`          BIGINT NOT NULL AUTO_INCREMENT,
    `CODE`        VARCHAR(255) UNIQUE COMMENT 'Mã vai trò',
    `NAME`        VARCHAR(255) COMMENT 'Tên vai trò',
    `ACTIVE`      TINYINT(1) DEFAULT 1 COMMENT 'Trạng thái hoạt động (1: active, 0: inactive)',
    `IS_MASTER`   TINYINT(1) DEFAULT 0 COMMENT 'Là vai trò master hay không',
    `DESCRIPTION` VARCHAR(255) COMMENT 'Mô tả vai trò',
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng phân quyền vai trò người dùng';

INSERT INTO `ROLE` (`CODE`, `NAME`, `ACTIVE`, `IS_MASTER`, `DESCRIPTION`)
VALUES ('ADMIN', 'Quản trị viên', 1, 1, 'Vai trò quản trị hệ thống'),
       ('USER', 'Nhân viên', 1, 1, 'Vai trò quản trị hệ thống'),
       ('MANAGER', 'Quản lý', 1, 1, 'Vai trò quản lý');


CREATE TABLE `EMPLOYEE_ROLE_MAPPING`
(
    `EMPLOYEE_ID` BIGINT NOT NULL COMMENT 'ID nhân viên',
    `ROLE_ID`     BIGINT NOT NULL COMMENT 'ID vai trò',
    PRIMARY KEY (`EMPLOYEE_ID`, `ROLE_ID`),
    CONSTRAINT `FK_EMPLOYEE_ROLE_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`) ON DELETE CASCADE,
    CONSTRAINT `FK_EMPLOYEE_ROLE_ROLE` FOREIGN KEY (`ROLE_ID`) REFERENCES `ROLE` (`ID`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Mapping giữa nhân viên và vai trò';

DROP TABLE IF EXISTS `QUALIFICATION`;

CREATE TABLE `QUALIFICATION`
(
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `LEVEL`         VARCHAR(255)          DEFAULT NULL,
    `NAME`          VARCHAR(255)          DEFAULT NULL,
    `MAJOR`         VARCHAR(255)          DEFAULT NULL,
    `DESCRIPTION`   TEXT                  DEFAULT NULL,
    `LICENSE_DATE`  DATE                  DEFAULT NULL,
    `EMPLOYEE_ID`   BIGINT                DEFAULT NULL,
    `CREATED_BY`    VARCHAR(255) NOT NULL,
    `CREATED_DATE`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`   VARCHAR(255) NULL,
    `MODIFIED_DATE` TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`        INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_QUALIFICATION_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin bằng cấp/chứng chỉ';

/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;


DROP TABLE IF EXISTS `WAGE`;

/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;

CREATE TABLE `ALLOWANCE`
(
    `ID`                    BIGINT       NOT NULL AUTO_INCREMENT,
    `ALLOWANCE_CODE`        VARCHAR(255)          DEFAULT NULL,
    `ALLOWANCE_NAME`        VARCHAR(255)          DEFAULT NULL,
    `ALLOWANCE_BASE`        DECIMAL(19, 2)        DEFAULT NULL,
    `ALLOWANCE_DESCRIPTION` VARCHAR(255)          DEFAULT NULL,
    `ATTACH_FILE`           VARCHAR(255)          DEFAULT NULL COMMENT 'Tệp đính kèm',
    `CREATED_BY`            VARCHAR(255) NOT NULL,
    `CREATED_DATE`          TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`           VARCHAR(255) NULL,
    `MODIFIED_DATE`         TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`                INT                   DEFAULT 1,
    PRIMARY KEY (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu thông tin mức lương';

CREATE TABLE `EMPLOYEE_ALLOWANCE`
(
    `ID`           BIGINT NOT NULL AUTO_INCREMENT,
    `EMPLOYEE_ID`  BIGINT NOT NULL,
    `ALLOWANCE_ID` BIGINT NOT NULL,
    `CREATED_DATE` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_EA_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
    CONSTRAINT `FK_EA_ALLOWANCE` FOREIGN KEY (`ALLOWANCE_ID`) REFERENCES `ALLOWANCE` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng trung gian liên kết nhân viên và phụ cấp';


CREATE TABLE `COMMENT`
(
    `ID`            BIGINT       NOT NULL AUTO_INCREMENT,
    `CONTENT`       TEXT         NOT NULL COMMENT 'Nội dung bình luận',
    `EMPLOYEE_ID`   BIGINT       NOT NULL,
    `TASK_ID`       BIGINT       NOT NULL,
    `EMPLOYEE_CODE` VARCHAR(50)  NOT NULL,
    `TASK_CODE`     VARCHAR(50)  NOT NULL,
    `CREATED_DATE`  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `MODIFIED_BY`   VARCHAR(255) NULL,
    `MODIFIED_DATE` TIMESTAMP             DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
    `STATUS`        INT                   DEFAULT 1,
    PRIMARY KEY (`ID`),
    CONSTRAINT `FK_COMMENT_EMPLOYEE` FOREIGN KEY (`EMPLOYEE_ID`) REFERENCES `EMPLOYEE` (`ID`),
    CONSTRAINT `FK_COMMENT_TASK` FOREIGN KEY (`TASK_ID`) REFERENCES `TASK` (`ID`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci
    COMMENT ='Bảng lưu bình luận của nhân viên theo công việc';

/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;


