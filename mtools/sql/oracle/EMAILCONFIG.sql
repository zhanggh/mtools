-- Create table
create table EMAILCONFIG
(
  ID       VARCHAR2(11),
  TOEMAILS VARCHAR2(881),
  SUBJECT  VARCHAR2(881),
  CONTEXT  VARCHAR2(2881),
  SAVEDIR  VARCHAR2(2881),
  APPTYPE  VARCHAR2(2)
)
tablespace WX_SALERECORD
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Add comments to the table 
comment on table EMAILCONFIG
  is '邮件通知配置';
comment on column EMAILCONFIG.SAVEDIR
  is '接收邮件保存目录';