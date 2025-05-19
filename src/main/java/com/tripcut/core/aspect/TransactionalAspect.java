package com.tripcut.core.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.tripcut.core.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Aspect
@Component
@RequiredArgsConstructor
public class TransactionalAspect {

    private final PlatformTransactionManager transactionManager;

    @Around("@annotation(transactional)")
    public Object transactionAround(ProceedingJoinPoint joinPoint, Transactional transactional) throws Throwable {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(transactional.readOnly());
        
        if (transactional.timeout() > 0) {
            def.setTimeout(transactional.timeout());
        }

        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            Object result = joinPoint.proceed();
            transactionManager.commit(status);
            return result;
        } catch (Exception e) {
            if (shouldRollback(e, transactional)) {
                transactionManager.rollback(status);
            } else {
                transactionManager.commit(status);
            }
            throw e;
        }
    }

    private boolean shouldRollback(Exception e, Transactional transactional) {
        Class<? extends Exception> exceptionClass = e.getClass();
        
        for (Class<? extends Throwable> rollbackException : transactional.rollbackFor()) {
            try {
                if (Class.forName(String.valueOf(rollbackException)).isAssignableFrom(exceptionClass)) {
                    return true;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        
        for (String noRollbackException : transactional.noRollbackFor()) {
            try {
                if (Class.forName(noRollbackException).isAssignableFrom(exceptionClass)) {
                    return false;
                }
            } catch (ClassNotFoundException ignored) {
            }
        }
        
        return true;
    }
} 