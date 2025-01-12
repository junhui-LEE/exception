package hello.exception.exhandler.advice;

import hello.exception.api.ApiExceptionV2Controller;
import hello.exception.exception.UserException;
import hello.exception.exhandler.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//  1. @ControllerAdvice는 대상으로 지정한 컨트롤러에 @ExceptionHandler, @InitBinder을 부여해 주는 기능을 한다.
//  2. @ControllerAdvice에 대상을 지정하지 않으면 모든 컨트롤러에 적용된다.( 모든 컨트롤러에 @ExceptionHandler,
//     @InitBinder을 부여해 주는 기능을 한다. )
//  3. @RestControllerAdvice는 @ControllerAdvice안에 있는 메서드의 리턴값에 @ResponseBody를 붙여준 것과 같다.
//  4. @RestControllerAdvice는 @ControllerAdivce와 코드가 같고, @ResponseBody가 추가 되어 있는것 뿐이다.
//     @Controller와 @RestController의 차이와 같다

@Slf4j
@RestControllerAdvice
public class ExControllerAdvice {
//    예외가 발생하는 컨트롤러에서 예외가 발생하면 그 예외를 처리하기 위한 java 코드
//    ,이전시간에는 예외가 발생하는 컨트롤러 안에 컨트롤러의 기능을 하는 정상코드와
//    @ExceptionHandler가 붙어 있는 예외 처리 코드가 함께 있었는데, ExControllerAdvice.java
//    에 @ExceptionHandler가 붙어 있는 예외처리 코드를 묶어서 따로 그 안에 넣을 것이다.
//    예외가 발생하는 컨트롤러에서는 예외처리 코드(@ExceptionHandler)부분을 삭제해도
//    ExceptionHandlerExceptionResolver가 ExControllerAdvice.java에 있는 @ExceptionHandler을
//    보고서 예외를 처리해 준다.
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult illgalExHandler(IllegalArgumentException e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("BAD", e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResult> userExHandler(UserException e){
        log.error("[exceptionHandler] ex", e);
        ErrorResult errorResult = new ErrorResult("USER-EX", e.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exHandle(Exception e){
        log.error("[exceptionHandler] ex", e);
        return new ErrorResult("EX", "내부 오류");
    }
}