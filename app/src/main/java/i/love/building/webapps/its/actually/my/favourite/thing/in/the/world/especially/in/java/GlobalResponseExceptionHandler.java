package i.love.building.webapps.its.actually.my.favourite.thing.in.the.world.especially.in.java;

import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalResponseExceptionHandler extends ResponseEntityExceptionHandler {
    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
        MethodArgumentNotValidException ex,
        HttpHeaders headers,
        HttpStatusCode status,
        WebRequest request
    ) {
        BindingResult binding = ex.getBindingResult();
        var fieldErrs = binding.getFieldErrors().stream()
            .map(err -> String.format("'%s': %s", err.getField(), err.getDefaultMessage()));
        var globalErrs = binding.getGlobalErrors().stream()
            .map(err -> String.format("'%s': %s", err.getObjectName(), err.getDefaultMessage()));
        String msg = Stream.concat(fieldErrs, globalErrs).collect(Collectors.joining("; "));
        var pd = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, msg);
        return ResponseEntity.badRequest().body(pd);
    }
}
