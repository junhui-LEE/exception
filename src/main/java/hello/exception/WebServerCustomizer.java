package hello.exception;

import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

//@Component
public class WebServerCustomizer implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
//    이렇게 WebServerFactoryCustomizer<ConfigurableWebServerFactory> 제네릭 인터페이스를
//    구현하고 빈으로 등록 함으로서 스프링부트가 서블릿으로 하여금 각각의 상황에 맞는 오류페이지를
//    보여주라고 설정을 할 수 있는데, 이렇게 구현해야 하는 제네릭 인터페이스는 의미가 있다기 보다는
//    서블릿 컨테이너가 이 제네릭 클래스를 쓰면 알아먹도록 지원을 해 주는 것이다. 이름 그대로
//    "웹서버(우리는 Apache Tomcat)를 커스텀마이징 하는 것이다."
    @Override
    public void customize(ConfigurableWebServerFactory factory) {

        ErrorPage errorPage404 = new ErrorPage(HttpStatus.NOT_FOUND, "/error-page/404");
//        404에러가 발생하면 /error-page/404 로 이동하라(정확히는 이 컨트롤러를 호출해라)는 의미를
//        ErrorPage객체에 담아서 errorPage404포인터에 넣은 것이다. HttpStatus.NOT_FOUND 를 들어가 보면
//        404를 확인할 수 있다.
        ErrorPage errorPage500 = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error-page/500");
        ErrorPage errorPageEx = new ErrorPage(RuntimeException.class, "/error-page/500");
//        참고로 RuntimeException 뿐만 아니라 그 자식 예외들은 전부 /error-page/500 으로 보내준다.

        factory.addErrorPages(errorPage404, errorPage500, errorPageEx);
//        errorPage404, errorPage500, errorPageEx 를 설정하고 등록을 해줘야 한다.

//        서블릿은
//        response.sendError(404)를 확인하면 errorPage404를 호출한다.
//        response.sendError(500)을 확인하면 errorPage500을 호출한다.
//        RuntimeException 또는 그 자식 타입의 예외가 발생하면 errorPageEx를 호출한다.

//        500 예외가 서버 내부에서 발생한 오류라는 뜻을 포함하고 있기 때문에 여기서는 예외가 발생한 경우도
//        500 오류 화면으로 처리했다.
    }

}