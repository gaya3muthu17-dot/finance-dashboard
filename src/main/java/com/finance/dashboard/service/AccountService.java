package com.finance.dashboard.service;

import com.finance.dashboard.entity.Account;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.AccountRepository;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    public AccountService(AccountRepository accountRepository) { this.accountRepository = accountRepository; }
    public List<Account> getByUser(User user) { return accountRepository.findByUser(user); }
    public List<Account> getByUserAndType(User user, Account.AccountType type) { return accountRepository.findByUserAndAccountType(user, type); }
    public Account save(Account account) { return accountRepository.save(account); }
    public void delete(Long id) { accountRepository.deleteById(id); }
    public BigDecimal getTotalBalance(User user) { return accountRepository.sumBalanceByUser(user); }
}
