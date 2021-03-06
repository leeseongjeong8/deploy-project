CREATE TABLE DEPLOY.BOARD (
	BOARD_NO NUMBER NOT NULL,
	WRITER VARCHAR(20) NOT NULL,
	TITLE VARCHAR(90) NOT NULL,
	BOARD_TYPE CHAR(1) NOT NULL,
	REGDATE DATE NOT NULL,
	UPDATE_DATE DATE,
	VIEW_COUNT NUMBER(10) NOT NULL,
	CONTENT BLOB NOT NULL
)
TABLESPACE USR
PCTFREE 10
INITRANS 2
STORAGE (
	MAXEXTENTS UNLIMITED
)
LOGGING
NOPARALLEL;
COMMENT ON TABLE DEPLOY.BOARD IS '자료실';
COMMENT ON COLUMN DEPLOY.BOARD.BOARD_NO IS '게시판번호';
COMMENT ON COLUMN DEPLOY.BOARD.BOARD_TYPE IS '유형';
COMMENT ON COLUMN DEPLOY.BOARD.CONTENT IS '내용';
COMMENT ON COLUMN DEPLOY.BOARD.REGDATE IS '등록일';
COMMENT ON COLUMN DEPLOY.BOARD.TITLE IS '제목';
COMMENT ON COLUMN DEPLOY.BOARD.UPDATE_DATE IS '수정일';
COMMENT ON COLUMN DEPLOY.BOARD.VIEW_COUNT IS '조회수';
COMMENT ON COLUMN DEPLOY.BOARD.WRITER IS '작성자';

CREATE UNIQUE INDEX DEPLOY.BOARD_NO_PK ON DEPLOY.BOARD (
	BOARD_NO ASC
)
LOGGING
TABLESPACE USR
PCTFREE 10
INITRANS 2;

ALTER TABLE DEPLOY.BOARD ADD CONSTRAINT BOARD_NO_PK
PRIMARY KEY (
	BOARD_NO
);

ALTER TABLE DEPLOY.BOARD ADD CONSTRAINT FK_BOARD_WRITER_MEMBER_ID
FOREIGN KEY (
	WRITER
) REFERENCES DEPLOY.MEMBER (
	ID
);