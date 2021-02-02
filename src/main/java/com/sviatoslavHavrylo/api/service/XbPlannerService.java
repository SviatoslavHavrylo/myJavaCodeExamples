package com.sviatoslavHavrylo.api.service;

import com.sviatoslavHavrylo.api.common.dto.BookingsVmuDto;
import com.sviatoslavHavrylo.api.domain.Booking;
import com.sviatoslavHavrylo.api.exception.ApiLogicFailException;
import com.sviatoslavHavrylo.api.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class XbPlannerService extends BasePlannerService {

    private final BookingRepository bookingDao;

    private final BayDao bayDao;

    @Autowired
    public XbPlannerServiceImpl(BookingRepository bookingDao, BayDao bayDao) {
        this.bookingDao = bookingDao;
        this.bayDao = bayDao;
    }

    @Override
    @Transactional
    public List<BookingsVmuDto> getBookingsVMU(Integer enterpriseId, Integer userId, String vmuName, Date startDate, Date endDate) {
        validateDates(startDate, endDate);
        validateVmuPermission(userId, vmuName);

        List<Bay> bays = bayDao.getByEnterpriseIdAndVmuName(enterpriseId, vmuName);
        if (bays.isEmpty()) {
            throw new ApiLogicFailException("No data");
        }

        List<Integer> baysIds = getBaysIds(bays);
        List<Booking> bookings = bookingDao.getByBayIdsAndBetweenStartEndDates(baysIds, startDate, endDate);

        return createBookingsVmuDtos(bookings, bays);
    }

    private void validateDates(Date startDate, Date endDate) {
        if (startDate.after(endDate)) {
            throw new ApiLogicFailException("startDate is after endDate");
        }
    }

    private void validateVmuPermission(Integer userId, String vmuName) {
        try {
            userVmuPermissionDao.getByUserIdAndVmuName(userId, vmuName);
        } catch (EmptyResultDataAccessException nre) {
            throw new ApiLogicFailException("VMU is not permitted to current user");
        }
    }

    private List<BookingsVmuDto> createBookingsVmuDtos(List<Booking> bookings, List<Bay> bays) {
        return bookings.stream()
                .map(booking -> createBookingsVmuDto(booking, bays))
                .sorted(Comparator.comparing(BookingsVmuDto::getStartDate))
                .collect(Collectors.toList());
    }

    private BookingsVmuDto createBookingsVmuDto(Booking booking, List<Bay> bays) {
        BookingsVmuDto bookingsVmuDto = booking.createDto();

        Integer bayId = booking.getBayId();
        Bay bay = bays.stream()
                .filter(bayItem -> bayItem.getId().equals(bayId))
                .findFirst().orElse(null);
        if (bay != null) {
            bookingsVmuDto.setVmuName(bay.getVmu().getName());
            bookingsVmuDto.setBayName(bay.getName());
        }

        return bookingsVmuDto;
    }

    private List<Integer> getBaysIds(List<Bay> bays) {
        return bays.stream()
                .map(Bay::getId)
                .collect(Collectors.toList());
    }
}
