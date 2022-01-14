package com.chatappapi.api.repository;

import com.chatcomponents.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long>, QuerydslPredicateExecutor<Contact> {
}
