# QRcodeUtil
生成和解析二维码
> * 只能解析黑白像素默认格式的二维码，彩色，艺术格式的二维码可能解析错误！
> * 生成的二维码规格为300×300，其中logo规格为60×60
## 直接使用
可以直接运行项目jar，通过命令窗口输入```java -jar jar名```直接运行jar包
- 1、生成和解析的二维码文件请放入/qrcode/文件夹中
- 2、如果生成带logo的二维码,请将logo图片命名为logo.jpg,并放在该jar包同级目录中


## 方法使用
#### 一、生成二维码
 > * QRCodeUtil.encode(String content, String logoImgPath, String destPath, boolean needCompress)
   ```java
    /**
     * 二维码编码，直接生成带logo图片文件
     * @param content 二维码内容
     * @param logoImgPath logo图片路径
     * @param destPath 保存的二维码的路径及名称
     * @param needCompress 是否压缩logo图片
     * @throws Exception
     */
    public static boolean encode(String content, String logoImgPath, String destPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
        mkdirs(destPath);
        return ImageIO.write(image, FORMAT_NAME, new File(destPath));
    }
```
