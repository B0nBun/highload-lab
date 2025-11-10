package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java.dto.meetingroombooking;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.Instant;

@ValidatorAnnotation
public record MeetingRoomBookingCreateRequestDTO(
        @NotNull Long userId,
        @NotNull Long meetingRoomId,
        @NotNull Instant startTime,
        @NotNull Instant endTime) {}

@Constraint(validatedBy = Validator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@interface ValidatorAnnotation {
    String message() default "booking start time must be before end time";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class Validator
        implements ConstraintValidator<ValidatorAnnotation, MeetingRoomBookingCreateRequestDTO> {
    public void initialize(ValidatorAnnotation annotation) {}

    @Override
    public boolean isValid(
            MeetingRoomBookingCreateRequestDTO value, ConstraintValidatorContext context) {
        Instant s = value.startTime();
        Instant e = value.endTime();
        return s.isBefore(e);
    }
}
