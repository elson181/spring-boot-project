package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Long> {
}
