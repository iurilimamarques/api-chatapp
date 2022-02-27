package com.chatappapi.api.repository;

import com.chatcomponents.Message;
import com.chatcomponents.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long>, QuerydslPredicateExecutor<Message> {
}
