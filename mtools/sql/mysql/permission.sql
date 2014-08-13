USE `goulm`;

DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `permid` int(11) NOT NULL AUTO_INCREMENT,
  `menuid` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '所属菜单',
  `permuri` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限URI',
  `permname` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限名称',
  `permdesc` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限描述',
  `permclass` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限控制的类',
  `permtype` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '权限类别',
  `permlevel` int(11),
   PRIMARY KEY (`permid`)
);

 