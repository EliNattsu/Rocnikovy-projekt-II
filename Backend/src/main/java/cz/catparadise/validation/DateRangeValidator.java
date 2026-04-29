package cz.catparadise.validation;

import cz.catparadise.model.Reservation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidDateRange, Reservation> {

    @Override
    public boolean isValid(Reservation reservation, ConstraintValidatorContext context) {
        if (reservation.getStartDate() == null || reservation.getEndDate() == null) {
            return true;
        }
        return reservation.getEndDate().isAfter(reservation.getStartDate());
    }
}