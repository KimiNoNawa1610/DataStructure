CREATE DEFINER=`root`@`localhost` TRIGGER `joannestore_BEFORE_INSERT` BEFORE INSERT ON `joannestore` FOR EACH ROW BEGIN
	call store_check(new.cityID,new.storeName);
END