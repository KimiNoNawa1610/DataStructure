CREATE DEFINER=`root`@`localhost` TRIGGER `course_BEFORE_UPDATE` BEFORE UPDATE ON `course` FOR EACH ROW BEGIN
	IF(NEW.chair!=OLD.chair)
    THEN
		SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT='CANNOT CHANGE CHAIR IN COURSE TABLE';
	END IF;
END