CREATE DEFINER=`root`@`localhost` TRIGGER `deptcourse_BEFORE_UPDATE` BEFORE UPDATE ON `deptcourse` FOR EACH ROW BEGIN
	IF(OLD.college!=NEW.college OR OLD.deptName!=NEW.deptName OR OLD.chair!=NEW.chair OR OLD.officeBldg!=NEW.officeBldg 
        OR OLD.officeNo!= OLD.officeNo) THEN
			SIGNAL SQLSTATE '45000'
			SET MESSAGE_TEXT='CANNOT CHANGE DEPARTMENTAL INFORMATION';
        END IF;
END