package com.finance.dashboard.repository;

import com.finance.dashboard.entity.Account;
import com.finance.dashboard.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    List<Account> findByUser(User user);
    List<Account> findByUserAndAccountType(User user, Account.AccountType type);

    @Query("SELECT COALESCE(SUM(a.balance),0) FROM Account a WHERE a.user = :user")
    BigDecimal sumBalanceByUser(User user);
}
