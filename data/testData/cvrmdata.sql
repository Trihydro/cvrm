/* **** Role **** */

INSERT INTO role (role, role_description) VALUES ('ROLE_ADMIN', 'Administrator');
INSERT INTO role (role, role_description) VALUES ('ROLE_READER', 'Read Only');
INSERT INTO role (role, role_description) VALUES ('ROLE_TRAINING', 'Trainer');
INSERT INTO role (role, role_description) VALUES ('ROLE_EQUIPMENT', 'Equipment Admin');
INSERT INTO role (role, role_description) VALUES ('ROLE_VEHICLE', 'Vehicle Manager');

/* **** Equipment Type **** */

INSERT INTO equipment_type (equipment_type) VALUES ('RSU');
INSERT INTO equipment_type (equipment_type) VALUES ('OBU-S OBU-D Environment Sensors HMI CAN Bus');
INSERT INTO equipment_type (equipment_type) VALUES ('OBU-S OBU-D HMI CAN Bus');
INSERT INTO equipment_type (equipment_type) VALUES ('OBU-S OBU-D HMI');
INSERT INTO equipment_type (equipment_type) VALUES ('OBU-D HMI');

/* **** Organization **** */
INSERT INTO organization (name, is_trucking_company) VALUES ('Wyoming DOT', 'N');
INSERT INTO organization (name, is_trucking_company) VALUES ('Wyoming State Police', 'N');
INSERT INTO organization (name, is_trucking_company) VALUES ('Trihydro', 'N');

/* **** Training Type **** */

INSERT INTO training_type (training_type) VALUES ('Online');
INSERT INTO training_type (training_type) VALUES ('Simulator');

/* **** Vehicle Class **** */

INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (1, 'Motorcycles', 'All two or three-wheeled motorized vehicles. Typical vehicles in this category have saddle type seats and are steered by handlebars rather than steering wheels. This category includes motorcycles, motor scooters, mopeds, motor-powered bicycles, and three-wheel motorcycles.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (2, 'Passenger Cars', 'All sedans, coupes, and station wagons manufactured primarily for the puspose of carrying passengers and including those passenger cars pulling recreational or other light trailers.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (3, 'Other Two-Axle, Four-Tire Single Unit Vehicles', 'All two-axle, four-tire, vehicles, other than passenger cars. Included in this classification are pickups, panels, vans, and other vehicles such as campers, motor homes, ambulances, hearses, carryalls, and minibuses. Other two-axle, four-tire single-unit vehicles pulling recreational or other light trailers are included in this classification. Because automatic vehicle classifiers have difficulty distinguishing class 3 from class 2, these two classes may be combined into class 2.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (4, 'Buses', 'All vehicles manufactured as traditional passenger-carrying buses with two axles and six tires or three or more axles. This category includes only traditional buses (including school buses) functioning as passenger-carrying vehicles. Modified buses should be considered to be a truck and should be appropriately classified.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (5, 'Two-Axle, Six-Tire, Single-Unit Trucks', 'All vehicles on a single frame including trucks, camping and recreational vehicles, motor homes, etc., with two axles and dual rear wheels.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (6, 'Three-Axle Single-Unit Trucks', 'All vehicles on a single frame including trucks, camping and recreational vehicles, motor homes, etc., with three axles.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (7, 'Four or More Axle Single-Unit Trucks', 'All trucks on a single frame with four or more axles.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (8, 'Four or Fewer Axle Single-Trailer Trucks', 'All vehicles with four or fewer axles consisting of two units, one of which is a tractor or straight truck power unit.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (9, 'Five-Axle Single-Trailer Trucks', 'All five-axle vehicles consisting of two units, one of which is a tractor or straight truck power unit.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (10, 'Six or More Axle Single-Trailer Trucks', 'All vehicles with six or more axles consisting of two units, one of which is a tractor or straight truck power unit.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (11, 'Five or Fewer Axle Multi-Trailer Trucks', 'All vehicles with five or fewer axles consisting of three or more units, one of which is a tractor or straight truck power unit.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (12, 'Six-Axle Multi-Trailer Trucks', 'All six-axle vehicles consisting of three or more units, one of which is a tractor or straight truck power unit.');
INSERT INTO vehicle_class (vehicle_class_number, vehicle_class, description) VALUES (13, 'Seven or More Axle Multi-Trailer Trucks', 'All vehicles with seven or more axles consisting of three or more units, one of which is a tractor or straight truck power unit.');

/* **** Training **** */
INSERT INTO training (training, course_id, training_type_id, notes) VALUES ('HMI Training', '2352', 1, 'Training course for system testing');
INSERT INTO training (training, course_id, training_type_id, notes) VALUES ('UW Driving Simulator', '5633', 2, 'version 2 of simulator - Training course for system testing');


/* **** Vehicle **** */
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('407 GON', 7, 1, 'Ferrari', 'FXX K', 'Vehicle for system testing' );
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('488 XTC', 7, 1, 'Ferrari', '488 GTB', 'Vehicle for system testing' );
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('700 FST', 7, 1, 'Ferrari', 'F12tdf', 'Vehicle for system testing' );
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('172', 7, 2, 'Dodge', 'Charger', 'Vehicle for system testing' );
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('13 820', 12, 3, 'Kenworth', 'W900L', 'Vehicle for system testing' );
INSERT INTO vehicle (id, vehicle_class_id, organization_id, make, model, notes) VALUES ('13 900', 12, 3, 'Kenworth', 'W900L', 'Vehicle for system testing' );

/* **** Equipment Component Type **** */
INSERT INTO equipment_component_type (equipment_component_type) VALUES ('Antenna');
INSERT INTO equipment_component_type (equipment_component_type) VALUES ('Cable');

/* **** Equipment **** */
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, notes) VALUES ('WD98327FE', 1, 'GClocomate300rsu0002', 'Locomate Commando RSU', '0026AD052640', 'Equipment for system testing' );
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, latitude, longitude, elevation, height, date_installed, notes) VALUES ('WD00392RE', 1, 'GClocomate300rsu0007', 'Locomate Commando RSU', '0026AD052690', 41.3015, -105.6104, 7165, 12, '24-jan-2017', 'Equipment for system testing' );
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, notes) VALUES ('WD00394OR', 2, 'GClocomate300asu00025', 'Locomate OBU', '0026AD0514F0', 'Equipment for system testing' );
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, notes) VALUES ('WD00401OR', 2, 'GClocomate300asd00029', 'Locomate OBU', '0026AD051530', 'Equipment for system testing' );
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, notes) VALUES ('WD00382OR', 3, 'F1LOCOMINI00335', 'OBU mini', '0026AD051115', 'Equipment for system testing' );
INSERT INTO equipment (asset_id, equipment_type_id, serial_number, model_number, wan, notes) VALUES ('WD00441OR', 4, 'F1LOCOMINI00323', 'OBU mini', '0026AD0510FD', 'Equipment for system testing' );


/* **** Participant **** */
INSERT INTO participant (first_name, last_name, email, organization_id, start_date) VALUES ('Rex', 'Smith', 'rsmith@gmail.com', 2, '01-apr-17');
INSERT INTO participant (first_name, last_name, email, organization_id, start_date) VALUES ('Roxy', 'Perry', 'rperry@gmail.com', 1, '01-jan-17');
INSERT INTO participant (first_name, last_name, email, organization_id, start_date) VALUES ('Cali', 'Zumpf', 'czumpf@gmail.com', 1, '01-jan-17');
INSERT INTO participant (first_name, last_name, email, organization_id) VALUES ('Olli', 'Jones', 'jonesman@gmail.com', 2);
INSERT INTO participant (first_name, last_name, email, organization_id, start_date) VALUES ('Anna', 'Karina', 'akarina@trihydro.com', 3, '08-feb-17');

/* **** Participant Training **** */
INSERT INTO participant_training (participant_id, training_id, time_to_complete, date_completed) VALUES (3, 2, 181, '10-apr-2017'); 
INSERT INTO participant_training (participant_id, training_id, time_to_complete, date_completed) VALUES (13, 2, 100, '14-jan-2017'); 
INSERT INTO participant_training (participant_id, training_id, time_to_complete, date_completed) VALUES (14, 2, 147, '15-jan-2017'); 
INSERT INTO participant_training (participant_id, training_id) VALUES (15, 18);
