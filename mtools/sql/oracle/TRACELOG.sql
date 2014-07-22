-- Create table
create table TRACELOG
(
  OPTID      VARCHAR2(20),
  LOGINUSER  VARCHAR2(20),
  OPT_URL    VARCHAR2(80),
  OPT_NAME   VARCHAR2(80),
  ORG_PARAMS VARCHAR2(2080),
  OPT_TIME   TIMESTAMP(6),
  OPT_RESULT VARCHAR2(100),
  FROM_IP VARCHAR2(100)
);
 
-- Add comments to the table 
comment on table TRACELOG
  is '访问痕迹跟踪表';
-- Add comments to the columns 
comment on column TRACELOG.OPTID
  is '编号';
comment on column TRACELOG.LOGINUSER
  is '访问者';
comment on column TRACELOG.OPT_URL
  is '访问url';
comment on column TRACELOG.OPT_NAME
  is '功能名称';
comment on column TRACELOG.ORG_PARAMS
  is '原参数';
comment on column TRACELOG.OPT_TIME
  is '操作时间';
comment on column TRACELOG.FROM_IP
  is '操作人源ip';
comment on column TRACELOG.OPT_RESULT
  is '操作结果';