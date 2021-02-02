package com.sviatoslavHavrylo.api.repository;

import com.sviatoslavHavrylo.api.domain.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingRepository extends CrudRepository<Booking, Integer> {

    List<Booking> getAllByAssetRegNo(String registrationNumber);

    @Query(value = "select * from booking where bayId in :baysIds and start_date >= :startDate  and end_date <= :endDate",
            nativeQuery = true)
    List<Booking> getByBayIdsAndBetweenStartEndDates(
            @Param("baysIds") List<Integer> baysIds,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);
}
