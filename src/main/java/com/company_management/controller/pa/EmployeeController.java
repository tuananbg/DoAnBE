package com.company_management.controller.pa;

import com.company_management.common.AppConstants;
import com.company_management.common.ErrorCode;
import com.company_management.common.ResultResp;
import com.company_management.common.enums.EmploymentStatus;
import com.company_management.common.enums.ReportType;
import com.company_management.dto.UserDetailDTO;
import com.company_management.dto.common.BaseResponse;
import com.company_management.dto.common.RequestPage;
import com.company_management.dto.common.ResponsePage;
import com.company_management.dto.request.pa.SearchEmployeeRequest;
import com.company_management.dto.request.pa.employee.RequestEmployeeDetailDTO;
import com.company_management.dto.response.TotalEmployeeDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeDetailDTO;
import com.company_management.dto.response.pa.employee.ResponseEmployeeSelectDTO;
import com.company_management.dto.response.pa.employee.ResponseListEmployeeDTO;
import com.company_management.service.EmployeeService;
import com.company_management.service.common.JasperReportService;
import com.company_management.utils.CommonUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix}/employee")
public class EmployeeController {

    private final EmployeeService employeeService;

    private final ITemplateEngine templateEngine;
    private final JasperReportService jasperReportService;

    @Value("${upload.path}")
    private String fileUpload;

    @PostMapping("/create")
    public BaseResponse<Object> addEmployee(@ModelAttribute("avatarFile") MultipartFile avatarFile,
                                            @ModelAttribute @Valid RequestEmployeeDetailDTO request) throws IOException {
        employeeService.createEmployee(avatarFile, request);
        return BaseResponse.ok(AppConstants.CREATE_SUCCESS_CODE_201, AppConstants.CREATE_SUCCESS_MESS_201);
    }

    @GetMapping("/list/{status}")
    public BaseResponse<ResponsePage<ResponseListEmployeeDTO>> findAllByKeywordAndStatus(@RequestParam(name = "keyword", required = false) String keyword,
                                                                                         @PathVariable("status") EmploymentStatus status,
                                                                                         @ModelAttribute @Valid RequestPage page) {
        return BaseResponse.ok(employeeService.findAllByKeywordAndStatus(keyword,status, page));
    }

//    @GetMapping("/detail-id/{id}")
//    public BaseResponse<ResponseEmployeeDetailDTO> getByIdEmployee(@PathVariable("id") Long id) {
//        return BaseResponse.ok(employeeService.detailEmployee(id));
//    }
    @GetMapping("/detail/{code}")
    public BaseResponse<ResponseEmployeeDetailDTO> getDetailByCode(@PathVariable("code") String code) {
        return BaseResponse.ok(employeeService.detailEmployeeCode(code));
    }

    @GetMapping("/avatar/{urlAvatar}")
    public ResponseEntity<byte[]> getImageByUrl(@PathVariable("urlAvatar") String urlAvatar) throws IOException {
        if (urlAvatar != null) {
            Path imagePath = Paths.get(fileUpload + urlAvatar);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imageBytes);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    public BaseResponse<Object> updateEmployee(@ModelAttribute("avatarFile") MultipartFile avatarFile,
                                             @ModelAttribute RequestEmployeeDetailDTO request) throws IOException {
        employeeService.updateEmployee(avatarFile, request);
        return BaseResponse.ok(AppConstants.UPDATE_SUCCESS_CODE_202,AppConstants.UPDATE_SUCCESS_MESS_202);
    }

    @GetMapping("/select")
    public BaseResponse<List<ResponseEmployeeSelectDTO>> selectEmployee() {
        return BaseResponse.ok(employeeService.selectEmployee());

    }


    @GetMapping(value = "/download-xlsx")
    public ResponseEntity<Resource> exportExcel() {
        byte[] bytes = jasperReportService.employeeFullInformation();
        String fileName = "[DTDI] HRM_Danh sach CBNV_ " + CommonUtils.getCurrentDate("ddMMyyyy") + "." + ReportType.XLSX.getCode();
        return jasperReportService.baseDownload(bytes,fileName);
    }

    @PostMapping("/export-pdf/{id}")
    public ResponseEntity<byte[]> exportPdf(@PathVariable("id") Long id) {
        // Tạo một tài liệu HTML
        String htmlContent = parseThymeleafTemplate(id);

        // Tạo một đối tượng ITextRenderer
        ITextRenderer renderer = new ITextRenderer();

        // Render tài liệu HTML thành PDF
        renderer.setDocumentFromString(htmlContent);
        renderer.layout();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        renderer.createPDF(baos);

        // Lấy byte array của tài liệu PDF
        byte[] pdfBytes = baos.toByteArray();

        // Thiết lập header và trả về tài liệu PDF
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ho_so_nhan_vien.pdf");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }

    private String parseThymeleafTemplate(Long userDetailId) {
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setSuffix(".html");
//        templateResolver.setTemplateMode(TemplateMode.HTML);

//        TemplateEngine templateEngine = new TemplateEngine();
//        templateEngine.setTemplateResolver(templateResolver);

//        Map<String, Object> params = LogisticsMailUtils.sendExportPdfEmployee(employeeService.exportPdf(userDetailId));
//
//        Context context = new Context();
//        context.setVariables(params);
//        return templateEngine.process("export_pdf_employee.html", context);
//        return templateEngine.process("export_pdf_employee", context);
        return null;
    }

    @GetMapping("total/{code}")
    public BaseResponse<TotalEmployeeDTO> totalEmployee(@PathVariable("code") String code) {
        return BaseResponse.ok(employeeService.totalEmployee(code));
    }

}
