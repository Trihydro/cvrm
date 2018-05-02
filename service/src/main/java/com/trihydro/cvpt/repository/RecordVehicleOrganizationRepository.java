/********************************************************************
 *  Copyright 2016 Trihydro 
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ********************************************************************/

 package com.trihydro.cvpt.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.List;

import com.trihydro.cvpt.model.RecordVehicleOrganization;

public interface RecordVehicleOrganizationRepository extends JpaRepository<RecordVehicleOrganization, String> 
{
    @Query(value = "SELECT CONCAT(CONCAT(org.name, '-'), vc.vehicle_class) AS row_id, org.name AS organization_name, vc.vehicle_class, COUNT(*) AS vehicle_count FROM vehicle v JOIN organization org ON v.organization_id = org.organization_id JOIN vehicle_class vc ON v.vehicle_class_id = vc.vehicle_class_id GROUP BY org.name, vc.vehicle_class",
            nativeQuery = true)
    List<RecordVehicleOrganization> getVehicleOrganizationRecords();
}