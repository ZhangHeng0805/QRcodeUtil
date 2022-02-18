import com.zhangheng.qrcode.QRCodeUtil;
import com.zhangheng.test.Human;
import com.zhangheng.test.ZhangSan;

import java.io.File;
import java.util.Scanner;
import java.util.UUID;

public class Main {

    private static String[] imgType={".jpg",".png",".jpeg",".JPG",".PNG",".JPEG"};
    private static String logoName="logo";
    private static final String baseDir="./qrcode/";

    public static void main(String[] args) throws Exception {
        QRCodeUtil.mkdirs(baseDir);
        StringBuilder str_title=new StringBuilder();
        StringBuilder str_notice=new StringBuilder("使用须知:\n");
        str_notice.append("1、生成和解析的二维码文件请放入"+baseDir.substring(1)+"文件夹中\n")
                .append("2、如果生成带logo的二维码,请将logo图片命名为logo.jpg,并放在该jar包同级目录中\n")
                .append("3、生成的二维码规格为300×300，其中logo规格为60×60");
        str_title.append("\n======星曦向荣二维码系统======\n")
                .append("\t1.生成二维码\n")
                .append("\t2.解析二维码\n")
                .append("\t0.退出系统\n")
                .append("=============================\n")
                .append("请输入操作编号:");
        System.out.println(str_notice.toString());
        while (true){
            System.out.println(str_title.toString());
            Scanner sc = new Scanner(System.in);
            int i = sc.nextInt();
            if (i==0){
                System.out.println("系统关闭！");
                break;
            }else if (i==1){
                System.out.println("请输入生成的二维码内容:");
                Scanner sc1_content = new Scanner(System.in);
                String content=sc1_content.nextLine();
                System.out.println("请输入生成的二维码文件名称:");
                Scanner sc1_name = new Scanner(System.in);
                String name=sc1_name.nextLine();
                if (name==null||name.length()<=0){
                    name= UUID.randomUUID().toString().substring(0,6);
                }
                System.out.println(enCode(content, name));
            }else if (i==2){
                System.out.println("请输入解析的二维码文件名称:");
                Scanner sc2_name = new Scanner(System.in);
                String name=sc2_name.nextLine();
                System.out.println(deCode(name));
            }else {
                System.out.println("(＞人＜；)对不起，您输入操作编号错误！");
            }
        }

    }


    /**
     * 生成二维码
     * @param content 二维码内容
     * @param imgName 二维码图片名
     * @return
     * @throws Exception
     */
    public static String enCode(String content,String imgName) throws Exception {
        File logo=null;
        //消除图片名中的非法字符
        imgName=imgName.replace("/","")
                .replace("\\","")
                .replace(" ","");
        String imgPath=baseDir+imgName+".jpg";
        boolean isLogo=false;
        for (String l:imgType){
            logo=new File("./"+logoName+l);
            if (logo.exists()){
                isLogo=true;
                break;
            }
        }
        boolean isSuccess=false;
        if (isLogo){
           isSuccess= QRCodeUtil.encode(content,logo.getPath(),imgPath,true);
        }else {
            isSuccess= QRCodeUtil.encode(content,imgPath);
        }
        if (isSuccess){
            return "二维码生成成功!二维码路径: "+imgPath.substring(1);
        }else {
            return "二维码生成失败!";
        }
    }

    /**
     * 解析二维码
     * @param imgName 二维码名称
     * @return
     */
    public static String deCode(String imgName) throws Exception {
        String result="";
        File img=null;
        //消除图片名中的非法字符
        imgName=imgName.replace("/","")
                .replace("\\","")
                .replace(" ","");

        boolean isImg=false;
        for (String l:imgType){
            if (imgName.endsWith(l)){
                img=new File(baseDir+imgName);
            }else {
                img=new File(baseDir+imgName+l);
            }
            if (img.exists()){
                isImg=true;
                break;
            }
        }
        if (isImg){
            result = "解析结果:"+QRCodeUtil.decode(img);
        }else {
            result="解析失败！(＞人＜；)对不起，二维码文件没有找到";
        }
        return result;
    }
}
