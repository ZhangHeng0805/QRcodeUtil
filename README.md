# QRcodeUtil
# [项目jar下载]("https://github.com/ZhangHeng0805/QRcodeUtil/releases/download/v1.0/QRcodeUtil.zip")
生成和解析二维码
> * 只能解析黑白像素默认格式的二维码，彩色，艺术格式的二维码可能解析错误！
> * 生成的二维码规格为300×300，其中logo规格为60×60
## 直接使用
可以直接运行项目jar，通过命令窗口输入```java -jar jar名```直接运行jar包
- 1、生成和解析的二维码文件请放入/qrcode/文件夹中
- 2、如果生成带logo的二维码,请将logo图片命名为logo.jpg,并放在该jar包同级目录中
- 3、使用环境JDK1.8+


## 方法使用
#### 一、生成二维码
 > * QRCodeUtil.encode(String content, String logoImgPath, String destPath, boolean needCompress);
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
> * QRCodeUtil.encode(String content, String logoImgPath, boolean needCompress);
```java
    /**
     * 二维码编码 生成BufferedImage数据
     * @param content 二维码内容
     * @param logoImgPath logo图片路径
     * @param needCompress 是否压缩logo图片
     * @return 二维码BufferedImage数据
     * @throws Exception
     */
    public static BufferedImage encode(String content, String logoImgPath, boolean needCompress) throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
        return image;
    }
```
> * QRCodeUtil.encode(String content, String logoImgPath, String destPath);
```java
    /**
     * 二维码编码 生成带未压缩logo的二维码图片
     * @param content 二维码内容
     * @param logoImgPath logo图片路径
     * @param destPath 保存的二维码的路径及名称
     * @throws Exception
     */
    public static boolean encode(String content, String logoImgPath, String destPath) throws Exception {
        return QRCodeUtil.encode(content, logoImgPath, destPath, false);
    }
```
> * QRCodeUtil.encode(String content, String destPath);
```java
    /**
     * 二维码编码 生成没有logo的二维码图片
     * @param content 二维码内容
     * @param destPath 保存的二维码的路径及名称
     * @throws Exception
     */
    public static boolean encode(String content, String destPath) throws Exception {
        return QRCodeUtil.encode(content, null, destPath, false);
    }
```
> * QRCodeUtil.encode(String content, String logoImgPath, OutputStream output, boolean needCompress);
```java
    /**
     * 二维码编码 ,直接生成带logo图片文件
     * @param content 二维码内容
     * @param logoImgPath logo图片路径
     * @param output 二维码的OutputStream流
     * @param needCompress 是否压缩logo图片
     * @throws Exception
     */
    public static boolean encode(String content, String logoImgPath, OutputStream output, boolean needCompress)
            throws Exception {
        BufferedImage image = QRCodeUtil.createImage(content, logoImgPath, needCompress);
        return ImageIO.write(image, FORMAT_NAME, output);
    }
```
> * QRCodeUtil.encode(String content, OutputStream output);
```java
    /**
     * 二维码编码 ,直接生成没有logo图片文件
     * @param content 二维码内容
     * @param output 二维码的OutputStream流
     * @throws Exception
     */
    public static boolean encode(String content, OutputStream output) throws Exception {
        return QRCodeUtil.encode(content, null, output, false);
    }
```
#### 二、解析二维码
> * QRCodeUtil.decode(File file);
```java
    /**
     * 二维码解码 通过文件解码
     * @param file 二维码图片文件
     * @return
     * @throws Exception
     */
    public static String decode(File file) throws Exception {
        BufferedImage image;
        image = ImageIO.read(file);
        if (image == null) {
            return null;
        }
        BufferedImageLuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        Result result;
        Hashtable hints = new Hashtable();
        // 解码设置编码方式为：utf-8
        hints.put(DecodeHintType.CHARACTER_SET, CHARSET);
        //优化精度
        hints.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        //复杂模式，开启PURE_BARCODE模式
        hints.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
        result = new MultiFormatReader().decode(bitmap, hints);
        String resultStr = result.getText();
        return resultStr;
    }
```
> * QRCodeUtil.decode(String path);
```java
    /**
     * 二维码解码 通过文件路径解码
     * @param path 二维码图片保存路径
     * @return
     * @throws Exception
     */
    public static String decode(String path) throws Exception {
        File file = new File(path);
        if (file.exists()) {
            return QRCodeUtil.decode(file);
        }else {
            throw new Exception("文件"+path+"不存在(file not exist！)");
        }
    }
```
