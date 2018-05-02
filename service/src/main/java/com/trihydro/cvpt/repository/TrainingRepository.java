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

import com.trihydro.cvpt.model.Training;

public interface TrainingRepository extends JpaRepository<Training, Integer> 
{
	Optional<Training> findByTrainingId(Integer trainingId);


    @Query(value = "SELECT * FROM training t WHERE t.training_id NOT IN (SELECT training_id FROM participant_training WHERE participant_id = ?1)",
            nativeQuery = true)
    List<Training> getAvailableTrainingsForParticipant(Integer participantId);


    @Query(value = "SELECT COUNT(t.training_type_id) FROM training t JOIN participant_training pt ON t.training_id = pt.training_id WHERE t.training_type_id = ?1 AND pt.participant_training_id IN (SELECT participant_training_id FROM participant_training WHERE date_completed IS NOT NULL)",
            nativeQuery = true)
    Optional<BigDecimal> getCountOfCompletedTrainingType(Integer trainingTypeId);
}