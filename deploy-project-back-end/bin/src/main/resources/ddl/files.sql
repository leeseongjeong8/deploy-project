CREATE TABLE DEPLOY.FILES (
	FILE_NO NUMBER NOT NULL,
	NAME VARCHAR(300) NOT NULL,
	DIRECTORY_PATH VARCHAR(300) NOT NULL,
	DELETE_YN CHAR(1) NOT NULL,
	REGDATE DATE NOT NULL,
	TYPE CHAR(1) NOT NULL,
	TYPE_NO NUMBER NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE DEPLOY.FILES IS '파일';
COMMENT ON COLUMN DEPLOY.FILES.DELETE_YN IS '삭제여부';
COMMENT ON COLUMN DEPLOY.FILES.DIRECTORY_PATH IS '저장위치';
COMMENT ON COLUMN DEPLOY.FILES.FILE_NO IS '파일번호';
COMMENT ON COLUMN DEPLOY.FILES.NAME IS '파일명';
COMMENT ON COLUMN DEPLOY.FILES.REGDATE IS '등록일';
COMMENT ON COLUMN DEPLOY.FILES.TYPE IS '구분';
COMMENT ON COLUMN DEPLOY.FILES.TYPE_NO IS '게시판/배포번호';

CREATE UNIQUE INDEX DEPLOY.FILE_NO_PK ON DEPLOY.FILES (
	FILE_NO ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

ALTER TABLE DEPLOY.FILES ADD CONSTRAINT FILE_NO_PK
PRIMARY KEY (
	FILE_NO
);CREATE TABLE DEPLOY.FILES (
	FILE_NO NUMBER NOT NULL,
	NAME VARCHAR(300) NOT NULL,
	DIRECTORY_PATH VARCHAR(300) NOT NULL,
	DELETE_YN CHAR(1) NOT NULL,
	REGDATE DATE NOT NULL,
	TYPE CHAR(1) NOT NULL,
	TYPE_NO NUMBER NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE DEPLOY.FILES IS '파일';
COMMENT ON COLUMN DEPLOY.FILES.DELETE_YN IS '삭제여부';
COMMENT ON COLUMN DEPLOY.FILES.DIRECTORY_PATH IS '저장위치';
COMMENT ON COLUMN DEPLOY.FILES.FILE_NO IS '파일번호';
COMMENT ON COLUMN DEPLOY.FILES.NAME IS '파일명';
COMMENT ON COLUMN DEPLOY.FILES.REGDATE IS '등록일';
COMMENT ON COLUMN DEPLOY.FILES.TYPE IS '구분';
COMMENT ON COLUMN DEPLOY.FILES.TYPE_NO IS '게시판/배포번호';

CREATE UNIQUE INDEX DEPLOY.FILE_NO_PK ON DEPLOY.FILES (
	FILE_NO ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

ALTER TABLE DEPLOY.FILES ADD CONSTRAINT FILE_NO_PK
PRIMARY KEY (
	FILE_NO
);
