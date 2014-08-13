
use goulm;
-- Create table department
create table DEPARTMENT
(
  depid   int(11) NOT NULL AUTO_INCREMENT COMMENT '部门id',
  depname   varchar(50) COMMENT '备注',
  parentid  varchar(20) COMMENT '父级部门id',
  principal varchar(50) COMMENT '部门负责人',
  reserved  varchar(200) COMMENT '备注',
  PRIMARY KEY (`depid`)
);