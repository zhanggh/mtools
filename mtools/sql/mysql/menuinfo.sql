use goulm;
DROP TABLE IF EXISTS `menuinfo`;
CREATE TABLE `menuinfo` (
  `MENUID` int(11) NOT NULL AUTO_INCREMENT,
  `parentid` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '父级菜单id',
  `menuname` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单名称',
  `linkurl` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '链接URI',
  `menutype` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '菜单类型(比如所属系统)',
  `ordernum` int(11) DEFAULT '0' COMMENT '菜单显示顺序',
  PRIMARY KEY (`MENUID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='菜单信息';

 
LOCK TABLES `menuinfo` WRITE;
UNLOCK TABLES;
 