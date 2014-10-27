package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GetpasswordConn {
    public static String getpassfile = "e:/log/getpass.txt";
    public static String ivrlog = "e:/log/ivrlog.txt";
    public static String getnum = "e:/log/getnum.txt";
    public static String error = "e:/log/error.txt";

    public static String getPass(String phone, String called, String provinceid, String passwordtypeid, int mintime, int maxtime) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        String str = null;
        String strone = null;
        String insertdatetime = null;
        try {
            String sqlone = "select inserttime from ivr_getnum where id =(select max(id) from ivr_getnum where phone='" + phone + "' and called='" + called + "' and passwordtypeid='" + passwordtypeid + "' and state=1 order by id )";
            String sql = "select id,cardnum,password from ivr_password where state >0 and passwordtypeid=" +
                    passwordtypeid + " ORDER BY rand() LIMIT 1 ";

            conn = MysqlgetConnect.getConncet();

            if (conn != null) {
                pstmt = conn.createStatement();

                rst = pstmt.executeQuery(sqlone);
                while (rst.next()) {
                    insertdatetime = rst.getString(1);
                }

                if ((insertdatetime != null) && (!("".equals(insertdatetime))))
                    if(!(todate(insertdatetime, mintime, maxtime))){
                        return null;
                    }
            }

            rst = pstmt.executeQuery(sql);
            String id = "";

            int i = 0;
            while (rst.next()) {
                ++i;
                id = rst.getString(1);
                str = rst.getString(2) + "," + rst.getString(3);
                strone = rst.getString(3);
            }
            if (i > 0) {
                sql = "update ivr_password set phone='" + phone +
                        "', callednumber='" + called + "' ,provinceid='" +
                        provinceid +
                        "',inserttime=now()  ,state=0 where  id=" + id;
                pstmt.execute(sql);
            } else {
                return null;
            }

            pstmt = null;
            rst = null;
            MysqlgetConnect.backConncet(conn);

            return str;
        } catch (Exception w) {
            writeComLog(w.getMessage(), error);
            w.printStackTrace();
        } finally {
            pstmt = null;
            rst = null;
            MysqlgetConnect.backConncet(conn);
        }
        return str;
    }

    public static boolean todate(String datetime, int mintime, int maxtime) {
        Date currentTime = new Date();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(datetime);
            Date now = new Date();

            long l = now.getTime() - date.getTime();

            long min = l / 1000L;
            writeComLog("starttime==" + df.format(now) + "  endtime===" + df.format(date), getpassfile);

            if ((mintime > min) || (min >= maxtime))
                return false;
            return true;
        } catch (Exception w) {
            w.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
    }

    public static boolean insertlog(String phone, String called, String starttime, String endtime, String fee, String provinceid) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        String str = null;
        String strstart = gettime(starttime);
        String strend = gettime(endtime);

        boolean isback = false;
        try {
            String sql = "insert into ivr_usertj(phone,callednumber,starttime,endtime,fee,provinceid) values('" +
                    phone +
                    "','" +
                    called +
                    "','" +
                    strstart +
                    "','" +
                    strend + "','" + fee + "','" + provinceid + "')";
            conn = MysqlgetConnect.getConncet();

            if (conn != null) {
                pstmt = conn.createStatement();

                pstmt.execute(sql);
                isback = true;

                pstmt = null;
                rst = null;
                MysqlgetConnect.backConncet(conn);
            } else {
                return false;
            }

            return isback;
        } catch (Exception w) {
            writeComLog(w.getMessage(), error);
            w.printStackTrace();
        } finally {
            pstmt = null;
            rst = null;
            MysqlgetConnect.backConncet(conn);
        }
        return isback;
    }

    public static String gettime(String time) {
        String str = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" +
                time.substring(6, 8) + " " + time.substring(8, 10) + ":" +
                time.substring(10, 12) + ":" + time.substring(12, 14);
        return str;
    }

    public static boolean issp(String called, String sppassword) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        int num = 0;
        try {
            String sql = "select count(*) from ivr_spnumber where spnumber ='" + called + "' and sppassword='" +
                    sppassword + "' ";
            conn = MysqlgetConnect.getConncet();

            if (conn != null) {
                pstmt = conn.createStatement();

                rst = pstmt.executeQuery(sql);
                String id = "";

                while (rst.next()) {
                    num = rst.getInt(1);
                }

                pstmt = null;
                rst = null;
                MysqlgetConnect.backConncet(conn);
                return (num > 0);
            }

            return false;
        } catch (Exception w) {
            writeComLog(w.getMessage(), error);
            w.printStackTrace();
        } finally {
            pstmt = null;
            rst = null;
            MysqlgetConnect.backConncet(conn);
        }
        return false;
    }

    public static int getNum(String phone, String called, String passwordtypeid) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        int num = 0;
        try {
            String sql = "select count(*) from ivr_password where state >0 and passwordtypeid=" +
                    passwordtypeid + " ";
            conn = MysqlgetConnect.getConncet();

            if (conn != null) {
                pstmt = conn.createStatement();

                rst = pstmt.executeQuery(sql);
                String id = "";

                while (rst.next()) {
                    num = rst.getInt(1);
                }
                String sqlone = "insert into ivr_getnum(phone,called,passwordtypeid)  values('" + phone + "','" + called + "','" + passwordtypeid + "')";

                pstmt.execute(sqlone);
                pstmt = null;
                rst = null;
                MysqlgetConnect.backConncet(conn);
                if (num > 0)
                    return 1;
                return 0;
            }

            return -1;
        } catch (Exception w) {
            writeComLog(w.getMessage(), error);
            w.printStackTrace();
        } finally {
            pstmt = null;
            rst = null;
            MysqlgetConnect.backConncet(conn);
        }
        return 0;
    }

    public static void writeComLog(String str, String filename) {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        try {
            File f = new File(filename);
            BufferedWriter bufOut;
            if (f.exists())
                bufOut = new BufferedWriter(new FileWriter(f, true));
            else {
                bufOut = new BufferedWriter(new FileWriter(f));
            }
            bufOut.write(dateString + "-----" + str + "\r\n");
            bufOut.flush();
            bufOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void write(String a, String b) {
    }
}
