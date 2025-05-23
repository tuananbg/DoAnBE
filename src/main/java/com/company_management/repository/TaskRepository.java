package com.company_management.repository;

import com.company_management.entity.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t FROM Task t JOIN Project p ON t.project.id = p.id WHERE p.projectCode = :projectCode")
    List<Task> findByProjectCode(@Param("projectCode") String projectCode);

    boolean existsByTaskCode(String taskCode);

    @Query(value = "SELECT t FROM Task t  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(t.taskCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.taskName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.employee.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.employee.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND t.status = :status " +
            "ORDER BY t.priority ASC")
    Page<Task> findByStatus(@Param("status") Integer status,@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT t FROM Task t  WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(t.taskCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.taskName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.employee.code) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.employee.fullName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND t.status = :status AND t.managerCode = :managerCode " +
            "ORDER BY t.priority ASC")
    Page<Task> findByStatusV2(@Param("status") Integer status,
                              @Param("managerCode") String managerCode,
                              @Param("keyword") String keyword,
                              Pageable pageable);

    @Query(value = "SELECT t FROM Task t JOIN t.employee e WHERE " +
            "(:keyword IS NULL OR " +
            "UPPER(t.taskCode) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%') OR " +
            "UPPER(t.taskName) LIKE CONCAT('%', UPPER(COALESCE(:keyword, '')), '%')) " +
            "AND t.status = :status AND e.code = :employeeCode " +
            "ORDER BY t.priority ASC")
    Page<Task> findByStatusAndEmployeeCode(@Param("status") Integer status,@Param("employeeCode") String employeeCode, @Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT " +
            "COUNT(t) AS totalCount, " +
            "COALESCE(SUM(CASE WHEN t.status = :status THEN 1 ELSE 0 END),0) AS doneCount " +
            "FROM Task t " +
            "JOIN t.project p " +
            "WHERE p.projectCode = :projectCode")
    Object[] countTaskAndDoneByProjectCode(
            @Param("projectCode") String projectCode,
            @Param("status") Integer status);

    @Query("SELECT\n" +
            "    COALESCE(SUM(CASE WHEN t.status = 1 THEN 1 ELSE 0 END), 0) AS status1,\n" +
            "  COALESCE(SUM(CASE WHEN t.status = 2 THEN 1 ELSE 0 END), 0) AS status2,\n" +
            "  COALESCE(SUM(CASE WHEN t.status = 3 THEN 1 ELSE 0 END), 0) AS status3 " +
            "FROM Task t\n" +
            "JOIN Project p ON t.project.id = p.id\n" +
            "WHERE p.projectCode = :projectCode")
    Object[] countTaskByProjectCode(@Param("projectCode") String projectCode);


    @Query("SELECT COUNT(t) FROM Task t")
    long countAllTasks();

    Optional<Task> findByTaskCode(String taskCode);

    @Query(value = "SELECT t.TASK_CODE\n" +
            "            FROM task t\n" +
            "            ORDER BY  t.TASK_CODE DESC\n" +
            "            LIMIT 1", nativeQuery = true)
    String taskCodeMax();

    @Query("SELECT COUNT(t) FROM Task t WHERE t.employee.code =:employeeCode AND t.status IN :status")
    Long countByEmployeeCode(@Param("employeeCode") String employeeCode, @Param("status") List<Integer> status);


    List<Task> findAllByManagerCodeAndStatus(String managerCode, Integer status);


    List<Task> findAllByStatus(Integer status);

}
