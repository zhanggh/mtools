-- Create table
create table ROLE
(
  ROLEID   NUMBER(20) not null,
  ROLENAME VARCHAR2(40) not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table ROLE
  add constraint ROLE20140505 primary key (ROLEID, ROLENAME)
  using index 
  tablespace WX_SALERECORD
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );