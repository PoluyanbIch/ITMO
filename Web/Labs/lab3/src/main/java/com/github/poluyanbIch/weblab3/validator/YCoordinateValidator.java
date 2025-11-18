package com.github.poluyanbIch.weblab3.validator;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import java.math.BigDecimal;

@FacesValidator("yCoordinateValidator")
public class YCoordinateValidator implements Validator<Object> {

    private static final BigDecimal MIN = new BigDecimal("-5");
    private static final BigDecimal MAX = new BigDecimal("3");

    @Override
    public void validate(FacesContext context, UIComponent component, Object value) throws ValidatorException {
        if (value == null) return;

        try {
            BigDecimal y = (value instanceof BigDecimal)
                    ? (BigDecimal) value
                    : new BigDecimal(value.toString());

            if (y.compareTo(MIN) < 0 || y.compareTo(MAX) > 0) {
                throw new ValidatorException(
                        new FacesMessage(FacesMessage.SEVERITY_ERROR,
                                "Y must be between -5 and 3", null)
                );
            }

        } catch (NumberFormatException e) {
            throw new ValidatorException(
                    new FacesMessage(FacesMessage.SEVERITY_ERROR,
                            "Y must be a valid number", null)
            );
        }
    }
}
