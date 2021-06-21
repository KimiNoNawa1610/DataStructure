/*Trigger to update course if the chair in the department change.*/
CREATE DEFINER=`root`@`localhost` TRIGGER `department_AFTER_UPDATE` AFTER UPDATE ON `department` FOR EACH ROW BEGIN
	IF NEW.chair != OLD.chair THEN
    UPDATE course
        SET chair = NEW.chair
        WHERE NEW.deptName = departmentName;
    END IF;
END