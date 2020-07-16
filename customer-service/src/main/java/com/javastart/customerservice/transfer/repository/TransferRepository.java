package com.javastart.customerservice.transfer.repository;

import com.javastart.customerservice.transfer.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    Optional<Transfer> getById(Long transferId);

    @Query(nativeQuery = true, value = "SELECT * FROM Transfer WHERE (bill_from=:billId) OR (bill_to=:billId)")
    Optional<List<Transfer>> getAllByBillId(@Param("billId") Long billIdTo);

    @Query(nativeQuery = true, value = "SELECT * FROM Transfer WHERE (email_from=:email) OR (email_to=:email)")
    Optional<List<Transfer>> getAllByEmail(@Param("email") String email);
}
