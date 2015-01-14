package com.sp.platform.web.action.card;

import com.opensymphony.xwork2.ActionSupport;
import com.sp.platform.cache.CardCache;
import com.sp.platform.entity.CardPassword;
import com.sp.platform.service.CardPasswordService;
import com.sp.platform.service.CardService;
import com.sp.platform.service.PriceService;
import com.sp.platform.util.PropertyUtils;
import com.sp.platform.util.XDEncodeHelper;
import com.sp.platform.vo.JsonVo;
import com.yangl.common.Struts2Utils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.convention.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
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
        @Result(name = "upload", location = "card_upload.jsp")})
public class CardAdminAction extends ActionSupport {
    @Autowired
    private CardService cardService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private CardPasswordService cardPasswordService;
    @Autowired
    private PropertyUtils propertyUtils;

    private Integer id;
    private Integer priceId;
    private List list;
    private String message;

    private File myFile;

    @Action("upload")
    public String upload() {
        list = cardService.getAll();
        return "upload";
    }

    @Action("getPrices")
    public void getPrices() {
        if (id == null) {
            JsonVo jsonVo = new JsonVo(false, "数据参数异常");
            Struts2Utils.renderJson(jsonVo);
            return;
        }
        list = priceService.getByCardId(id);
        JsonVo jsonVo = new JsonVo(true, list, "");
        Struts2Utils.renderJson(jsonVo);
    }

    @Action("doUpload")
    public String doUpload() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(myFile)));

            String blackUser;
            blackUser = reader.readLine();
            List<CardPassword> cards = new ArrayList<CardPassword>();
            Date now = new Date();XDEncodeHelper xdEncodeHelper = new XDEncodeHelper(propertyUtils.getProperty("DESede.key", "tch5VEeZSAJ2VU4lUoqaYddP"));
            while (StringUtils.isNotBlank(blackUser)) {
                String[] temp = blackUser.split(",");
                CardPassword card = new CardPassword();
                card.setCardid(id);
                card.setPriceid(priceId);
                card.setCardno(xdEncodeHelper.XDEncode(temp[0]));
                card.setPassword(xdEncodeHelper.XDEncode(temp[1]));
//                card.setCardno(temp[0]);
//                card.setPassword(temp[1]);
                card.setState(0);
                card.setCtime(now);
                card.setUtime(now);
                cards.add(card);
                blackUser = reader.readLine();
            }
            cardPasswordService.save(cards);
            message = "导入成功，共导入 " + cards.size() + " 条 "
                    + CardCache.getCard(id).getName() + CardCache.getPrice(priceId).getDescription() + " 卡";
        } catch (Exception e) {
            e.printStackTrace();
        }

        list = cardService.getAll();
        return "upload";
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

    public File getMyFile() {
        return myFile;
    }

    public void setMyFile(File myFile) {
        this.myFile = myFile;
    }
}
