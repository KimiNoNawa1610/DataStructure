CREATE DEFINER=`root`@`localhost` PROCEDURE `store_check`(cityID_s int, storeName_s varchar(50))
BEGIN
	DECLARE Count INT;
    SELECT count(storeName) INTO Count from city INNER JOIN joannestore USING(cityID) WHERE storeName=storeName_s
    AND districtName=(SELECT districtName from city WHERE cityID= cityID_s);
    IF(Count>0) THEN
		SIGNAL SQLSTATE '40000'
		SET MESSAGE_TEXT='The store already exist in that district';
    END IF;
END