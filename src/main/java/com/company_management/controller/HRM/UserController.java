package com.company_management.controller.HRM;

import com.company_management.dto.request.UserDetailRequest;
import com.company_management.dto.response.BasicResponse;
import com.company_management.service.JwtService;
import com.company_management.dto.request.UserSearchRequest;
import com.company_management.dto.response.PageResponse;
import com.company_management.dto.response.UserSearchResponse;
import com.company_management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${apiPrefix}/user")
@RequiredArgsConstructor
public class UserController {
    private final JwtService jwtService;
    private final UserService userService;
    @PostMapping("/search")
    public ResponseEntity<PageResponse<UserSearchResponse>> searchUser(@RequestBody UserSearchRequest request,
                                                                       @PageableDefault Pageable pageable) {
        return new ResponseEntity<>(userService.searchUser(request, pageable), HttpStatus.OK);
    }

    @GetMapping("/detail")
    public ResponseEntity<UserSearchResponse> getUserDetailById(@RequestParam(name = "id") Long id) {
        return new ResponseEntity<>(userService.findUserDetailById(id), HttpStatus.OK);
    }

    @PostMapping("/userdetail")
    public ResponseEntity<BasicResponse> updateUserDetail(@RequestBody UserDetailRequest request) {
        return new ResponseEntity<>(userService.createUserDetail(request), HttpStatus.OK);
    }
}
