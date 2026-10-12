package com.juntas.expenses.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juntas.expenses.event.model.EventExpenseModel;

public interface EventExpenseRepository extends JpaRepository<EventExpenseModel, Long> {

}
