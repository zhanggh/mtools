use goulm;
 
DROP TABLE IF EXISTS `userinfo`;
CREATE TABLE `userinfo` (
  `userid` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `username` varchar(20) CHARACTER SET utf8 DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `workphone` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
   `parentid` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `status` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `usertype` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `email` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `addr` varchar(255) CHARACTER SET utf8 DEFAULT NULL,
  `reserved` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '备注',
  `verifycode` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '验证码',
  `depid` varchar(255) COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '所属部门',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`userid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

