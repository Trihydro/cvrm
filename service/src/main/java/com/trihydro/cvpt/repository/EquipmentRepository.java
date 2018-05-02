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

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.trihydro.cvpt.model.Equipment;

public interface EquipmentRepository extends JpaRepository<Equipment, Integer> 
{
	Optional<Equipment> findByEquipmentId(Integer equipmentId);


    @Query(value = "SELECT COUNT(*) FROM equipment WHERE equipment_type_id = ?1 GROUP BY equipment_type_id",
            nativeQuery = true)
    Optional<BigDecimal> getCountOfEquipmentType(Integer equipmentTypeId);


    @Query(value = "SELECT COUNT(*) FROM equipment WHERE equipment_type_id = ?1 AND date_installed IS NOT NULL GROUP BY equipment_type_id",
            nativeQuery = true)
    Optional<BigDecimal> getCountOfInstalledEquipmentType(Integer equipmentTypeId);


    @Query(value = "SELECT * FROM equipment eq WHERE eq.equipment_id NOT IN (SELECT equipment_id FROM vehicle_equipment) AND eq.equipment_type_id NOT IN (SELECT equipment_type_id FROM equipment_type WHERE equipment_type = 'RSU')",
            nativeQuery = true)
    List<Equipment> getAvailableVehicleEquipment();
}