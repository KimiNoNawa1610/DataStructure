CREATE DEFINER=`root`@`localhost` TRIGGER `joannestore_BEFORE_UPDATE` BEFORE UPDATE ON `joannestore` FOR EACH ROW BEGIN
	call store_check(new.cityID,new.storeName);
END