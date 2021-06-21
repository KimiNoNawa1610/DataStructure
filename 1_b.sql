CREATE DEFINER=`root`@`localhost` TRIGGER `course_BEFORE_INSERT` BEFORE INSERT ON `course` FOR EACH ROW BEGIN
	IF NEW.`chair` IS NULL OR NEW.`chair`='' THEN
		SET NEW.`chair` = 
        (
			SELECT chair FROM department dept
            WHERE NEW.departmentName = dept.deptName
        );
	END IF;
END