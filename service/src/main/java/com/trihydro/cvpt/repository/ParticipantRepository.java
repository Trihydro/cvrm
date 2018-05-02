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
import java.util.Optional;

import com.trihydro.cvpt.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant, Integer> 
{
	Optional<Participant> findByParticipantId(Integer id);

    @Query(value = "SELECT COUNT(*) FROM participant",
            nativeQuery = true)
    Optional<BigDecimal> getCountOfParticipant();


    @Query(value = "SELECT COUNT(DISTINCT pt.participant_id) FROM participant_training pt WHERE pt.participant_id NOT IN (SELECT participant_id FROM participant_training WHERE date_completed IS NULL)",
            nativeQuery = true)
    Optional<BigDecimal> getCountOfTrainingCompleteParticipant();


    @Query(value = "SELECT AVG(time_to_complete) FROM participant_training",
            nativeQuery = true)
    Optional<BigDecimal> getAverageParticipantTrainingTime();
}