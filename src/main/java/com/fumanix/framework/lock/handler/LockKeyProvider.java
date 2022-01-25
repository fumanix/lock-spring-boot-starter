package com.fumanix.framework.lock.handler;

import com.fumanix.framework.lock.annotation.DLock;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 分布式锁 键 生成策略
 * @create: 2019-12-25 19:31
 */
@Component
public class LockKeyProvider {

    private ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    private ExpressionParser parser = new SpelExpressionParser();

    /**
     * 获取自定义注解Key
     * @param joinPoint
     * @param dLock
     * @return
     */
    public String getKeyName(JoinPoint joinPoint, DLock dLock) {
        List<String> keyList = new ArrayList<>();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        List<String> definitionKeys = getSpellDefinitionKey(dLock.keys(), method, joinPoint.getArgs());
        keyList.addAll(definitionKeys);
        return StringUtils.collectionToDelimitedString(keyList,"","-","");
    }

    /**
     * 获取Spell表达式
     * @param definitionKeys
     * @param method
     * @param parameterValues
     * @return
     */
    private List<String> getSpellDefinitionKey(String[] definitionKeys, Method method, Object[] parameterValues) {
        List<String> definitionKeyList = new ArrayList<>();
        for (String definitionKey : definitionKeys) {
            if (StringUtils.hasText(definitionKey)) {
                EvaluationContext context = new MethodBasedEvaluationContext(null, method, parameterValues, nameDiscoverer);
                String key = parser.parseExpression(definitionKey).getValue(context).toString();
                definitionKeyList.add(key);
            }
        }
        return definitionKeyList;
    }
}
