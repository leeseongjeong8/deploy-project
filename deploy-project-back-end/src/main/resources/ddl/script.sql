CREATE TABLE DEPLOY.SCRIPT (
	SCRIPT_NO NUMBER NOT NULL,
	DEPLOY_NO NUMBER,
	CATEGORY VARCHAR(20) NOT NULL,
	FILE_TYPE VARCHAR(20) NOT NULL,
	LOCAL_PATH VARCHAR(500) NOT NULL,
	DIRECTORY_PATH VARCHAR(500) NOT NULL,
	BACKUP_SCRIPT VARCHAR(500) NOT NULL,
	REFLECT_SCRIPT VARCHAR(500) NOT NULL,
	ROLLBACK_SCRIPT VARCHAR(500) NOT NULL,
	JAR_SCRIPT VARCHAR(500)
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE DEPLOY.SCRIPT IS '스크립트';
COMMENT ON COLUMN DEPLOY.SCRIPT.BACKUP_SCRIPT IS '백업스크립트';
COMMENT ON COLUMN DEPLOY.SCRIPT.CATEGORY IS '구분';
COMMENT ON COLUMN DEPLOY.SCRIPT.DEPLOY_NO IS '배포번호';
COMMENT ON COLUMN DEPLOY.SCRIPT.DIRECTORY_PATH IS '디렉토리경로';
COMMENT ON COLUMN DEPLOY.SCRIPT.FILE_TYPE IS '파일타입';
COMMENT ON COLUMN DEPLOY.SCRIPT.JAR_SCRIPT IS 'JAR/WAR스크립트';
COMMENT ON COLUMN DEPLOY.SCRIPT.LOCAL_PATH IS '소스경로';
COMMENT ON COLUMN DEPLOY.SCRIPT.REFLECT_SCRIPT IS '반영스크립트';
COMMENT ON COLUMN DEPLOY.SCRIPT.ROLLBACK_SCRIPT IS '원복스크립트';
COMMENT ON COLUMN DEPLOY.SCRIPT.SCRIPT_NO IS '스크립트번호';

CREATE UNIQUE INDEX DEPLOY.SCRIPT_NO_PK ON DEPLOY.SCRIPT (
	SCRIPT_NO ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

ALTER TABLE DEPLOY.SCRIPT ADD CONSTRAINT SCRIPT_NO_PK
PRIMARY KEY (
	SCRIPT_NO
);

ALTER TABLE DEPLOY.SCRIPT ADD CONSTRAINT FK_SCRIPT_DEPLOY_NO_SCRIPT_NO
FOREIGN KEY (
	DEPLOY_NO
) REFERENCES DEPLOY.DEPLOY_MANAGEMENT (
	DEPLOY_NO
);