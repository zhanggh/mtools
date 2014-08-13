-- Create table
create table MENUINFO
(
  MENUID   NUMBER(30) not null,
  PARENTID NUMBER(30),
  MENUNAME     VARCHAR2(50),
  HELP     VARCHAR2(2048),
  LINKURL  VARCHAR2(128),
  ORDERNUM NUMBER(8) default 0,
  MENUTYPE VARCHAR2(20)
);
-- Add comments to the columns 
comment on column MENUINFO.MENUID
  is '菜单编号';
comment on column MENUINFO.PARENTID
  is '父级编号';
comment on column MENUINFO.MENUNAME
  is '菜单名称';
comment on column MENUINFO.HELP
  is '帮助信息';
comment on column MENUINFO.LINKURL
  is '链接';
comment on column MENUINFO.ORDERNUM
  is '顺序';
comment on column MENUINFO.MENUTYPE
  is '菜单类型：mo或者weixin';