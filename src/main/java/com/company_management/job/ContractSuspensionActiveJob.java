package com.company_management.job;

import com.company_management.service.EmployeeContractService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContractSuspensionActiveJob implements Job {
    @Autowired
    private EmployeeContractService employeeContractService;

    @Async
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        try{
            log.info("Start job ContractSuspensionActiveJob");
//            Cập nhật trạng thái hợp đồng sắp hết hạn vào tháng này thành hết hạn
            employeeContractService.updateStatusContractRenawalMonth();
//            Cập nhật trạng thái hợp đồng hết hạn vào tháng sau thành sắp hết hạn
            employeeContractService.updateStatusContractRenewalNextMonth();
        }catch (Exception e){
            log.error(e.getMessage());
        }
    }
}
