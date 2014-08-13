use goulm;
DROP TABLE IF EXISTS `userrole`;
CREATE TABLE `userrole` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `userid` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `roleid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
