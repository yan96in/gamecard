package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.common.PageView;
import com.sp.platform.service.PcCardLogService;
import com.sp.platform.util.TimeUtils;
import com.yangl.common.Struts2Utils;
import com.yangl.common.hibernate.PaginationSupport;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-12-12
 * Time: 下午8:50
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/pccard")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({
        @Result(name = "userbill", location = "user_bill.jsp"),
        @Result(name = "userbilllist", location = "user_bill_list.jsp"),
        @Result(name = "usercardlist", location = "user_card_list.jsp")})
public class PccardAction extends ActionSupport {
    @Autowired
    PcCardLogService pcCardLogService;

    private int pageSize;
    private int pageNum;
    private PaginationSupport paginationSupport;
    private Order[] orders;
    private String pageGoto;

    private PageView pageView = new PageView();
    private List list;

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

        paginationSupport = pcCardLogService.getPage(paginationSupport, orders, pageView);
        StringBuilder url = new StringBuilder();
        url.append(Struts2Utils.getRequest().getContextPath());
        url.append("/pccard/pccard!userCardList.action?pageView.btime=");
        url.append(pageView.getBtime());
        url.append("&pageView.etime=");
        url.append(pageView.getEtime());
        if(StringUtils.isNotBlank(pageView.getCalled())){
            url.append("&pageView.caller=");
            url.append(pageView.getCaller());
        }
        url.append("&pageView.type=").append(pageView.getType());
        url.append("&pageView.spid=").append(pageView.getSpid());

        paginationSupport.setUrl(url.toString());

        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);
        list = paginationSupport.getItems();
        return "usercardlist";
    }

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

    public PaginationSupport getPaginationSupport() {
        return paginationSupport;
    }

    public void setPaginationSupport(PaginationSupport paginationSupport) {
        this.paginationSupport = paginationSupport;
    }

    public Order[] getOrders() {
        return orders;
    }

    public void setOrders(Order[] orders) {
        this.orders = orders;
    }

    public String getPageGoto() {
        return pageGoto;
    }

    public void setPageGoto(String pageGoto) {
        this.pageGoto = pageGoto;
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
}
