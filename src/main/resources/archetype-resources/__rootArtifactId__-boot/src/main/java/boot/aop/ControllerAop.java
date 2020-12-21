#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.boot.aop;


import ${package}.api.dto.base.ResponseBean;
import ${package}.boot.config.MessageManager;
import ${package}.domain.exception.ExceptionConstants;
import ${package}.domain.exception.AppRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 异常处理、参数校验
 * @author ${author}
 * @version 1.0
 * @date 2020-12-21 20:05
 **/
@Slf4j
@Aspect
@Component
public class ControllerAop {
    /**
     * 参数验证器
     */
    private final Validator validator;
    /**
     *
     */
    private static final AtomicLong SEQ_GLOBAL_FOR_LOG = new AtomicLong(1);
    /**
     * 响应时间格式
     */
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 获取properties的配置文案
     */
    private final MessageManager messageManager;

    /**
     * 构造器
     */
    public ControllerAop(Validator validator, MessageManager messageManager) {
        this.validator = validator;
        this.messageManager = messageManager;
    }

    @Pointcut("execution(* ${package}.adapter.controller.impl.*.*(..))")
    public void baseResourcePointCut() {
    }

    @Around(value = "baseResourcePointCut()", argNames = "pjp")
    public Object around(ProceedingJoinPoint pjp) {
        Object proceed;
        try {
            MDC.put("requestSeq", String.valueOf(SEQ_GLOBAL_FOR_LOG.getAndIncrement()));
            //检验参数
            proceed = checkParam(pjp);
            if (proceed == null) {
                //执行方法
                proceed = proceed(pjp);
            }
            log.info("响应结果:{}", proceed);
            if (proceed instanceof ResponseBean) {
                buildResponseBean((ResponseBean<?>)proceed);
            }
        } finally {
            MDC.clear();
        }
        return proceed;
    }

    /**
     *
     */
    private Object proceed(ProceedingJoinPoint pjp){
        try {
            return pjp.proceed();
        } catch (AppRuntimeException appException) {
            log.error("AppRuntimeException:{}-{}", appException.getErrorCode(),appException.getErrorMessage());
            return buildResult(pjp, appException);
        } catch (Throwable throwable) {
            log.error("", throwable);
            return buildResult(pjp);
        }
    }

    /**
     * 构造返回值
     =     */
    private void buildResponseBean(ResponseBean<?> proceed){
        proceed.setResTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
        if (StringUtils.isEmpty(proceed.getResultCode())) {
            proceed.setResultCode(ExceptionConstants.SUCCESS);
        }
        if (StringUtils.isEmpty(proceed.getResultInfo())) {
            proceed.setResultInfo(messageManager.message(proceed.getResultCode()));
        }
    }

    /**
     * 参数校验
     */
    private Object checkParam(ProceedingJoinPoint pjp){
        try{
            Object[] args = pjp.getArgs();
            Signature signature = pjp.getSignature();
            log.info("请求接口：{}.{}", signature.getDeclaringTypeName(), signature.getName());
            if (args.length > 0) {
                List<ConstraintViolation<Object>> validate = new ArrayList<>();
                for (int i = 0; i < args.length; i++) {
                    Object arg = args[i];
                    log.info("请求参数[{}]:{}", i, arg);
                    validate.addAll(validator.validate(arg));
                }
                if (!validate.isEmpty()) {
                    sort(validate);
                    String msg = joinMsg(validate);
                    return buildResult(pjp, ExceptionConstants.PARAM_EXCEPTION, msg);
                }
            }
        }catch (Exception e){
            log.error("checkParam error", e);
            return buildResult(pjp, ExceptionConstants.PARAM_EXCEPTION, "check param error");
        }
        return null;
    }

    /**
     * 参数校验结果处理
     */
    private String joinMsg(List<ConstraintViolation<Object>> validate) {
        return validate.stream().map(v -> v.getPropertyPath() + v.getMessage()).collect(Collectors.joining(";"));
    }

    /**
     *
     */
    private void sort(List<ConstraintViolation<Object>> validate) {
        validate.sort(Comparator.comparing(Object::hashCode));
    }

    /**
     *
     */
    private Object buildResult(ProceedingJoinPoint pjp) {
        return buildResult(pjp, ExceptionConstants.UN_KNOW_EXCEPTION, messageManager.message(ExceptionConstants.UN_KNOW_EXCEPTION));
    }

    /**
     *
     */
    private Object buildResult(ProceedingJoinPoint pjp, AppRuntimeException appRuntimeException) {
        String errorMessage = appRuntimeException.getErrorMessage();
        String errorCode = appRuntimeException.getErrorCode();
        if (StringUtils.isEmpty(errorMessage)) {
            return buildResult(pjp, errorCode, messageManager.message(errorCode));
        } else {
            return buildResult(pjp, errorCode, errorMessage);
        }
    }

    /**
     *
     */
    private Object buildResult(ProceedingJoinPoint pjp, String code, String msg) {
        Signature signature = pjp.getSignature();
        Class<?> returnType = null;
        if (signature instanceof MethodSignature) {
            returnType = ((MethodSignature) signature).getReturnType();
        }
        Object o = null;
        try {
            assert returnType != null;
            o = returnType.newInstance();
        } catch (IllegalAccessException | InstantiationException ignored) {
            if (ResponseBean.class.isAssignableFrom(returnType)) {
                log.error("class '{}' hasn't non parameter constructor", returnType.getName());
            } else {
                log.error("class '{}' should extends ${groupId}.token.export.vo.ResponseBean ", returnType.getName());
            }
        }
        if (o instanceof ResponseBean) {
            ResponseBean<?> responseBean = (ResponseBean<?>) o;
            responseBean.setResultCode(code);
            responseBean.setResultInfo(msg);
            responseBean.setResTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(TIME_FORMAT)));
            return o;
        }
        return o;
    }
}
