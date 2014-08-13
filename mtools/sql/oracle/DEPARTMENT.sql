-- Create table department
create table DEPARTMENT
(
  DEPID     VARCHAR2(20),
  DEPNAME   VARCHAR2(50),
  PARENTID  VARCHAR2(20),
  PRINCIPAL VARCHAR2(50),
  RESERVED  VARCHAR2(200)
);
-- Add comments to the table 
comment on table DEPARTMENT
  is '部门';
-- Add comments to the columns 
comment on column DEPARTMENT.DEPID
  is '部门编号';
comment on column DEPARTMENT.DEPNAME
  is '部门名称';
comment on column DEPARTMENT.PARENTID
  is '父级部门';
comment on column DEPARTMENT.PRINCIPAL
  is '负责人，对应用户id';
comment on column DEPARTMENT.RESERVED
  is '保留字段';