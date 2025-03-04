//package com.company_management.service.impl;
//
//import com.company_management.exception.BadRequestException;
//import com.company_management.dto.MenuItemDTO;
//import com.company_management.entity.MenuItem;
//import com.company_management.dto.response.BasicResponse;
//import com.company_management.repository.MenuItemRepository;
//import com.company_management.service.MenuItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@Transactional(rollbackFor = Throwable.class)
//@RequiredArgsConstructor
//public class MenuItemServiceImpl implements MenuItemService {
//    private final MenuItemRepository menuItemRepository;
//
//    @Override
//    public BasicResponse addMenuItem(MenuItemDTO menuItemDTO) {
//        MenuItem menuItem = new MenuItem();
//        menuItem.setMenuItemCode(menuItemDTO.getMenuItemCode());
//        menuItem.setMenuItemName(menuItemDTO.getMenuItemName());
//        menuItem.setIsActive(menuItemDTO.getStatus());
//        menuItemRepository.save(menuItem);
//        return new BasicResponse(200, "Thêm mới thành công");
//    }
//
//    @Override
//    public BasicResponse updateMenuItem(MenuItemDTO menuItemDTO) {
//        MenuItem menuItem = menuItemRepository.findById(menuItemDTO.getId()).orElseThrow(() -> new BadRequestException("Không tìm thấy Menu theo id: " + menuItemDTO.getId()));
//        return new BasicResponse(200, "Cập nhật thành công");
//    }
//
//    @Override
//    public List<MenuItemDTO> getAll() {
//        return menuItemMapper.toDto(menuItemRepository.findAll());
//    }
//}
