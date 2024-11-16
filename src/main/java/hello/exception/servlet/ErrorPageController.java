package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
public class ErrorPageController {

//    WAS는 Exception이나 response.sendError() 을 확인하면 다시 요청을 하는데 이때
//    request의 attribute에 오류정보를 추가해서 호출한다. request에 담겨 있는것이
//    엄청 많지만 아래의 정도만 알아보자

    // RequestDispatcher 상수로 정의되어 있음
    String ERROR_EXCEPTION = "javax.servlet.error.exception";
    String ERROR_EXCEPTION_TYPE = "javax.servlet.error.exception_type";
    String ERROR_MESSAGE = "javax.servlet.error.message";
    String ERROR_REQUEST_URI = "javax.servlet.error.request_uri";
    String ERROR_SERVLET_NAME = "javax.servlet.error.servlet_name";
    String ERROR_STATUS_CODE = "javax.servlet.error.status_code";

    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 404");
        printErrorInfo(request);
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        printErrorInfo(request);
        return "error-page/500";
    }

    @RequestMapping(value = "/error-page/500", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> errorPage500Api(HttpServletRequest request, HttpServletResponse response){
        log.info("API errorPage 500");

        HashMap<String, Object> result = new HashMap<>();
        Exception ex = (Exception)request.getAttribute(ERROR_EXCEPTION);

        result.put("status", request.getAttribute(ERROR_STATUS_CODE));
        result.put("message", ex.getMessage());

        Integer statusCode = (Integer)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

        return new ResponseEntity(result, HttpStatus.valueOf(statusCode));
    }


    private void printErrorInfo(HttpServletRequest request) {
        log.info("ERROR_EXCEPTION: ex=", request.getAttribute(ERROR_EXCEPTION));
        log.info("ERROR_EXCEPTION_TYPE: {}", request.getAttribute(ERROR_EXCEPTION_TYPE));
        log.info("ERROR_MESSAGE: {}", request.getAttribute(ERROR_MESSAGE));
        // ex의 경우 NestedServletException 스프링이 한번 감싸서 반환
        log.info("ERROR_REQUEST_URI: {}", request.getAttribute(ERROR_REQUEST_URI));
        log.info("ERROR_SERVLET_NAME: {}", request.getAttribute(ERROR_SERVLET_NAME));
        log.info("ERROR_STATUS_CODE: {}", request.getAttribute(ERROR_STATUS_CODE));
        log.info("dispatchType={}", request.getDispatcherType());
    }


    //    ************************************************************************************
////    오류가 발생했을 때 처리할 수 있는 컨트롤러가 필요하다. 예를 들어서 RuntimeException 예외가
////    발생하면 errorPageEx 에서 지정한 /error-page/500 이 호출된다. WAS에서 진짜 /error-page/500
////    이라는 것이 다시 쭉~호출이 된다. 필터,서블릿 해서 쭉 호출되서 컨트롤러까지 다시 호출되는 것이다.
////    그래서 오류처리를 할 컨트롤러가 필요하다.
//
////    post 이던 어떤 것이던 한번에 처리하려고 @GetMapping을 안썼다.
//    @RequestMapping("/error-page/404")
//    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
//        log.info("erroPage 404");
//        return "error-page/404";
//    }
//
//    @RequestMapping("/error-page/500")
//    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
//        log.info("errorPage 500");
//        return "error-page/500";
//    }
//    ************************************************************************************

}
