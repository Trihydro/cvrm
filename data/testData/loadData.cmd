call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'equipmenttypedata.txt' INTO TABLE equipment_type;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'equipmentdata.txt' INTO TABLE equipment;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'organizationdata.txt' INTO TABLE organization;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'roledata.txt' INTO TABLE role;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'vehicleclassdata.txt' INTO TABLE vehicle_class;" -u <username> --password=<password>  

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'vehicledata.txt' INTO TABLE vehicle;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'participantdata.txt' INTO TABLE participant;" -u <username> --password=<password> 

call mysql -e "use cvpt; LOAD DATA LOCAL INFILE 'userdata.txt' INTO TABLE user;" -u <username> --password=<password> 

 