package com.sviatoslavHavrylo.api.controller;

import com.sviatoslavHavrylo.api.service.XbPlannerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping(value = "/api/planner/v1")
public class XbPlannerController extends BaseController {

    private final XbPlannerService xbPlannerService;

    @Autowired
    public XbPlannerController(XbPlannerService xbPlannerService) {
        this.xbPlannerService = xbPlannerService;
    }

    @GetMapping("/getBookingsVMU")
    public Response getBookingByAssetRegistrationNumber(@ModelAttribute ApiAuthentication authentication,
                                                        @RequestParam("vmuName") String vmuName,
                                                        @RequestParam("startDate") @DateTimeFormat(pattern = DATE_FORMAT) Date startDate,
                                                        @RequestParam("endDate") @DateTimeFormat(pattern = DATE_FORMAT) Date endDate) {
        log.info("getBookingsVMU method called");

        return handleResponse("getBookingsVMU By VMUName between dates",
                () -> xbPlannerService.getBookingsVMU(authentication.getEnterpriseId(), authentication.getUserId(),
                        vmuName, startDate, endDate));
    }
}
