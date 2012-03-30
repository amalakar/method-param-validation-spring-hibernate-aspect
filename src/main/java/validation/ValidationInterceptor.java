/**
 *  Copyright 2011 Gunnar Morling (http://www.gunnarmorling.de/)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package validation;

import java.util.Set;

import javax.validation.Validation;
import javax.validation.Validator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.validator.method.MethodConstraintViolation;
import org.hibernate.validator.method.MethodConstraintViolationException;
import org.hibernate.validator.method.MethodValidator;


@Aspect
public class ValidationInterceptor {

    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    //Match any public methods in a class annotated with @AutoValidating
    @Around("execution(public * *(..)) && @within(validation.AutoValidating)")
    public Object validateMethodInvocation(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        MethodValidator methodValidator = validator.unwrap( MethodValidator.class );

        Set<MethodConstraintViolation<Object>> parametersViolations = methodValidator.validateAllParameters(
                pjp.getTarget(), signature.getMethod(), pjp.getArgs()
        );
        if ( !parametersViolations.isEmpty() ) {
            throw new MethodConstraintViolationException( parametersViolations );
        }

        result =  pjp.proceed(); //Execute the method

        Set<MethodConstraintViolation<Object>> returnValueViolations = methodValidator.validateReturnValue(
                pjp.getTarget(), signature.getMethod(), result
        );
        if ( !returnValueViolations.isEmpty() ) {
            throw new MethodConstraintViolationException( returnValueViolations );
        }

        return result;
    }
}
