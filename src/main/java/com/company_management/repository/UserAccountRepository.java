package com.company_management.repository;

import com.company_management.entity.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByEmail(String email);

    Optional<UserAccount> findByEmployeeId(Long userDetailId);

    Optional<UserAccount> findByActiveCode(String activeCode);
    Optional<UserAccount> findByForgotPassword(String forgotPassword);
    @Query(value = """
            SELECT *
            FROM user_account u
            ORDER BY u.created_date DESC
            """,
            nativeQuery = true)
    Page<UserAccount> findAllWithPagination(Pageable pageable);
}
