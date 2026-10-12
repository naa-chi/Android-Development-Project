package com.juntas.expenses.utility.mapper;

import com.juntas.expenses.utility.dto.CreateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UpdateUtilityExpenseRequest;
import com.juntas.expenses.utility.dto.UtilityExpenseResponse;
import com.juntas.expenses.utility.model.UtilityExpenseModel;
import org.springframework.stereotype.Component;

@Component
public class UtilityExpenseMapper {
    public UtilityExpenseResponse toResponse(UtilityExpenseModel entity) {
        return new UtilityExpenseResponse(
                entity.getId(),
                entity.getEmissionDate(),
                entity.getBill(),
                entity.getUtilType(),
                entity.getForUserId(),
                entity.getForEmail(),
                entity.getNeighborhoodId()
        );
    }

    public UtilityExpenseModel toEntity(CreateUtilityExpenseRequest req) {
        UtilityExpenseModel e = new UtilityExpenseModel();
        e.setEmissionDate(req.emissionDate());
        e.setBill(req.bill());
        e.setUtilType(req.utilType());
        e.setForUserId(req.forUserId());
        e.setForEmail(req.forEmail());
        e.setNeighborhoodId(req.neighborhoodId());
        return e;
    }

    public void applyUpdate(UpdateUtilityExpenseRequest req, UtilityExpenseModel target) {
        if (req.emissionDate() != null) target.setEmissionDate(req.emissionDate());
        if (req.bill() != null) target.setBill(req.bill());
        if (req.utilType() != null) target.setUtilType(req.utilType());
        if (req.forUserId() != null) target.setForUserId(req.forUserId());
        if (req.forEmail() != null) target.setForEmail(req.forEmail());
        if (req.neighborhoodId() != null) target.setNeighborhoodId(req.neighborhoodId());
    }
}