package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.SpInfoCache;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.ServiceNum;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.service.UserCardLogSerivce;
import com.sp.platform.util.TimeUtils;
import com.yangl.common.Struts2Utils;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.hibernate.criterion.Order;
import org.joda.time.DateTime;
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
        @Result(name = "userbill", location = "user_bill.jsp"),
        @Result(name = "userbilllist", location = "user_bill_list.jsp"),
        @Result(name = "usercardlist", location = "user_card_list.jsp")})
public class CardbillAction extends ActionSupport {
    @Autowired
    private UserCardLogSerivce userCardLogSerivce;
    @Autowired
    private BillTempService billTempService;

    private PageView pageView = new PageView();
    private List list;
    private List list2;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    private int pageSize;
    private int pageNum;
    private PaginationSupport paginationSupport;
    private Order[] orders;
    private String pageGoto;

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

    public String provinceList() {
        list = userCardLogSerivce.getCardProvince(pageView);
        return "provincelist";
    }

    public String userBill() {
        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            DateTime dateTime = new DateTime();
            String shj = format.format(dateTime.toDate());
            pageView.setEtime(shj);
            dateTime = dateTime.plusDays(-360);
            shj = format.format(dateTime.toDate());
            pageView.setBtime(shj);
        }
        String[] sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        pageView.setBtime(sj[0]);
        pageView.setEtime(sj[1]);
        list = billTempService.getSmsByCaller(pageView);
        list2 = userCardLogSerivce.getListByCaller(pageView);
        return "userbill";
    }

    public String userBillList() {
        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;

        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }
        String[] sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        pageView.setBtime(sj[0]);
        pageView.setEtime(sj[1]);
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("id")};

        paginationSupport = billTempService.getPage(paginationSupport, orders, pageView);
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/card/cardbill!userBillList.action?pageView.btime="
                + pageView.getBtime() + "&pageView.etime=" + pageView.getEtime());
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);
        list = paginationSupport.getItems();
        return "userbilllist";
    }

    public String userCardList() {
        if (pageNum < 1)
            pageNum = 1;
        if (pageSize < 1)
            pageSize = com.sp.platform.common.Constants.page_size;

        if (StringUtils.isEmpty(pageView.getBtime()) || StringUtils.isEmpty(pageView.getEtime())) {
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String shj = format.format(new Date());
            pageView.setBtime(shj);
            pageView.setEtime(shj);
        }
        String[] sj = TimeUtils.chuli(pageView.getBtime(), pageView.getEtime());
        pageView.setBtime(sj[0]);
        pageView.setEtime(sj[1]);
        paginationSupport = new PaginationSupport(pageNum, pageSize);
        orders = new Order[]{Order.desc("id")};

        paginationSupport = userCardLogSerivce.getPage(paginationSupport, orders, pageView);
        paginationSupport.setUrl(Struts2Utils.getRequest().getContextPath() + "/card/cardbill!userCardList.action?pageView.btime="
                + pageView.getBtime() + "&pageView.etime=" + pageView.getEtime());
        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);
        list = paginationSupport.getItems();
        return "usercardlist";
    }

    public String cardCount() {
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

    public PaginationSupport getPaginationSupport() {
        return paginationSupport;
    }

    public void setPaginationSupport(PaginationSupport paginationSupport) {
        this.paginationSupport = paginationSupport;
    }

    public String getPageGoto() {
        return pageGoto;
    }

    public void setPageGoto(String pageGoto) {
        this.pageGoto = pageGoto;
    }
}
