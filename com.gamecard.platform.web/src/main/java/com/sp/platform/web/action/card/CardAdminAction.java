package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.HaoduanCache;
import com.sp.platform.entity.Card;
import com.sp.platform.entity.Paychannel;
import com.sp.platform.entity.Paytype;
import com.sp.platform.entity.Price;
import com.sp.platform.service.*;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.vo.ChannelVo;
import com.sp.platform.vo.JsonVo;
import com.sp.platform.vo.PhoneVo;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-8-18
 * Time: 下午9:20
 * To change this template use File | Settings | File Templates.
 */
@Namespace("/card")
@Scope("prototype")
@InterceptorRefs({@InterceptorRef("loginInterceptor")})
@Results({@Result(name = "main", location = "main.jsp"),
        @Result(name = "upload", location = "upload.jsp")})
public class CardAdminAction extends ActionSupport {
    @Autowired
    private CardService cardService;
    @Autowired
    private PriceService priceService;

    private Integer id;
    private Integer priceId;
    private List list;
    private String message;

    @Action("upload")
    public String upload() {
        list = cardService.getAll();
        return "upload";
    }

    public void getPrices(){
        if (id == null) {
            JsonVo jsonVo = new JsonVo(false, "数据参数异常");
            Struts2Utils.renderJson(jsonVo);
            return;
        }
        list = priceService.getByCardId(id);
        JsonVo jsonVo = new JsonVo(true, list, "");
        Struts2Utils.renderJson(jsonVo);
    }

    @Action("doupload")
    public String doUpload(){
        list = cardService.getAll();
        return "index";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPriceId() {
        return priceId;
    }

    public void setPriceId(Integer priceId) {
        this.priceId = priceId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }
}
