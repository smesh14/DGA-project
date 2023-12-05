package com.contactbook.repository;

import com.contactbook.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {



    Page<Contact> findByUserId(Long userId, Pageable pageRequest);
    Contact findByNameAndUserId(String contactName,Long userId);
    boolean existsByPhoneNumberAndUserId(String phoneNumber, Long userId);
    boolean existsByPhoneNumberAndUserIdAndIdIsNot(String phoneNumber, Long userId,Long id);
    boolean existsByNameAndUserId(String name, Long userId);
    boolean existsByNameAndUserIdAndIdIsNot(String name, Long userId, Long id);

}
