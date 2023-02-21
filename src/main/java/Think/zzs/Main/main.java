package Think.zzs.Main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/*
*
* 可以改造成数据库测试数据生成器
*
* */
public class main {
    public static void main(String[] args) throws IOException {
//        爬取网站
        String familynet="https://blog.csdn.net/weixin_30745553/article/details/101878981";
        String boysnamenet="https://www.yw11.com/html/qiming/hunianqiming/2011/0710/534.html";
        String girlsnamenet="https://jingyan.baidu.com/article/fea4511a40912af7bb912599.html";
//        html文本
        String familycontent=webCruler(familynet);
        String boyscontent=webCruler(boysnamenet);
        String girlscontent=webCruler(girlsnamenet);
//        正则
        List<String> familys = ReUtil.findAll("([\\u4E00-\\u9FA5]{1})(,)", familycontent, 1);
        List<String> boystempname = ReUtil.findAll("([\\u4E00-\\u9FA5]{2})([\\s])(..)", boyscontent, 1);
        List<String> girlstempname = ReUtil.findAll("([\\u4E00-\\u9FA5]{2})(、)(..)", girlscontent, 0);


//        数据处理
        List<String> boysname=boystempname;
        List<String> girlsname=DealGirls(girlstempname);

//        拼接名字
        List<String> boys = getBoys(familys, boysname, 50);
        List<String> girls = getGirls(familys, girlsname, 50);

//        存入本地文件
        FileUtil.writeLines(boys, "boys.txt","GBK");
        FileUtil.writeLines(girls, "girls.txt","GBK");

    }

    //    规范初爬取女生姓名
    private static List<String> DealGirls(List<String> girlstempname) {
        List<String> girlsname = new ArrayList<>();
        for (String girlname : girlstempname) {
            String[] split = girlname.split("、");
            for (int i=0;i<split.length;i++) {
                girlsname.add(split[i]);
            }
        }
        return girlsname;
    }

    //    拼接男生姓名
    private static List<String> getBoys(List<String>familys, List<String>boysname, int count){
        int i=0;
        HashSet<String> boys_hs=new HashSet<String>();
        Random random=new Random();
        while(i<count){
            Collections.shuffle(familys);
            Collections.shuffle(boysname);
            int age= random.nextInt(9)+18;
            boys_hs.add(familys.get(0)+boysname.get(0)+"-"+"男"+"-"+age);
            i++;
        }

        return boys_hs.stream().map(String::trim).collect(Collectors.toList());
    }

    //    拼接女生姓名
    private static List<String> getGirls(List<String>familys, List<String>girlsname, int count){
        int i=0;
        HashSet<String> girls_hs=new HashSet<String>();
        Random random=new Random();
        while(i<count){
            Collections.shuffle(familys);
            Collections.shuffle(girlsname);
            int age= random.nextInt(9)+18;
            girls_hs.add(familys.get(0)+girlsname.get(0)+"-"+"女"+"-"+age);
            i++;
        }
        return girls_hs.stream().map(String::trim).collect(Collectors.toList());
    }

    //    通过正则表达式获取需要的数据
    private static ArrayList<String> getData(String content, String regex,int index) {
//        正则表达式模型
        Pattern pattern=Pattern.compile(regex);
//        用自定义的正则表达式匹配文本
        Matcher matcher = pattern.matcher(content);
        ArrayList<String> arrayList=new ArrayList<>();
//        如果存在符合正则表达式的数据
        while (matcher.find()) {
//            跳一组  即指针
            String group=matcher.group(index);
            arrayList.add(group);
        }
        return arrayList;

    }

    //    爬取页面
    private static String webCruler(String net) throws IOException {
//        获取html content
        URL url = new URL(net);
//          打开连接
        URLConnection conn=url.openConnection();
//          获取输入流
        InputStreamReader inputStreamReader = new InputStreamReader(conn.getInputStream());
//          拼接字符串
        StringBuilder sb = new StringBuilder();
        int ch;
//          通过字符拼接组成html文本
        while ((ch = inputStreamReader.read())!= -1) {
            sb.append((char)ch);
        }
//             关闭流
        inputStreamReader.close();
//        返回html文本
        return sb.toString();
    }
}
