USE `goulm`;


DROP TABLE IF EXISTS `emailconfig`;
CREATE TABLE `emailconfig` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `toemails` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '目标地址，多个地址是以英文逗号分隔',
  `subject` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '主题',
  `context` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '邮件内容',
  `savedir` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '邮件备份目录',
  `apptype` varchar(255) COLLATE utf8_bin DEFAULT '01' COMMENT '邮件通知类型',
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
 