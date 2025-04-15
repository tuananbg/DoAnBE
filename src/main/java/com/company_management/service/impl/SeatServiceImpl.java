package com.company_management.service.impl;

import com.company_management.utils.mapper.MapperUtils;
import com.company_management.dto.request.RequestSeatDTO;
import com.company_management.dto.response.ResponseSeatDTO;
import com.company_management.entity.Position;
import com.company_management.entity.Seat;
import com.company_management.exception.AppException;
import com.company_management.repository.PositionRepository;
import com.company_management.repository.SeatRepository;
import com.company_management.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SeatServiceImpl implements SeatService {
    private final SeatRepository seatRepository;
    private final PositionRepository positionRepository;
    @Override
    public List<ResponseSeatDTO> findAll() {

        List<Seat> seats = seatRepository.findAll();
        List<ResponseSeatDTO> responseSeatDTOS = new ArrayList<>();
        seats.forEach(seat -> {
            ResponseSeatDTO dto = new ResponseSeatDTO();
            MapperUtils.map(seat, dto);
            Position position = seat.getPosition();
            if (position != null) {
                dto.setPositionName(position.getPositionName());
                if (position.getDepartment() != null) {
                    dto.setDepartmentName(position.getDepartment().getDepartmentName());
                }
            }
            responseSeatDTOS.add(dto);
        });
        return responseSeatDTOS;
    }

    @Override
    public void create(RequestSeatDTO requestSeatDTO) {
        if (seatRepository.existsByCode(requestSeatDTO.getCode())){
            throw new AppException("ERROR","Mã vị trí đã tồn tại!");
        }
        Seat seat = new Seat();
        seat.setCode(requestSeatDTO.getCode());
        seat.setDescription(requestSeatDTO.getDescription());

        if (requestSeatDTO.getPositionId() != null) {
            positionRepository.findById(requestSeatDTO.getPositionId()).ifPresent(seat::setPosition);
        }

        seatRepository.save(seat);
    }
}
