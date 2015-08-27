package com.sp.platform.service;

import com.sp.platform.entity.Paytype;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:46
 * To change this template use File | Settings | File Templates.
 */
public interface PaytypeService extends AbstractService<Paytype> {
    List findByOi(Integer paytypeId);
}
