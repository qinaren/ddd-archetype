#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.aop;

import ${package}.boot.config.MessageManager;
import ${package}.api.dto.base.ResponseBean;
import ${package}.domain.exception.AppRuntimeException;
import ${package}.domain.exception.ExceptionConstants;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ControllerAop.class, ValidationAutoConfiguration.class})
public class ControllerAopTest {
    @MockBean
    private MethodInvocationProceedingJoinPoint proceedingJoinPoint;
    @MockBean
    private MethodSignature signature;
    @MockBean
    private MessageManager messageManager;
    @Autowired
    private ControllerAop controllerAop;
    @Before
    public void before(){
        when(proceedingJoinPoint.getArgs()).thenReturn(getArgs());
        when(proceedingJoinPoint.getSignature()).thenReturn(signature);
        when(signature.getReturnType()).thenReturn(ResponseBean.class);
        try {
            when(proceedingJoinPoint.proceed()).thenReturn(buildResponseBean());
        } catch (Throwable ignored) {
        }
    }
    @Test
    public void success(){
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.SUCCESS.equals(around.getResultCode());
    }
    @Test
    public void paramEx(){
        when(proceedingJoinPoint.getArgs()).thenReturn(getArgs1());
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.PARAM_EXCEPTION.equals(around.getResultCode());
    }
    @Test
    public void responseEx(){
        when(proceedingJoinPoint.getArgs()).thenReturn(getArgs1());
        when(signature.getReturnType()).thenReturn(ResponseTestBean.class);
        controllerAop.around(proceedingJoinPoint);
        assert true;
    }
    @Test
    public void testIsException(){
        try {
            when(proceedingJoinPoint.proceed()).thenThrow(new AppRuntimeException(ExceptionConstants.UN_KNOW_EXCEPTION));
        } catch (Throwable throwable) {
            assert false;
        }
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.UN_KNOW_EXCEPTION.equals(around.getResultCode());
    }
    @Test
    public void testIsException2(){
        try {
            when(proceedingJoinPoint.proceed()).thenThrow(new AppRuntimeException(ExceptionConstants.UN_KNOW_EXCEPTION,"TEST"));
        } catch (Throwable throwable) {
            assert false;
        }
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.UN_KNOW_EXCEPTION.equals(around.getResultCode());
        assert "TEST".equals(around.getResultInfo());
    }
    @Test
    public void testIsException3(){
        try {
            when(proceedingJoinPoint.proceed()).thenThrow(new RuntimeException("TEST EXCEPTION"));
        } catch (Throwable throwable) {
            assert false;
        }
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.UN_KNOW_EXCEPTION.equals(around.getResultCode());
    }
    @Test
    public void testCodeIsNull(){
        ResponseBean<Object> objectResponseBean = new ResponseBean<>();
        objectResponseBean.setResultCode(null);
        try {
            when(proceedingJoinPoint.proceed()).thenReturn(objectResponseBean);
        } catch (Throwable throwable) {
            assert false;
        }
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.SUCCESS.equals(around.getResultCode());
    }
    @Test
    public void testIsSuccess(){
        ResponseBean<Object> objectResponseBean = new ResponseBean<>();
        objectResponseBean.setResultCode(ExceptionConstants.UN_KNOW_EXCEPTION);
        try {
            when(proceedingJoinPoint.proceed()).thenReturn(objectResponseBean);
        } catch (Throwable throwable) {
            assert false;
        }
        ResponseBean<?> around = (ResponseBean<?>)controllerAop.around(proceedingJoinPoint);
        assert ExceptionConstants.UN_KNOW_EXCEPTION.equals(around.getResultCode());
    }
    private Object[] getArgs1(){
        Object[] objects = new Object[1];
        FindOneReqVo findOneReqVo = new FindOneReqVo();
        objects[0] = findOneReqVo;
        return objects;
    }
    private Object[] getArgs(){
        Object[] objects = new Object[2];
        objects[0] = "AB";
        objects[1] = "TEST";
        return objects;
    }
    private ResponseBean<?> buildResponseBean(){
        ResponseBean<?> responseBean = new ResponseBean<>();
        responseBean.setResultCode(ExceptionConstants.SUCCESS);
        return responseBean;
    }
    @Test
    public void baseResourcePointCutTest(){
        controllerAop.baseResourcePointCut();
    }

    static class ResponseTestBean{
        private ResponseTestBean(){

        }
    }
    static class FindOneReqVo implements Serializable {
        /**
         * 用户pin
         */
        @NotBlank
        private String pin;
        /**
         * 活动id
         */
        @NotBlank
        private String activityId;
    }

}