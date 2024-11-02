package hello.exception.servlet;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
public class ErrorPageController {
//    오류가 발생했을 때 처리할 수 있는 컨트롤러가 필요하다. 예를 들어서 RuntimeException 예외가
//    발생하면 errorPageEx 에서 지정한 /error-page/500 이 호출된다. WAS에서 진짜 /error-page/500
//    이라는 것이 다시 쭉~호출이 된다. 필터,서블릿 해서 쭉 호출되서 컨트롤러까지 다시 호출되는 것이다.
//    그래서 오류처리를 할 컨트롤러가 필요하다.

//    post 이던 어떤 것이던 한번에 처리하려고 @GetMapping을 안썼다.
    @RequestMapping("/error-page/404")
    public String errorPage404(HttpServletRequest request, HttpServletResponse response) {
        log.info("erroPage 404");
        return "error-page/404";
    }

    @RequestMapping("/error-page/500")
    public String errorPage500(HttpServletRequest request, HttpServletResponse response){
        log.info("errorPage 500");
        return "error-page/500";
    }
}
