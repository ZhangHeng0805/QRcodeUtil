package com.zhangheng.qrcode;

import com.google.zxing.*;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.OutputStream;
import java.util.Hashtable;

/**
 * 二维码工具类(格式固定)
 * @author 张恒
 * @program: QRcodeUtil
 * @email zhangheng.0805@qq.com
 * @date 2022-02-17 14:19
 */
public class QRCodeUtil {

    //编码格式
    private static final String CHARSET = "utf-8";
    //图片格式
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;
    // LOGO宽度
    private static final int LOGO_WIDTH = 60;
    // LOGO高度
    private static final int LOGO_HEIGHT = 60;


    /**
     * 创建二维码
     * @param content 二维码内容
     * @param logoImgPath Logo图片路径
     * @param needCompress 是否压缩LOGO图片
     * @return 二维码BufferedImage数据
     * @throws Exception
     */
    private static BufferedImage createImage(String content, String logoImgPath, boolean needCompress) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        if (logoImgPath == null || "".equals(logoImgPath)) {
            return image;
        }
        // 插入图片
        QRCodeUtil.insertImage(image, logoImgPath, needCompress);
        return image;
    }

    /**
     * 插入LOGO图片
     * @param source 二维码
     * @param imgPath Logo图片路径
     * @param needCompress 是否压缩LOGO图片
     * @throws Exception
     */
    private static void insertImage(BufferedImage source, String imgPath, boolean needCompress) throws Exception {
        File file = new File(imgPath);
        if (!file.exists()) {
            System.err.println("" + imgPath + "   该文件不存在！");
            return;
        }
        Image src = ImageIO.read(new File(imgPath));
        int width = src.getWidth(null);
        int height = src.getHeight(null);
        if (needCompress) { // 压缩LOGO
            if (width > LOGO_WIDTH) {
                width = LOGO_WIDTH;
            }
            if (height > LOGO_HEIGHT) {
                height = LOGO_HEIGHT;
            }
            Image image = src.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics g = tag.getGraphics();
            g.drawImage(image, 0, 0, null); // 绘制缩小后的图
            g.dispose();
            src = image;
        }
        // 插入LOGO
        Graphics2D graph = source.createGraphics();
        int x = (QRCODE_SIZE - width) / 2;
        int y = (QRCODE_SIZE - height) / 2;
        graph.drawImage(src, x, y, width, height, null);
        Shape shape = new RoundRectangle2D.Float(x, y, width, width, 6, 6);
        graph.setStroke(new BasicStroke(3f));
        graph.draw(shape);
        graph.dispose();
    }

    /**
     * 自动创建多层目录
     * @param destPath
     */
    public static void mkdirs(String destPath) {
        File file = new File(destPath);
        // 当文件夹不存在时，mkdirs会自动创建多层目录，区别于mkdir．(mkdir如果父目录不存在则会抛出异常)
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
        }
    }

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

    /**
     * 二维码编码 生成没有logo的二维码图片
     * @param content 二维码内容
     * @param destPath 保存的二维码的路径及名称
     * @throws Exception
     */
    public static boolean encode(String content, String destPath) throws Exception {
        return QRCodeUtil.encode(content, null, destPath, false);
    }
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

    /**
     * 二维码编码 ,直接生成没有logo图片文件
     * @param content 二维码内容
     * @param output 二维码的OutputStream流
     * @throws Exception
     */
    public static boolean encode(String content, OutputStream output) throws Exception {
        return QRCodeUtil.encode(content, null, output, false);
    }

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

}
