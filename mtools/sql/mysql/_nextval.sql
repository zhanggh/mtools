use goulm;
DROP FUNCTION IF EXISTS `_nextval`;
CREATE DEFINER=`root`@`localhost` FUNCTION `_nextval`(n varchar(50)) RETURNS int(11)
begin
declare _cur integer;
set _cur=(select current_value from tb_sequence where name= n);
update tb_sequence
 set current_value = _cur + _increment
 where name=n ;
return _cur;
end;
