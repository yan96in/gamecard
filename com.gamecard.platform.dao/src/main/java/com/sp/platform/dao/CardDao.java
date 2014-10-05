package com.sp.platform.dao;

import com.sp.platform.entity.Card;
import com.yangl.common.hibernate.HibernateDaoUtil;
import org.springframework.stereotype.Repository;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午10:21
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class CardDao extends HibernateDaoUtil<Card, Integer> {
}
