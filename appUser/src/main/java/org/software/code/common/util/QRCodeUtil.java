package org.software.code.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 二维码生成工具类
 * 使用Google ZXing库生成二维码
 */
@Component
public class QRCodeUtil {

    @Autowired
    private OSSUtil ossUtil;

    private static final int DEFAULT_WIDTH = 300;
    private static final int DEFAULT_HEIGHT = 300;
    private static final String DEFAULT_FORMAT = "PNG";

    /**
     * 生成二维码并上传到OSS
     * @param content 二维码内容
     * @param fileName 文件名
     * @return 二维码图片URL
     */
    public String generateQRCodeAndUpload(String content, String fileName) {
        try {
            // 生成二维码图片
            BufferedImage qrImage = generateQRCodeImage(content, DEFAULT_WIDTH, DEFAULT_HEIGHT);
            
            // 将图片转换为字节数组
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(qrImage, DEFAULT_FORMAT, baos);
            byte[] imageBytes = baos.toByteArray();
            
            // 上传到阿里云OSS
            String ossUrl = ossUtil.uploadBytes(imageBytes, fileName + ".png", "qrcodes");
            
            return ossUrl;
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成二维码失败", e);
        }
    }

    /**
     * 生成二维码图片
     * @param content 二维码内容
     * @param width 图片宽度
     * @param height 图片高度
     * @return BufferedImage
     */
    public BufferedImage generateQRCodeImage(String content, int width, int height) 
            throws WriterException, IOException {
        
        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hints.put(EncodeHintType.MARGIN, 1);

        // 生成二维码
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        // 创建BufferedImage
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        // 设置颜色
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
            }
        }

        return image;
    }

    /**
     * 生成带Logo的二维码
     * @param content 二维码内容
     * @param width 图片宽度
     * @param height 图片高度
     * @param logoImage Logo图片
     * @return BufferedImage
     */
    public BufferedImage generateQRCodeWithLogo(String content, int width, int height, BufferedImage logoImage) 
            throws WriterException, IOException {
        
        BufferedImage qrImage = generateQRCodeImage(content, width, height);
        
        if (logoImage != null) {
            // 计算Logo大小（二维码的1/5）
            int logoWidth = width / 5;
            int logoHeight = height / 5;
            
            // 缩放Logo
            Image scaledLogo = logoImage.getScaledInstance(logoWidth, logoHeight, Image.SCALE_SMOOTH);
            BufferedImage logo = new BufferedImage(logoWidth, logoHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = logo.createGraphics();
            g2d.drawImage(scaledLogo, 0, 0, null);
            g2d.dispose();
            
            // 将Logo绘制到二维码中心
            Graphics2D qrG2d = qrImage.createGraphics();
            int logoX = (width - logoWidth) / 2;
            int logoY = (height - logoHeight) / 2;
            qrG2d.drawImage(logo, logoX, logoY, null);
            qrG2d.dispose();
        }
        
        return qrImage;
    }
}
