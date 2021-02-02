package com.sviatoslavHavrylo.api.domain;

import com.sviatoslavHavrylo.api.common.dto.BookingsVmuDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name = "booking", schema = "planner")
public class Booking implements Serializable {

    private static final long serialVersionUID = -767117455035018655L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "assetId")
    private String assetRegNo;

    @Column(name = "enterpriseName")
    private String enterpriseName;

    @Column(name = "location")
    private String location;

    @Column(name = "start_date")
    private Date startDate;

    @Column(name = "end_date")
    private Date endDate;

    @Column(name = "dueDate")
    private Date dueDate;

    @Column(name = "bayId")
    private Integer bayId;

    @Column(name = "eventTypeId")
    private Integer eventTypeId;

    @Column(name = "eventTypeName")
    private String eventTypeName;

    @Column(name = "jobNumber")
    private Integer jobNumber;

    @Column(name = "invoiceeName")
    private String invoiceeName;

    public BookingsVmuDto createDto() {
        BookingsVmuDto bookingsVmuDto = new BookingsVmuDto();
        bookingsVmuDto.setBookingId(id);
        bookingsVmuDto.setBayId(bayId);
        bookingsVmuDto.setStartDate(startDate);
        bookingsVmuDto.setEndDate(endDate);
        bookingsVmuDto.setDate(dueDate);
        bookingsVmuDto.setRegNo(assetRegNo);
        bookingsVmuDto.setEventTypeId(eventTypeId);
        bookingsVmuDto.setEventTypeName(eventTypeName);
        bookingsVmuDto.setJobNumber(jobNumber);
        bookingsVmuDto.setInvoiceeName(invoiceeName);

        return bookingsVmuDto;
    }
}
