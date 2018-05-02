
/* **** Role Table **** */

CREATE TABLE role
(
    role_id             NUMBER(10) NOT NULL,
    role                VARCHAR2(255) NOT NULL,
    role_description    VARCHAR2(255) NOT NULL,
    CONSTRAINT role_pk PRIMARY KEY (role_id)
);

/* generic type sequence used for mulitple table ids  */
/* used for all tables that will not create many rows */
CREATE SEQUENCE type_seq START WITH 1;

CREATE OR REPLACE TRIGGER role_bir
BEFORE INSERT ON role
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.role_id
    FROM   dual;
END;
/

/* **** Log Table **** */

CREATE TABLE log
(
    log_id        NUMBER(19) NOT NULL CONSTRAINT log_pk PRIMARY KEY,
    user_login    VARCHAR2(255) NOT NULL,
    entry_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    action        VARCHAR2(10) NOT NULL,
    entity        VARCHAR2(100) NOT NULL,
    record_id     NUMBER(10), 
    record_data   VARCHAR2(1000)
);

CREATE SEQUENCE log_seq START WITH 1;

CREATE OR REPLACE TRIGGER log_bir
BEFORE INSERT ON log
FOR EACH ROW

BEGIN
    SELECT log_seq.NEXTVAL
    INTO   :new.log_id
    FROM   dual;
END;
/

/* **** Organization Table **** */

CREATE TABLE organization
(
    organization_id     NUMBER(10) NOT NULL,
    name                VARCHAR2(255) NOT NULL,
    is_trucking_company VARCHAR2(1) DEFAULT 'N' NOT NULL,
    CONSTRAINT organization_pk PRIMARY KEY (organization_id),
    CONSTRAINT organization_itc_ck CHECK (is_trucking_company IN ('Y', 'N'))
);

CREATE OR REPLACE TRIGGER organization_bir
BEFORE INSERT ON organization
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.organization_id
    FROM   dual;
END;
/


/* **** Equipment Type Table **** */

CREATE TABLE equipment_type
(
    equipment_type_id    NUMBER(10) NOT NULL,
    equipment_type       VARCHAR2(255) NOT NULL,
    CONSTRAINT equipment_type_pk PRIMARY KEY (equipment_type_id)
);

CREATE OR REPLACE TRIGGER equipment_type_bir
BEFORE INSERT ON equipment_type
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.equipment_type_id
    FROM   dual;
END;
/



/* **** Training Type Table **** */

CREATE TABLE training_type
(
    training_type_id    NUMBER(10) NOT NULL,
    training_type       VARCHAR2(255) NOT NULL,
    CONSTRAINT training_type_pk PRIMARY KEY (training_type_id)
);

CREATE OR REPLACE TRIGGER training_type_bir
BEFORE INSERT ON training_type
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.training_type_id
    FROM   dual;
END;
/


/* **** Equipment Component Type Table **** */

CREATE TABLE equipment_component_type
(
    equipment_component_type_id    NUMBER(10) NOT NULL,
    equipment_component_type       VARCHAR2(255) NOT NULL,
    CONSTRAINT equipment_component_type_pk PRIMARY KEY (equipment_component_type_id)
);

CREATE OR REPLACE TRIGGER equipment_component_type_bir
BEFORE INSERT ON equipment_component_type
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.equipment_component_type_id
    FROM   dual;
END;
/


/* **** Training Table **** */

CREATE TABLE training
(
    training_id        NUMBER(10) NOT NULL,
    training           VARCHAR2(255) NOT NULL,
    course_id          VARCHAR2(255) NOT NULL,
    training_type_id   NUMBER(10) NOT NULL,
    notes              VARCHAR(1000),
    CONSTRAINT training_pk PRIMARY KEY (training_id),
    CONSTRAINT train2traintype_fk FOREIGN KEY (training_type_id)
        REFERENCES training_type(training_type_id)
);

CREATE OR REPLACE TRIGGER training_bir
BEFORE INSERT ON training
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.training_id
    FROM   dual;
END;
/


/* **** Participant Table **** */

