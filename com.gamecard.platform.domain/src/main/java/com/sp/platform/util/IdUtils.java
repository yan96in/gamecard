package com.sp.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: mopdzz
 * Date: 14-10-27
 * Time: 下午11:05
 * To change this template use File | Settings | File Templates.
 */
public class IdUtils {

    private static SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMddHHmmss");
    /**
     * ID生成器
     *
     * @param moduleName   模块简称 如（mb） 长度2 位
     * @return
     */
    public static String idGenerator(String moduleName){
        StringBuffer sb = new StringBuffer("");
        if(StringUtil.isNotBlank(moduleName)){
            if(moduleName.length()>=2){
                sb.append(moduleName.substring(0, 2));
            }else{
                sb.append(StringUtil.stringFilling(moduleName,"AB",2));
            }
        }else{
            sb.append(StringUtil.stringFilling(moduleName,"AB",2));
        }
        sb.append(timeString());
        sb.append(randString());
        sb.append(randNumber());
        return sb.toString();
    }
    /**
     * 生成系统时间字符串
     * @return
     */
    private static String timeString(){
        StringBuffer sb = new StringBuffer("");
        sb.append(sdf.format(new Date()));
        String temp=sb.toString();
        Random r=new Random();
        StringBuffer sbTemp = new StringBuffer("");
        int range=temp.length();
        for(int i=0;i<range;i++){
            sbTemp.append(temp.charAt(i));
        }
        sbTemp.append(r.nextInt(10)).append(r.nextInt(10)).append(r.nextInt(10));
        return sbTemp.toString();
    }

    /**
     * 生成最后4 位随机数
     * @return
     */
    public static String randNumber(){
        Random random = new Random();
        StringBuffer sb = new StringBuffer("");
        sb.append(random.nextInt(10));
        sb.append(random.nextInt(10));
        sb.append(random.nextInt(10));
        sb.append(random.nextInt(10));
        return sb.toString();
    }

    /**
     * 生成3位随即字符串
     * @return
     */
    private static String randString(){
        StringBuffer buffer=new StringBuffer("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ");
        StringBuffer sb=new StringBuffer("");
        Random r=new Random();
        int range=buffer.length();
        for(int i=0;i<3;i++){
            sb.append(buffer.charAt(r.nextInt(range)));
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String id = IdUtils.idGenerator("wo");
		System.out.println("id: " + id);
        id = IdUtils.idGenerator("wo");
        System.out.println("id: " + id);
        id = IdUtils.idGenerator("wo");
        System.out.println("id: " + id);
        id = IdUtils.idGenerator("wo");
        System.out.println("id: " + id);
    }
}
