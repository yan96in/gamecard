package com.sp.platform;

import com.sp.platform.entity.SmsBillLog;
import com.sp.platform.entity.SmsBillTemp;
import com.sp.platform.service.BillTempService;
import com.sp.platform.timer.TimerMain;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: yangl
 * Date: 13-11-23 下午10:03
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class BillTest {
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    private BillTempService billTempService;
    @Autowired
    TimerMain timerMain;

    int line = 1;

    @Test
    public void haoduan() throws IOException, SQLException {
        String sql = "select code from mobilelocation";
        List<Map<String, Object>> l = jdbcTemplate.queryForList(sql);
        Map<String, String> haoduan = new HashMap<String, String>();
        for (Map<String, Object> map : l) {
            haoduan.put(map.get("code").toString(), map.get("code").toString());
        }

        List<String> list = FileUtils.readLines(new File("/Users/yanglei/Downloads/号段.txt"));
        int i = 0;

        List<String[]> noHao = new ArrayList<String[]>();
        for (String str : list) {
            String[] strs = str.split("\t");
            if (StringUtils.isBlank(haoduan.get(strs[0]))) {
                noHao.add(strs);
                i++;
            }
        }
        System.out.println(i);
        insertBooks(noHao);
    }

    public void insertBooks(List<String[]> book) throws SQLException, IOException {

        List<String> sqls = new ArrayList<String>();
        for (String[] strings : book) {
            String sql = "insert into mobilelocation(code,province,city) values('" + strings[0] + "','" + strings[1] + "','" + strings[2] + "');";
            sqls.add(sql);
        }
        FileUtils.writeLines(new File("/Users/yanglei/Downloads/haoduan.sql"), sqls);

//        final List<String[]> tempBook = book;
//        String sql = "insert into mobilelocation(code,province,city) values(?,?,?)";
//        Connection conn = ConnectDB.getConnection("MySql", "139.159.0.226:3301/skyd", "remoteuser", "service@remote");
//        PreparedStatement st = null;
//        ResultSet rs = null;
//        st = conn.prepareStatement(sql);
//        for (int i = 0; i < tempBook.size(); i++) {
//
//            st.setString(1, tempBook.get(i)[0]);
//            st.setString(2, tempBook.get(i)[1]);
//            st.setString(3, tempBook.get(i)[2]);
//            st.addBatch();
//            if (i % 1000 == 0) {
//                st.executeBatch();
//                st.clearBatch();
//            }
//        }
//        st.executeBatch();

    }

    @Test
    public void bill_diff() throws IOException {
        List<String> list = FileUtils.readLines(new File("e://sp-platform-sp.log"));
        int i = 0;

        for (String str : list) {
            if (StringUtils.indexOf(str, "status=DELIVRD") >= 0) {
                String s = StringUtils.left(str, 144);
                s = StringUtils.right(s, 8);
                System.out.println(StringUtils.left(str, 19) + " : " + s);
                jdbcTemplate.execute("update sms_bill_log set btime='" + StringUtils.left(str, 19)
                        + "',etime='" + StringUtils.left(str, 19) + "' where linkid='" + s + "'");
                i++;
            }
        }
        System.out.println(i);
    }

    @Test
    public void mobile_null() throws IOException {
        String sql = "select LINKID from SMS_BILL_LOG where MOBILE is NULL";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            list2.add(map.get("LINKID").toString());
        }

        FileUtils.writeLines(new File("e://linkid.txt"), list2);
    }

    @Test
    public void update_mobile() throws Exception {
        List<String> list = FileUtils.readLines(new File("e://linkid.txt"));

        List<String> list2 = FileUtils.readLines(new File("e://sp-platform-sp.log.2013-11-20"));

        List<String> list3 = new ArrayList<String>();

        Map<String, String> map = new HashMap<String, String>();
        String temp;
        String temp2;
        String ss;
        for (String str : list2) {
            ss = str;
            if (str.indexOf("linkid") >= 0 && str.indexOf("mobile") >= 0) {
                temp = StringUtils.substring(str, str.indexOf("linkid=") + 7);
                temp = StringUtils.substring(temp, 0, temp.indexOf("&"));

                temp2 = StringUtils.substring(str, str.indexOf("mobile=") + 7);
                temp2 = StringUtils.substring(temp2, 0, temp2.indexOf("&"));
                map.put(temp, temp2);
            }
            if (str.indexOf("link_id") >= 0 && str.indexOf("mo_from") >= 0) {
                temp = StringUtils.substring(str, str.indexOf("link_id=") + 8);
                temp = StringUtils.substring(temp, 0, temp.indexOf("&"));

                temp2 = StringUtils.substring(str, str.indexOf("mo_from=") + 8);
                temp2 = StringUtils.substring(temp2, 0, temp2.indexOf("&"));
                map.put(temp, temp2);
            }
        }
        System.out.println("-----------------------");

        String string;
        for (String str : list) {
            string = map.get(str);
            if (StringUtils.isNotBlank(string)) {
                String sql = "update SMS_BILL_LOG set mobile='" + string + "' where linkid='" + str + "'";
                System.out.println(sql);
                jdbcTemplate.execute("update SMS_BILL_LOG set mobile='" + string + "' where linkid='" + str + "'");
                list3.add(str + " - " + string);
            }
        }
        FileUtils.writeLines(new File("e://linkid-mobile.txt"), list3);
    }

    @Test
    public void query_linkid_18_20() throws IOException {
        String sql = "select * from SMS_BILL_LOG where btime>'2013-11-18' and btime<'2013-11-21' and msg in('C#BL01','C#BL02','C#BL')";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        StringBuilder builder;
        for (Map<String, Object> map : list) {
            builder = new StringBuilder();
            builder.append(map.get("id")).append(",");
            builder.append(map.get("mobile")).append(",");
            builder.append(map.get("spnum")).append(",");
            builder.append(map.get("msg")).append(",");
            builder.append(map.get("linkid")).append(",");
            builder.append(map.get("status")).append(",");
            builder.append(map.get("btime")).append(",");
            builder.append(map.get("etime")).append(",");
            builder.append(map.get("province")).append(",");
            builder.append(map.get("city")).append(",");
            builder.append(map.get("fee")).append(",");
            builder.append(map.get("sfid")).append(",");
            builder.append(map.get("cpid")).append(",");
            builder.append(map.get("type")).append(",");
            builder.append(map.get("syncurl")).append(",");
            builder.append(map.get("parentid"));

            list2.add(builder.toString());
        }

        FileUtils.writeLines(new File("e://linkid.txt"), list2);
    }

    @Test
    public void save_bill() throws IOException {
        List<String> list = FileUtils.readLines(new File("e://linkid.txt"));
        for (String str : list) {
            String[] temp = str.split(",");
            StringBuilder builder = new StringBuilder();
            builder.append("insert into sms_bill_log values(");
            builder.append(temp[0]).append(",");
            builder.append("'").append(temp[1]).append("',");
            builder.append("'").append(temp[2]).append("',");
            builder.append("'").append(temp[3]).append("',");
            builder.append("'").append(temp[4]).append("',");
            builder.append("'").append(temp[5]).append("',");
            builder.append("'").append(temp[6]).append("',");
            builder.append("'").append(temp[7]).append("',");
            builder.append("'").append(temp[8]).append("',");
            builder.append("'").append(temp[9]).append("',");

            builder.append(temp[10]).append(",");
            builder.append(temp[11]).append(",");
            builder.append(temp[12]).append(",");
            builder.append(temp[13]).append(",");

            builder.append("'").append(temp[14]).append("',");
            builder.append(temp[15]).append(")");
            jdbcTemplate.execute(builder.toString());
        }
    }

    @Test
    public void init_database() throws IOException {
        List<String> list = FileUtils.readLines(new File("d://sp_ff//database.txt"));
        for (String str : list) {
            if (StringUtils.isBlank(str)) {
                continue;
            }

            jdbcTemplate.execute(str);
        }
    }

    @Test
    public void query_test() throws IOException {
        String sql = "select called,province from tbl_province_reduce";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<String> list2 = new ArrayList<String>();
        for (Map<String, Object> map : list) {
            System.out.println(map.get("province").toString());
        }
    }

    @Test
    public void test() throws Exception {
        bufa1();
        bufa2();
    }

    public void bufa1() throws Exception {
        List<String> list = FileUtils.readLines(new File("e://bill//20_1.txt"));

        for (String str : list) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://localhost:8080/receivesms/4/res.sms?" + str);
            HttpResponse response = client.execute(get);
            System.out.println(response.getStatusLine().getStatusCode());
            Thread.sleep(10);
            client.getConnectionManager().shutdown();
        }
    }

    public void bufa2() throws Exception {
        List<String> list = FileUtils.readLines(new File("e://bill//20_2.txt"));

        for (String str : list) {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet("http://localhost:8080/receivesms/4/res.sms?" + str);
            HttpResponse response = client.execute(get);
            System.out.println(response.getStatusLine().getStatusCode());
            Thread.sleep(10);
            client.getConnectionManager().shutdown();
        }
    }

    @Test
    public void ttt() throws IOException {
        List<SmsBillTemp> list = billTempService.getCalloutData();
        List<String> bill = new ArrayList();
        for (SmsBillTemp smsBillTemp : list) {
            if (!StringUtils.equals("DELIVRD", StringUtils.upperCase(smsBillTemp.getStatus()))) {
                smsBillTemp.setFee(0);
            }
            SmsBillLog billLog = new SmsBillLog();
            billLog.setLinkid(smsBillTemp.getLinkid());
            billLog.setMobile(smsBillTemp.getMobile());
            billLog.setSpnum(smsBillTemp.getSpnum());
            billLog.setMsg(smsBillTemp.getMsg());
            billLog.setStatus(smsBillTemp.getStatus());
            billLog.setBtime(smsBillTemp.getBtime());
            billLog.setEtime(smsBillTemp.getEtime());
            billLog.setProvince(smsBillTemp.getProvince());
            billLog.setCity(smsBillTemp.getCity());
            billLog.setFee(smsBillTemp.getFee());
            billLog.setSfid(smsBillTemp.getSfid());
            billLog.setCpid(smsBillTemp.getCpid());
            billLog.setParentid(smsBillTemp.getParentid());
            billLog.setType(smsBillTemp.getType());
            billLog.setSyncurl(smsBillTemp.getSyncurl());

            StringBuilder sql = new StringBuilder();
            sql.append("insert into sms_bill_log values(");
            sql.append("'").append(billLog.getMobile()).append("',");
            sql.append("'").append(billLog.getSpnum()).append("',");
            sql.append("'").append(billLog.getMsg()).append("',");
            sql.append("'").append(billLog.getLinkid()).append("',");
            sql.append("'").append(billLog.getStatus()).append("',");
            sql.append("'").append(billLog.getBtime()).append("',");
            sql.append("'").append(billLog.getEtime()).append("',");
            sql.append("'").append(billLog.getProvince()).append("',");
            sql.append("'").append(billLog.getCity()).append("',");
            sql.append(billLog.getFee()).append(",");
            sql.append(billLog.getSfid()).append(",");
            sql.append(billLog.getCpid()).append(",");
            sql.append(billLog.getType()).append(",");
            sql.append("'").append(billLog.getSyncurl()).append("',");
            sql.append(billLog.getParentid()).append(")");

            bill.add(sql.toString());
        }
        FileUtils.writeLines(new File("e://sms_bill_log.txt"), bill);
    }

    @Test
    public void tttt() throws IOException {
        List<String> list = FileUtils.readLines(new File("e://sms_bill_log.txt"));
        for (String sql : list) {
            jdbcTemplate.execute(sql);
        }
    }

    @Test
    public void testGetProvinceFee() throws InterruptedException {
        timerMain.start();
        Thread.sleep(1000*30);
        Integer fee = billTempService.getProvinceFee("13552922122");
        System.out.println(fee);
    }
}
