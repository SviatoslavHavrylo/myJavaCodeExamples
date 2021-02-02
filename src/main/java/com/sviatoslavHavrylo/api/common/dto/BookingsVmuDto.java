package com.sviatoslavHavrylo.api.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

import static com.sviatoslavHavrylo.api.service.util.Constants.DATE_TIME_FORMAT;

@Getter
@Setter
@NoArgsConstructor
public class BookingsVmuDto implements Serializable {

    private static final long serialVersionUID = 3161236086015520879L;

    private Integer bookingId;

    private String vmuName;

    @JsonProperty("bayID")
    private Integer bayId;

    private String bayName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date endDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_TIME_FORMAT)
    private Date date;

    private String regNo;

    private Integer eventTypeId;

    private String eventTypeName;

    @JsonProperty("jobNo")
    private Integer jobNumber;

    @JsonProperty("customerName")
    private String invoiceeName;
}
