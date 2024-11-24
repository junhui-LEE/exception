package hello.exception.resolver;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class MyHandlerExceptionResolver implements HandlerExceptionResolver {

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {
        try{
            if(ex instanceof IllegalArgumentException){
                log.info("IllegalArgumentException resolver to 400");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
                return new ModelAndView();
                /*
                    response.sendError()과 ModelAndView()를 반환함으로서 예외를 처리할 수 있다.
                    return new ModelAndView();를 써 줌으로서 새로운 ModelAndView를 반환할 수 있다.
                    생성자에 비어 있는 값을 넣어서 ModelAndView(); 를 생성하면 정상흐름으로 계속 return
                    되어서 서블릿 컨테이너, WAS까지 정상적으로 흐름이 return 된다.

                    이렇게 해주면 여기서 예외를 먹어 버린 것이다. 정상 흐름으로 계속 return 되어서 서블릿 컨테이너까지
                    가면 response.sendError(HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());를 서블릿
                    컨테이너가 보고 WAS는 상태코드를 400으로 인식한다.
                */
            }
        }catch(IOException e){
            // try{..}를 실행하는 과정에서 예외가 발생하면 로그 한번 찍자
            log.error("resolver ex", e);
        }
        // 이렇게 하면 그냥 예외가 다시 터져서 날라간다.
        return null;
    }

}
