package com.cabioglu.tefas.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cabioglu.tefas.dto.FundDataHistoryDTO;
import com.cabioglu.tefas.entity.Fund;
import com.cabioglu.tefas.entity.FundDataHistory;

public interface FundDataHistroyRepository extends JpaRepository<FundDataHistory, Long> {
	
	 @Query("SELECT new com.cabioglu.tefas.dto.FundDataHistoryDTO(fh.id, f.code, f.fundName, fh.date, fh.unitPrice,fh.totalUnits,fh.quantity,fh.totalValue) " +
	           "FROM Fund f JOIN f.dataHistory fh")
	    List<FundDataHistoryDTO> getAllFundHistoryDetails();
	 
	 @Query("SELECT COUNT(fh) > 0 " +
		       "FROM Fund f JOIN f.dataHistory fh WHERE f.code = :code AND fh.date = :date")
	 boolean existsFundHistoryDetail(@Param("code") String code, @Param("date") Date date);
	 
	 @Query("SELECT fh " +
	           "FROM FundDataHistory fh where fh.fund.id = :id and (fh.date = :date or fh.date = :oneDayDate or fh.date = :weekDate or fh.date = :monthDate or fh.date = :threeMonthDate or fh.date = :sixMonthDate or fh.date = :oneYearDate or fh.date = :threeYearDate or fh.date = :fiveYearDate)")
	    List<FundDataHistory> getFundHistoryPerformanceDate(@Param("id") Long id,@Param("date") Date date,@Param("oneDayDate") Date oneDayDate,@Param("weekDate") Date weekDate,@Param("monthDate") Date monthDate,@Param("threeMonthDate") Date threeMonthDate,@Param("sixMonthDate") Date sixMonthDate,@Param("oneYearDate") Date oneYearDate,@Param("threeYearDate") Date threeYearDate,@Param("fiveYearDate") Date fiveYearDate);

	 @Query("""
		       SELECT f 
		       FROM Fund f 
		       JOIN FETCH f.dataHistory fh 
		       WHERE fh.date IN (:dates)
		       """)
		List<Fund> findAllWithDataHistory(@Param("dates") List<Date> dates);

		@Query("SELECT MAX(fh.date) FROM FundDataHistory fh")
		Date getMaxDate();
}
