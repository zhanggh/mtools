-- Create table
create table ROLEPERM
(
  ROLEID VARCHAR2(20) not null,
  PERMID VARCHAR2(20) not null
);
-- Add comments to the table 
comment on table ROLEPERM
  is '角色权限关联表';
-- Create/Recreate primary, unique and foreign key constraints 
alter table ROLEPERM
  add constraint ROLEPERM20140505 primary key (ROLEID, PERMID)
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