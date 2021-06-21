CREATE DEFINER=`root`@`localhost` TRIGGER `deptcourse_BEFORE_INSERT` BEFORE INSERT ON `deptcourse` FOR EACH ROW BEGIN
	DECLARE college_s varchar(50);
	DECLARE deptName_s varchar (50);
    DECLARE chair_s varchar (50);
    DECLARE officeBldg_s varchar (10);
    DECLARE officeNo_s integer;
    DECLARE count integer;
    SELECT count(*) INTO count FROM deptcourse WHERE deptName=NEW.deptName;
    IF(count>0) THEN
		SELECT chair INTO chair_s FROM deptcourse WHERE deptName=NEW.deptName;
        SELECT officeBldg INTO officeBldg_s FROM deptcourse WHERE deptName=NEW.deptName;
        SELECT officeNo INTO officeNo_s FROM deptcourse WHERE deptName=NEW.deptName;
        SELECT college INTO college_s FROM deptcourse WHERE deptName=NEW.deptName;
        SELECT deptName INTO deptName_s FROM deptcourse WHERE deptName=NEW.deptName;
        IF(college_s!=NEW.college OR deptName_s!=NEW.deptName OR chair_s!=NEW.chair OR officeBldg_s!=NEW.officeBldg 
        OR officeNo_s!= NEW.officeNo) THEN
			SIGNAL SQLSTATE '40000'
			SET MESSAGE_TEXT='THE INSERT INFORMATION WAS NOT CORRECT';
        END IF;
    END IF;
END