package com.company_management.controller.HRM;

import com.company_management.common.AppConstants;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.request.RequestSeatDTO;
import com.company_management.dto.response.ResponseSeatDTO;
import com.company_management.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/seat")
public class SeatController {
    private final SeatService seatService;

    @GetMapping("/list")
    public BaseResponse<List<ResponseSeatDTO>> getAllPosition() {
        return BaseResponse.ok(AppConstants.STATUS_200, AppConstants.MESSAGE_200, seatService.findAll());
    }

    @PostMapping("/create")
    public BaseResponse<Object> create(@RequestBody RequestSeatDTO request) {
        seatService.create(request);
        return BaseResponse.ok(AppConstants.STATUS_200, AppConstants.MESSAGE_200);
    }
}
