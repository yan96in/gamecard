package database;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Getpasswordnine {
    public static String getpassfile = "e:/newlog/ninegetpass.txt";
    public static String ivrlog = "e:/newlog/nineivrlog.txt";
    public static String error = "e:/newlog/nineerror.txt";

    public static String getPass(String phone, String called, String provinceid, String passwordtypeid) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        String str = null;
        String strone = null;
        try {
            String sql = "select id,cardnum,password from ivr_password where state >0 and passwordtypeid=" +
                    passwordtypeid + " ORDER BY rand() LIMIT 1 ";

            conn = MysqlgetConnect.getConncet();

            if (conn != null) {
                pstmt = conn.createStatement();
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
                    System.out.println(sql);
                    pstmt.execute(sql);
                } else {
                    return null;
                }

                pstmt = null;
                rst = null;
                MysqlgetConnect.backConncet(conn);
            } else {
                return null;
            }
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

    public static long todate(String datetime, String endtime) {
        Date currentTime = new Date();
        long min = 0L;
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = df.parse(datetime);
            Date enddate = df.parse(endtime);
            System.out.println(enddate + "====" + date);

            long l = enddate.getTime() - date.getTime();

            min = l / 1000L;

            return min;
        } catch (Exception w) {
            w.printStackTrace();
        }
        return min;
    }

    public static boolean insertlog(String phone, String called, String starttime, String endtime) {
        Statement pstmt = null;
        ResultSet rst = null;
        Connection conn = null;
        String str = null;

        String strstart = gettime(starttime);
        String strend = gettime(endtime);
        String fee = String.valueOf(todate(strstart, strend));

        boolean isback = false;
        try {
            String sql = "insert into ivr_usertj(phone,callednumber,starttime,endtime,fee,provinceid) values('" +
                    phone +
                    "','" +
                    called +
                    "','" +
                    strstart +
                    "','" +
                    strend + "','" + fee + "','" + 0 + "')";
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

    public static void main(String[] args) {
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
}
