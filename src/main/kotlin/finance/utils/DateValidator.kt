package finance.utils

import org.apache.logging.log4j.LogManager
import java.sql.Date
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class DateValidator : ConstraintValidator<ValidDate, Date> {
    override fun initialize(constraintAnnotation: ValidDate) {
    }

    override fun isValid(value: Date, context: ConstraintValidatorContext): Boolean {
        logger.info("dateToBeEvaluated: $value")
        return value > Date.valueOf("2000-01-01")
    }

    companion object {
        private val logger = LogManager.getLogger()
    }
}