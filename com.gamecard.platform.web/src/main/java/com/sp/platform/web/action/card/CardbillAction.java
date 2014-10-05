package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.TimeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-9-20
 * Time: 下午9:12
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/card")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "list", location = "card_bill_list.jsp"),
        @Result(name = "provincelist", location = "card_province_list.jsp"),
        @Result(name = "cardcount", location = "card_count.jsp"),
        @Result(name = "userbill", location = "user_bill.jsp")})
public class CardbillAction extends ActionSupport {
    @Autowired
    private UserCardLogSerivce userCardLogSerivce;
    @Autowired
    private BillTempService billTempService;

    private PageView pageView = new PageView();
    private List list;
    private List list2;

    public String list() {
        if (pageView == null) {
            pageView = new PageView();
        }
        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }

        list = userCardLogSerivce.getCardBill(pageView);
        return "list";
    }

    public String provinceList(){
        list = userCardLogSerivce.getCardProvince(pageView);
        return "provincelist";
    }

    public String userBill(){
        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }
        String[] sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        pageView.setBtime(sj[0]);
        pageView.setEtime(sj[1]);
        list = billTempService.getSmsByCaller(pageView);
        list2 = userCardLogSerivce.getListByCaller(pageView);
        return "userbill";
    }

    public String cardCount(){
        list = userCardLogSerivce.getCardCount();
        return "cardcount";
    }

    public PageView getPageView() {
        return pageView;
    }

    public void setPageView(PageView pageView) {
        this.pageView = pageView;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getList2() {
        return list2;
    }

    public void setList2(List list2) {
        this.list2 = list2;
    }
}
