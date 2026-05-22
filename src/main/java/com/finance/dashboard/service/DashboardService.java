package com.finance.dashboard.service;

import com.finance.dashboard.entity.Transaction;
import com.finance.dashboard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Service
public class DashboardService {

    private final TransactionRepository transactionRepository;

    public DashboardService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public BigDecimal getTotalRevenue() { return transactionRepository.sumByType(Transaction.Type.INCOME); }
    public BigDecimal getTotalExpense() { return transactionRepository.sumByType(Transaction.Type.EXPENSE); }

    public BigDecimal getMonthlyProfit() {
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end   = LocalDate.now();
        BigDecimal income  = transactionRepository.sumByTypeAndDateBetween(Transaction.Type.INCOME,  start, end);
        BigDecimal expense = transactionRepository.sumByTypeAndDateBetween(Transaction.Type.EXPENSE, start, end);
        return income.subtract(expense);
    }

    public long getTotalTransactions() { return transactionRepository.count(); }
    public List<Transaction> getRecentTransactions() { return transactionRepository.findTop10ByOrderByDateDesc(); }

    public Map<String, List<BigDecimal>> getMonthlyChartData(int year) {
        List<BigDecimal> income  = new ArrayList<>(Collections.nCopies(12, BigDecimal.ZERO));
        List<BigDecimal> expense = new ArrayList<>(Collections.nCopies(12, BigDecimal.ZERO));

        for (Object[] row : transactionRepository.monthlyTotalByType(Transaction.Type.INCOME,  year))
            income.set(((Number) row[0]).intValue() - 1, (BigDecimal) row[1]);
        for (Object[] row : transactionRepository.monthlyTotalByType(Transaction.Type.EXPENSE, year))
            expense.set(((Number) row[0]).intValue() - 1, (BigDecimal) row[1]);

        Map<String, List<BigDecimal>> result = new LinkedHashMap<>();
        result.put("income",  income);
        result.put("expense", expense);
        return result;
    }
}
