use goulm;
DROP TABLE IF EXISTS `tb_sequence`;
CREATE TABLE `tb_sequence` (
  `name` varchar(50) NOT NULL,
  `current_value` int(11) NOT NULL,
  `_increment` int(11) NOT NULL DEFAULT '1',
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
LOCK TABLES `tb_sequence` WRITE;
INSERT INTO `tb_sequence` VALUES ('roleid',17,1);
UNLOCK TABLES;
 