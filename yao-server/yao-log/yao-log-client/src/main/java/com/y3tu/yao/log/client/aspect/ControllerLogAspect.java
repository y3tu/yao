package com.y3tu.yao.log.client.aspect;

import com.y3tu.tool.core.http.IpUtil;
import com.y3tu.yao.common.util.YaoUtil;
import com.y3tu.yao.log.client.annotation.ControllerLog;
import com.y3tu.yao.log.client.configure.LogProperties;
import com.y3tu.yao.log.client.constant.LogQueueNameConstant;
import com.y3tu.yao.log.client.constant.LogStatusEnum;
import com.y3tu.yao.log.client.constant.SaveModeEnum;
import com.y3tu.tool.core.exception.ExceptionUtil;
import com.y3tu.tool.core.util.StrUtil;
import com.y3tu.yao.log.client.entity.Log;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 日志处理Aop
 *
 * @author y3tu
 */
@Aspect
@Slf4j
public class ControllerLogAspect {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private LogProperties logProperties;

    /**
     * 配置切入点
     */
    @Pointcut("@annotation(com.y3tu.yao.log.client.annotation.ControllerLog)")
    public void logPointcut() {
        // 该方法无方法体,主要为了让同类中其他方法使用此切入点
    }

    /**
     * 配置环绕通知,使用在方法logPointcut()上注册的切入点
     *
     * @param pjp
     */
    @Around("logPointcut()")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) throws Throwable {
        Object result = null;
        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method targetMethod = methodSignature.getMethod();
        Class clazz = methodSignature.getDeclaringType();
        // 需要记录日志存库
        Log logDto = new Log();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //获取类注解
        ControllerLog logAnnotationClass = (ControllerLog) clazz.getAnnotation(ControllerLog.class);

        // 获取方式注解
        ControllerLog logAnnotation = targetMethod.getAnnotation(ControllerLog.class);

        String serverName = logAnnotation.serverName();
        String moduleName = logAnnotation.moduleName();
        if (StrUtil.isEmpty(serverName)) {
            serverName = logAnnotationClass.serverName();
        }
        if (StrUtil.isEmpty(moduleName)) {
            moduleName = logAnnotationClass.moduleName();
        }
        String className = pjp.getTarget().getClass().getName();
        String methodName = targetMethod.getName();
        logDto.setMethod(className + "." + methodName + "()");

        Object[] args = pjp.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(targetMethod);
        if (args != null && paramNames != null) {
            StringBuilder params = new StringBuilder();
            params = handleParams(params, args, Arrays.asList(paramNames));
            logDto.setParams(params.toString());
        }

        String ip = IpUtil.getIpAddr(request);
        logDto.setServerName(serverName)
                .setModuleName(moduleName)
                .setActionName(logAnnotation.actionName())
                .setActionType(logAnnotation.actionType().getValue())
                .setIp(ip)
                .setLocation(IpUtil.getCityInfo(ip))
                .setRequestUrl(request.getRequestURL().toString())
                .setUserAgent(request.getHeader("user-agent"));

        String username = YaoUtil.getCurrentUsername();
        logDto.setUsername(username);

        try {
            long startTime = System.currentTimeMillis();
            //执行操作
            result = pjp.proceed();
            // 本次操作用时（毫秒）
            long elapsedTime = System.currentTimeMillis() - startTime;
            log.info("[{}]use time: {}", pjp.getSignature(), elapsedTime);

            logDto.setTime(String.valueOf(elapsedTime));
            logDto.setStatus(LogStatusEnum.SUCCESS.getCode());
            return result;

        } catch (Throwable throwable) {

            logDto.setException(ExceptionUtil.getStackTrace(throwable));
            logDto.setStatus(LogStatusEnum.FAIL.getCode());
            throw throwable;

        } finally {
            // 发送消息到 系统日志队列
            String logQueue = "";
            if (logAnnotation.saveMode() == SaveModeEnum.DB) {
                logQueue = LogQueueNameConstant.DB_LOG_QUEUE;
            } else if (logAnnotation.saveMode() == SaveModeEnum.ES) {
                logQueue = LogQueueNameConstant.ES_LOG_QUEUE;
            } else {
                if (StrUtil.isNotEmpty(logProperties.getSaveMode())) {
                    String saveMode = logProperties.getSaveMode();
                    if ("ES".equals(saveMode)) {
                        logQueue = LogQueueNameConstant.ES_LOG_QUEUE;
                    } else {
                        logQueue = LogQueueNameConstant.DB_LOG_QUEUE;
                    }
                } else {
                    logQueue = LogQueueNameConstant.DB_LOG_QUEUE;
                }
            }
            rabbitTemplate.convertAndSend(logQueue, logDto);
        }
    }


    @SuppressWarnings("all")
    private StringBuilder handleParams(StringBuilder params, Object[] args, List paramNames) {
        try {
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Map) {
                    Set set = ((Map) args[i]).keySet();
                    List<Object> list = new ArrayList<>();
                    List<Object> paramList = new ArrayList<>();
                    for (Object key : set) {
                        list.add(((Map) args[i]).get(key));
                        paramList.add(key);
                    }
                    return handleParams(params, list.toArray(), paramList);
                } else {
                    if (args[i] instanceof Serializable) {
                        Class<?> aClass = args[i].getClass();
                        try {
                            aClass.getDeclaredMethod("toString", new Class[]{null});
                            // 如果不抛出 NoSuchMethodException 异常则存在 toString 方法 ，安全的 writeValueAsString ，否则 走 Object的 toString方法
                            params.append(" ").append(paramNames.get(i)).append(": ").append(StrUtil.str(args[i], "UTF-8"));
                        } catch (NoSuchMethodException e) {
                            params.append(" ").append(paramNames.get(i)).append(": ").append(StrUtil.str(args[i], "UTF-8"));
                        }
                    } else if (args[i] instanceof MultipartFile) {
                        MultipartFile file = (MultipartFile) args[i];
                        params.append(" ").append(paramNames.get(i)).append(": ").append(file.getName());
                    } else {
                        params.append(" ").append(paramNames.get(i)).append(": ").append(args[i]);
                    }
                }
            }
        } catch (Exception ignore) {
            params.append("参数解析失败");
        }
        return params;
    }

}
