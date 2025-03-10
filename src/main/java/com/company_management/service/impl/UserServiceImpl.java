package com.company_management.service.impl;

import com.company_management.common.DataUtil;
import com.company_management.common.DateUtil;
import com.company_management.dto.response.*;
import com.company_management.entity.*;
import com.company_management.exception.AppException;
import com.company_management.exception.BadRequestException;
import com.company_management.dto.request.AccountSearchRequest;
import com.company_management.dto.request.UserCustomEmployeeRequest;
import com.company_management.dto.request.UserDetailRequest;
import com.company_management.dto.request.UserSearchRequest;
import com.company_management.repository.*;
import com.company_management.service.UserService;
import com.company_management.utils.DataUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final SocialInsuranceRepository socialInsuranceRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final WageRepository wageRepository;
    private final UserAccountRepository userAccountRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public PageResponse<UserSearchResponse> searchUser(UserSearchRequest request, Pageable pageable) {
//        return userCustomRepository.searchAllUser(request, pageable);
        return null;
    }

    @Override
    public UserSearchResponse findUserDetailById(Long id) {
        UserAccount userCustom = userAccountRepository.findById(id).orElseThrow(() -> new BadRequestException(
                "Không tìm thấy User theo id" + id));
        Employee employee =
                employeeRepository.findById(userCustom.getEmployee().getId()).orElseThrow(() -> new AppException(
                        "API-500", "Có lỗi xảy ra"));
        UserSearchResponse result = new UserSearchResponse();
        result.setId(userCustom.getId());
        result.setFullName(userCustom.getUsername());
        result.setEmail(userCustom.getEmail());
        return result;
    }

    @Override
    public BasicResponse createUserDetail(UserDetailRequest request) {
        UserAccount user =
                userAccountRepository.findById(request.getUserId()).orElseThrow(() -> new BadRequestException("Có lỗi " +
                        "xảy ra: Không tìm thấy User theo id: " + request.getUserId()));
        Employee employee =
                employeeRepository.findById(user.getEmployee().getId()).orElseThrow(() -> new AppException("API-500",
                        "Có lỗi xảy ra"));
        Department department =
                departmentRepository.findById(request.getDepartmentId()).orElseThrow(() -> new BadRequestException(
                        "Có lỗi xảy ra: Phòng ban không tồn tại!"));
        Position position =
                positionRepository.findById(request.getPositionId()).orElseThrow(() -> new BadRequestException(
                        "Có lỗi xảy ra: Chức danh không tồn tại!"));
//        employee.setGender(request.getGender());
//        employee.setBirthday(DateUtil.string2Date(request.getBirthday()));
//        employee.setAddress(request.getAddress());
//
//        Contract contract = contractMapper.toEntity(request.getContract());
//        contractRepository.save(contract);
//        SocialInsurance socialInsurance = socialInsuranceMapper.toEntity(request.getSocialInsurance());
//        socialInsurance.setUserDetailId(userDetail.getId());
//        socialInsuranceRepository.save(socialInsurance);
//        Wage wage = wageMapper.toEntity(request.getWage());
//        wageRepository.save(wage);
//
//        userDetailRepository.save(userDetail);
        return new BasicResponse(200, "Thành công");
    }

    @Override
    public AccountDetailResponse findAccountDetail(Long id) {
        if (DataUtil.isNullOrZero(id)) {
            throw new BadRequestException("Dữ liệu không hợp lệ");
        }
//        UserCustom user = userCustomRepository.findById(id).orElseThrow(() -> new BadRequestException("Có lỗi " +
//                "xảy ra: Không tìm thấy User theo id: " + id));
        return null;
    }

    @Override
    public PageResponse<AccountSearchResponse> searchAccount( Pageable pageable) {
//        return userCustomRepository.searchAccount(pageable);
        return null;
    }

    @Override
    public ResponseAccountRole findAccountRole(Long id) {
        UserAccount userAccount = userAccountRepository.findByEmployeeId(id).orElseThrow(() -> new BadRequestException(""));
        ResponseAccountRole responseAccountRole = new ResponseAccountRole();
        responseAccountRole.setId(userAccount.getId());
        responseAccountRole.setRoles(userAccount.getRole().getCode());
        responseAccountRole.setFullName(userAccount.getUsername());
        return responseAccountRole;

    }

    @Override
    @Transactional
    public void editUserCustom(UserCustomEmployeeRequest userCustomEmployeeRequest) {
//        UserCustom userCustom = userCustomRepository.findById(userCustomEmployeeRequest.getId()).orElseThrow(
//                () -> new AppException("ERR01", "Tài khoản không tồn tại!")
//        );
//        if (!DataUtils.isNullOrEmpty(userCustomEmployeeRequest.getUserDetailId())) {
//            userCustom.setUserDetailId(userCustomEmployeeRequest.getUserDetailId());
//        }
//        userCustomRepository.save(userCustom);
    }

}
