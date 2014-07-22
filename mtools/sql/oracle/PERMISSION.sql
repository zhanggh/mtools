-- Create table
create table PERMISSION
(
  PERMID    NUMBER(20) not null,
  MENUID    NUMBER(30),
  PERMURI   VARCHAR2(128),
  PERMNAME  VARCHAR2(64),
  PERMDESC  VARCHAR2(2048),
  PERMCLASS VARCHAR2(100),
  PERMTYPE  VARCHAR2(20),
  PERMLEVEL NUMBER
);
-- Add comments to the table 
comment on table PERMISSION
  is '权限信息表';
-- Add comments to the columns 
comment on column PERMISSION.PERMID
  is '权限编号';
comment on column PERMISSION.MENUID
  is '所属菜单编号';
comment on column PERMISSION.PERMURI
  is '权限uri';
comment on column PERMISSION.PERMNAME
  is '权限名称';
comment on column PERMISSION.PERMDESC
  is '描述';
comment on column PERMISSION.PERMCLASS
  is '访问的类，如：com.allinpay.stms.xxx';
comment on column PERMISSION.PERMTYPE
  is '权限类型，用于区别不同系统的权限，如：mo 或者weixin';
comment on column PERMISSION.PERMLEVEL
  is '权限级别，0级，无需记录操作日志，1级以上均有记录操作记录';