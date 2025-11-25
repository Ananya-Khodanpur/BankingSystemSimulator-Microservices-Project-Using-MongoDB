package com.repository;

import com.document.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction,String > {
    List<Transaction> findBySourceAccountOrDestinationAccount(String src,String dest);
}
