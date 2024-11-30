package hello.exception.resolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.exception.exception.UserException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.html.HTML;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UserHandlerExceptionResolver implements HandlerExceptionResolver {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ModelAndView resolveException(HttpServletRequest request,
                                         HttpServletResponse response,
                                         Object handler,
                                         Exception ex) {

//        HandlerExceptionResolver를 API 예외가 발생하는 상황에서는 어떤 방식으로 사용되는지를 지금 학습하고 있다.
//        사용자가 http://localhost:8080/api/members/user-ex로 요청을 보내면 ApiExceptionController.java에서
//        예외를 발생시키는데, 그 예외는 내가 만들어 놓은 UserException.java 이다. UserException 예외가 발생하면 그 예외가
//        DispatcherServlet으로 가고 ExceptionResolver로 갈 것이다. 다시말해 예외가 DispatcherServlet으로 간 다음에
//        내가 지금 작성하고 있는 UserHandlerExceptionResolver.java로 갈 것이다. UserHandlerExceptionResolver에서는
//        예외가 들어오면 어떤방식으로 처리하는 지를 학습해 보고 있는 중이다. 한번더 써 보자면 API 예외가 발생했을시에는
//        HandlerExceptionResolver가 어떤 방식으로 예외를 처리하는지를 지금 학습하고 있는 것이다.

     /*
        결론을 먼저 쓰자면 API예외가 발생하고 HandlerExceptionResolver을 쓸 경우에는 HandlerExceptionResolver에서 예외를
        전부 처리하고 response 까지 세팅을 다 한 다음에 response를 WAS밖으로 보내도록 코드를 짠다.
        API에서 예외가 발생할 시에 그런식으로 HandlerExceptionResolver을 활용한다. WAS까지 예외가 안가기 떄문에 내부 호출도 없다.
      */

        try {
            if (ex instanceof UserException) {
                log.info("UserException resolver to 400");
                String acceptHeader = request.getHeader("accept");
                // 여기서 더 정교하게 처리하려면 HTTP헤더의 accept가 JSON인 경우와 JSON이 아닌경우(HTML로 요청하는경우) 이렇게
                // 나누어서 처리를 해 줘야 한다.
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                // HTTP 상태코드도 400으로 바꿔주겠다.
                // 잘 보면 response.sendError()가 아니다. 따라서 WAS까지는 새로운 예외가 전달되지 않고 WAS는 내부호출을 하지 않는다.

                if ("application/json".equals(acceptHeader)) {
                    Map<String, Object> errorResult = new HashMap<>();
                    errorResult.put("ex", ex.getClass());
                    errorResult.put("message", ex.getMessage());
                    String result = objectMapper.writeValueAsString(errorResult);
                    // response.getWriter().write(); 에 있는 write()인자에 JSON으로 바꿔서 넣어 줘야 하니까 ObjectMapper가 필요하다.
                    // objectMapper을 이용해서 errorResult를 String 타입을 갖는 JSON으로 바꿔서 result 변수에 담았고 그 result를
                    // write();의 인자로 넣어줄 것이다.

                    // 이제 errorResult 데이터를 response(응답메시지 전체)에 넣어줘야 한다.
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write(result);

                    // resolveException의 반환타입은 ModelAndView 이다. 즉, dispatcherServlet에 ModelAndView를 보내고 render을
                    // 호출해서 view템플릿을 완성된 뷰로 만든다음에 그 뷰를 응답메시지 바디에 담아서 클라이언트에게 보내는 매커니즘을
                    // 제공하고 있다. 따라서 우선 ModelAndView를 응답(리턴)해야 한다. 그래야지 dispatcher과 view, render 과 같은
                    // 메커니즘이 정상적으로 일단은 실행이 된다. 그리고 resolveException 내부에서 ModelAndView를 리턴을 하면
                    // resolveException으로 들어왔던 예외는 처리가 되는 것이다.
                    // ( 참고로 만일 response.sendError()을 호출하면 예외를 또 발생시킬 수 있다. )
                    // 그리고 정상흐름의 과정에서는 인자로 response가 계속 반환되게 되어서 결국에는 response가 servlet container 넘어서
                    // WAS까지 가고 WAS는 response(응답메시지 전체)를 보고 응답메시지로 response를 클라이언트에 보낸다.
                    // ( new ModelAndView() 처럼 빈 ModelAndView를 반환하면 WAS는 뷰를 렌더링 하지 않는다. )

                    return new ModelAndView();

                } else {
                    // request의 accept 에 TEXT/HTML 이런것들이 넘어오면
                    return new ModelAndView("error/500");
                    // resources -> templates -> error -> 500.html 이 호출된다. 뷰리졸버 까지 가서 랜더링 해버리겠다는 얘기이다.
                }
            }
        }catch(IOException e){
            // objectMapper.writeValueAsString(); 메소드를 쓰면 체크예외(IOException) 발생해서 try{...}catch{...} 로 묶어줬다.
            log.error("resolver ex", e);
        }

        return null;
    }

}