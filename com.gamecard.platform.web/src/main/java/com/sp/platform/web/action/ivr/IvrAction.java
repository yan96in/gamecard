package com.sp.platform.web.action.ivr;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CardCache;
import com.sp.platform.common.PageView;
import com.sp.platform.entity.IvrCardLog;
import com.sp.platform.service.IvrCardLogService;
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
 * Created by yanglei on 15/8/3.
 */
@Namespace("/ivr")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({
        @Result(name = "list", location = "bill_list.jsp"),
        @Result(name = "card_list", location = "card_bill_list.jsp"),
        @Result(name = "userCardList", location = "user_card_list.jsp")})
public class IvrAction extends ActionSupport {
    @Autowired
    private IvrCardLogService ivrCardLogService;

    private int pageSize;
    private int pageNum;
    private PaginationSupport paginationSupport;
    private Order[] orders;
    private String pageGoto;

    private PageView pageView = new PageView();
    private List list;

    @Action("userCardList")
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
        paginationSupport = ivrCardLogService.getPage(paginationSupport, orders, pageView);

        StringBuilder url = new StringBuilder();
        url.append(Struts2Utils.getRequest().getContextPath());
        url.append("/ivr/userCardList.action?pageView.btime=");
        url.append(pageView.getBtime());
        url.append("&pageView.etime=");
        url.append(pageView.getEtime());
        if (StringUtils.isNotBlank(pageView.getCaller())) {
            url.append("&pageView.caller=");
            url.append(pageView.getCaller());
        }
        if (StringUtils.isNotBlank(pageView.getCalled())) {
            url.append("&pageView.called=");
            url.append(pageView.getCalled());
        }
        paginationSupport.setUrl(url.toString());

        pageGoto = PaginationSupport.getClientPageContent(paginationSupport);
        list = paginationSupport.getItems();

        for (int i = 0; i < list.size(); i++) {
            IvrCardLog ivrCardLog = (IvrCardLog) list.get(i);
            ivrCardLog.setCardShowName(CardCache.getCard(ivrCardLog.getCardid()).getName() + "-"
                    + CardCache.getPrice(ivrCardLog.getPriceid()).getDescription());
        }

        return "userCardList";
    }


    @Action("list")
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
        list = ivrCardLogService.getBillInfo(pageView);
        if(pageView.getCpid() == 2){
            return "card_list";
        }
        return "list";
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
