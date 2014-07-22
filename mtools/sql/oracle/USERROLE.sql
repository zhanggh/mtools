-- Create table
create table USERROLE
(
  USERID VARCHAR2(32) not null,
  ROLEID NUMBER(30) not null
);
 
-- Create/Recreate primary, unique and foreign key constraints 
alter table USERROLE
  add constraint FSFSFWWERWESW primary key (ROLEID, USERID)
  using index 
  tablespace USERS
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