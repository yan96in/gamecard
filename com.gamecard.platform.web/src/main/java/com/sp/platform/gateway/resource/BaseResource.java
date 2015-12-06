/**
 *
 */
package com.sp.platform.gateway.resource;

import com.sp.platform.gateway.constant.Status;
import com.sp.platform.gateway.exception.HTTPException;
import com.sp.platform.gateway.exception.ValidationException;
import com.sp.platform.gateway.validator.Groups.CheckParam;
import com.sp.platform.util.LogEnum;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author yang lei
 */
public abstract class BaseResource {

    public static Logger log = LoggerFactory.getLogger(BaseResource.class);

    /**
     * Definition Validation
     */
    private static Validator validator;

    /** Initialize validation */
    static {
        if (validator == null) {
            LogEnum.DEFAULT.debug("Initialize validation");
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
    }

    /**
     * do validation
     */
    protected <T> void validator(T t) throws HTTPException {
        LogEnum.DEFAULT.debug("Do Validation");

        checkValidatorResult(validator.validate(t));
        checkValidatorResult(validator.validate(t, CheckParam.class));
    }

    private <T> void checkValidatorResult(Set<ConstraintViolation<T>> constraintViolationsByCheckAllow) {
        if (constraintViolationsByCheckAllow.size() > 0) {
            LogEnum.DEFAULT.debug("The Parameters was Incorrect!");

            List<String> messageList = new ArrayList<String>();
            for (ConstraintViolation<T> ConstraintViolation : constraintViolationsByCheckAllow) {
                LogEnum.DEFAULT.debug("error is :" + ConstraintViolation.getPropertyPath().toString() + " " + ConstraintViolation.getMessage());
                messageList.add(ConstraintViolation.getPropertyPath().toString() + " " + ConstraintViolation.getMessage());
            }
            throw new ValidationException(Status.BAD_REQUEST, messageList, new ValidationException());
        }
    }

    protected int getPcSpType(String channelType) {
        int type = 0;
        if (StringUtils.equals("WO+", channelType)) {
            type = 1;
        } else if (StringUtils.equals("翼光", channelType)) {
            type = 2;
        } else if (StringUtils.equals("移动游戏", channelType)) {
            type = 3;
        } else if (StringUtils.equals("翼龙", channelType)) {
            type = 4;
        } else if (StringUtils.equals("空中移动", channelType)) {
            type = 5;
        } else if (StringUtils.equals("电信", channelType)) {
            type = 6;
        }
        return type;
    }
}