CREATE TABLE participant
(
    participant_id     NUMBER(10) NOT NULL,
    first_name         VARCHAR2(255),
    last_name          VARCHAR2(255),
    email              VARCHAR2(255),
    organization_id    NUMBER(10),
    start_date         DATE,
    end_date           DATE,
    CONSTRAINT participant_pk PRIMARY KEY (participant_id),
    CONSTRAINT part2org_fk FOREIGN KEY (organization_id)
        REFERENCES organization(organization_id)
);

CREATE SEQUENCE participant_seq START WITH 1;

CREATE OR REPLACE TRIGGER participant_bir
BEFORE INSERT ON participant
FOR EACH ROW

BEGIN
    SELECT participant_seq.NEXTVAL
    INTO   :new.participant_id
    FROM   dual;
END;
/


/* **** Participant Training Table **** */

CREATE TABLE participant_training
(
    participant_training_id   NUMBER(10) NOT NULL,
    participant_id            NUMBER(10) NOT NULL,
    training_id               NUMBER(10) NOT NULL, 
    time_to_complete          NUMBER(10),
    date_completed            DATE,
    CONSTRAINT participant_training_pk PRIMARY KEY (participant_training_id),
    CONSTRAINT parttrain2train_fk FOREIGN KEY (training_id)
        REFERENCES training(training_id),
    CONSTRAINT parttrain2part_fk FOREIGN KEY (participant_id)
        REFERENCES participant(participant_id)
        ON DELETE CASCADE
);

CREATE SEQUENCE participant_training_seq START WITH 1;

CREATE OR REPLACE TRIGGER participant_training_bir
BEFORE INSERT ON participant_training
FOR EACH ROW

BEGIN
    SELECT participant_training_seq.NEXTVAL
    INTO   :new.participant_training_id
    FROM   dual;
END;
/



/* **** Vehicle Class Table **** */

CREATE TABLE vehicle_class
(
    vehicle_class_id     NUMBER(10) NOT NULL,
    vehicle_class_number NUMBER(10) NOT NULL,
    vehicle_class        VARCHAR2(255) NOT NULL,
    description          VARCHAR2(1000),
    CONSTRAINT vehicle_class_pk PRIMARY KEY (vehicle_class_id)
);

CREATE OR REPLACE TRIGGER vehicle_class_bir
BEFORE INSERT ON vehicle_class
FOR EACH ROW

BEGIN
    SELECT type_seq.NEXTVAL
    INTO   :new.vehicle_class_id
    FROM   dual;
END;
/


/* **** Vehicle Table **** */
CREATE TABLE vehicle
(
    vehicle_id          NUMBER(10) NOT NULL,
    id                  VARCHAR2(255),
    vehicle_class_id    NUMBER(10) NOT NULL,
    organization_id     NUMBER(10) NOT NULL,
    vin                 VARCHAR2(255),
    make                VARCHAR2(255),
    model               VARCHAR2(255),
    notes               VARCHAR2(1000),  
    CONSTRAINT vehicle_pk PRIMARY KEY (vehicle_id),
    CONSTRAINT veh2vehclass_fk FOREIGN KEY (vehicle_class_id)
        REFERENCES vehicle_class(vehicle_class_id),
    CONSTRAINT veh2org_fk FOREIGN KEY (organization_id)
        REFERENCES organization(organization_id)
);

CREATE SEQUENCE vehicle_seq START WITH 1;

CREATE OR REPLACE TRIGGER vehicle_bir
BEFORE INSERT ON vehicle
FOR EACH ROW

BEGIN
    SELECT vehicle_seq.NEXTVAL
    INTO   :new.vehicle_id
    FROM   dual;
END;
/


/* **** Equipment Table **** */

CREATE TABLE equipment
(
    equipment_id             NUMBER(10) NOT NULL,
    description              VARCHAR2(255),
    asset_id                 VARCHAR2(255),
    equipment_type_id        NUMBER(10) NOT NULL, 
    serial_number            VARCHAR2(255),
    model_number             VARCHAR2(255), 
    wan                      VARCHAR2(255),   
    latitude                 NUMBER(10,6),
    longitude                NUMBER(10,6),
    elevation                NUMBER(10,2),
    height                   NUMBER(10,2),
    date_installed           DATE,
    notes                    VARCHAR2(1000),
    CONSTRAINT equipment_pk PRIMARY KEY (equipment_id),
    CONSTRAINT eq2eqtype_fk FOREIGN KEY (equipment_type_id)
        REFERENCES equipment_type(equipment_type_id)
);

