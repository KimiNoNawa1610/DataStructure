SET SQL_SAFE_UPDATES = 0;
CREATE TABLE city (
  cityID int NOT NULL,
  districtName varchar(50) NOT NULL,
  PRIMARY KEY (cityID)
);

CREATE TABLE joannestore (
  cityID int NOT NULL,
  storeName varchar(50) NOT NULL,
  storeManager varchar(50) NOT NULL,
  storeRevenue float NOT NULL,
  PRIMARY KEY (cityID, storeName),
  CONSTRAINT joannestore_fk FOREIGN KEY (cityID) REFERENCES city (cityID)
);

insert into city (cityID, districtName)
	values(1, "South Bay"),(2, "South Bay");

insert into joannestore (cityID, storeName, storeManager, storeRevenue )
    values (1, "Palo Verde", "Eliza Doolittle", 500005.82),
           (1, "Towne Center", "Milly Cyrus", 382234.88),
           (2, "Fashion Center", "David Duchovny", 128000.83);
           
insert into joannestore (cityID, storeName, storeManager, storeRevenue )# expected to fail
    values  (2, "Towne Center", "Michael J. Fox", 512384.22);

Update joannestore set storeName="Palo Verde" WHERE cityID=2;# expected to fail