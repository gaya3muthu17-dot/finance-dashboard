package com.finance.dashboard.service;

import com.finance.dashboard.entity.Transaction;
import com.finance.dashboard.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions() { return transactionRepository.findAllByOrderByDateDesc(); }
    public Optional<Transaction> getById(Long id) { return transactionRepository.findById(id); }
    public Transaction save(Transaction t) { return transactionRepository.save(t); }
    public void delete(Long id) { transactionRepository.deleteById(id); }

    public List<Transaction> search(String keyword, String type, String category, String startDate, String endDate) {
        Transaction.Type typeEnum = (type != null && !type.isEmpty()) ? Transaction.Type.valueOf(type) : null;
        LocalDate start = (startDate != null && !startDate.isEmpty()) ? LocalDate.parse(startDate) : null;
        LocalDate end   = (endDate   != null && !endDate.isEmpty())   ? LocalDate.parse(endDate)   : null;
        String kw  = (keyword  != null && !keyword.isEmpty())  ? keyword  : null;
        String cat = (category != null && !category.isEmpty()) ? category : null;
        return transactionRepository.searchTransactions(kw, typeEnum, cat, start, end);
    }
}