CREATE SEQUENCE equipment_seq START WITH 1;

CREATE OR REPLACE TRIGGER equipment_bir
BEFORE INSERT ON equipment
FOR EACH ROW

BEGIN
    SELECT equipment_seq.NEXTVAL
    INTO   :new.equipment_id
    FROM   dual;
END;
/


/* **** Equipment Component Table **** */

CREATE TABLE equipment_component
(
    equipment_component_id       NUMBER(19) NOT NULL,
    equipment_component_type_id  NUMBER(10) NOT NULL, 
    equipment_id                 NUMBER(10) NOT NULL, 
    description                  VARCHAR2(255),
    serial_number                VARCHAR2(255),
    model_number                 VARCHAR2(255),
    version                      VARCHAR2(60),
    count                        NUMBER(10),
    CONSTRAINT equipment_component_pk PRIMARY KEY (equipment_component_id),
    CONSTRAINT eqcomp2eq_fk FOREIGN KEY (equipment_id)
        REFERENCES equipment(equipment_id)
        ON DELETE CASCADE,
    CONSTRAINT eqcomp2eqcomptype_fk FOREIGN KEY (equipment_component_type_id)
        REFERENCES equipment_component_type(equipment_component_type_id)
);

CREATE SEQUENCE equipment_component_seq START WITH 1;

CREATE OR REPLACE TRIGGER equipment_component_bir
BEFORE INSERT ON equipment_component
FOR EACH ROW

BEGIN
    SELECT equipment_component_seq.NEXTVAL
    INTO   :new.equipment_component_id
    FROM   dual;
END;
/



/* **** Vehicle Equipment Table **** */

CREATE TABLE vehicle_equipment
(
    vehicle_equipment_id    NUMBER(10) NOT NULL,
    vehicle_id              NUMBER(10) NOT NULL,
    equipment_id            NUMBER(10) NOT NULL,
    CONSTRAINT veheqp_unique UNIQUE (equipment_id),
    CONSTRAINT vehicle_equipment_pk PRIMARY KEY (vehicle_equipment_id),
    CONSTRAINT veheqp2veh_fk FOREIGN KEY (vehicle_id)
        REFERENCES vehicle(vehicle_id)
        ON DELETE CASCADE,
    CONSTRAINT veheqp2eqp_fk FOREIGN KEY (equipment_id)
        REFERENCES equipment(equipment_id)
        ON DELETE CASCADE
);

CREATE SEQUENCE vehicle_equipment_seq START WITH 1;

CREATE OR REPLACE TRIGGER vehicle_equipment_bir
BEFORE INSERT ON vehicle_equipment
FOR EACH ROW

BEGIN
    SELECT vehicle_equipment_seq.NEXTVAL
    INTO   :new.vehicle_equipment_id
    FROM   dual;
END;
/


/* **** Participant Vehicle Table **** */

CREATE TABLE participant_vehicle
(
    participant_vehicle_id  NUMBER(10) NOT NULL,
    participant_id          NUMBER(10) NOT NULL,
    vehicle_id              NUMBER(10) NOT NULL,
    is_primary              VARCHAR2(1) DEFAULT 'N' NOT NULL,
    CONSTRAINT participant_vehicle_pk PRIMARY KEY (participant_vehicle_id),
    CONSTRAINT participant_vehicle_ip_ck CHECK (is_primary IN ('Y', 'N')),
    CONSTRAINT partveh2part_fk FOREIGN KEY (participant_id)
        REFERENCES participant(participant_id)
        ON DELETE CASCADE,
    CONSTRAINT partveh2veh_FK FOREIGN KEY (vehicle_id)
        REFERENCES vehicle(vehicle_id)
        ON DELETE CASCADE
);

CREATE SEQUENCE participant_vehicle_seq START WITH 1;

CREATE OR REPLACE TRIGGER participant_vehicle_bir
BEFORE INSERT ON participant_vehicle
FOR EACH ROW

BEGIN
    SELECT participant_vehicle_seq.NEXTVAL
    INTO   :new.participant_vehicle_id
    FROM   dual;
END;
/
     