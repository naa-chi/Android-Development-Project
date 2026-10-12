package com.juntas.expenses.utility.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.juntas.expenses.utility.model.UtilityExpenseModel;

public interface UtilityExpenseRepository extends JpaRepository<UtilityExpenseModel, Long> {

}
