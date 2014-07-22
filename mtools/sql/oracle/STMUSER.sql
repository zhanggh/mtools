-- Create table
create table STMUSER
(
  USERID      VARCHAR2(32) not null,
  USERNAME    VARCHAR2(20),
  PASSWORD    VARCHAR2(32),
  WORKPHONE   VARCHAR2(20),
  MOBILEPHONE VARCHAR2(20),
  STATUS      VARCHAR2(8),
  TYPE        VARCHAR2(8),
  EMAIL       VARCHAR2(40),
  ADDR        VARCHAR2(140),
  RESERVED    VARCHAR2(60),
  VERIFYCODE  VARCHAR2(60),
  DEPID       VARCHAR2(20),
  CREATE_TIME TIMESTAMP(6)
);
-- Add comments to the table 
comment on table STMUSER
  is '用户信息表，包括运营人员信息和普通用户信息';
-- Add comments to the columns 
comment on column STMUSER.USERID
  is '登陆名';
comment on column STMUSER.USERNAME
  is '用户姓名';
comment on column STMUSER.PASSWORD
  is '用户密码';
comment on column STMUSER.WORKPHONE
  is '工作电话';
comment on column STMUSER.MOBILEPHONE
  is '移动电话';
comment on column STMUSER.STATUS
  is '状态';
comment on column STMUSER.TYPE
  is '用户类型';
comment on column STMUSER.EMAIL
  is '邮箱';
comment on column STMUSER.ADDR
  is '住址';
comment on column STMUSER.RESERVED
  is '保留字段';
comment on column STMUSER.VERIFYCODE
  is '验证码';
comment on column STMUSER.DEPID
  is '所属部门';
comment on column STMUSER.CREATE_TIME
  is '创建时间';
-- Create/Recreate primary, unique and foreign key constraints 
alter table STMUSER
  add constraint SDFSDFSDZ3424234 primary key (USERID)
  using index;