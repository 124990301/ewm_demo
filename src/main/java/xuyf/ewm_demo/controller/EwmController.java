package xuyf.ewm_demo.controller;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;

@RestController
public class EwmController {

    /**
     * 二维码生成图片
     * @param content
     */
    @GetMapping("/getTp")
    public void getTp(String content){
        // 定义要生成二维码的基本参数
        int width = 300;
        int height = 300;
        String type = "png";

// 定义二维码的配置，使用HashMap
        HashMap hints = new HashMap();
// 字符集，内容使用的编码
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
// 容错等级，H、L、M、Q
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
// 边距，二维码距离边的空白宽度
        hints.put(EncodeHintType.MARGIN, 2);

        try {
            // 生成二维码对象，传入参数：内容、码的类型、宽高、配置
            BitMatrix bitMatrix =  new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // 定义一个路径对象
            Path file = new File("D:/learn/"+content+".png").toPath();
            // 生成二维码，传入二维码对象、生成图片的格式、生成的路径
            MatrixToImageWriter.writeToPath(bitMatrix, type, file);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 生成二维码返回base64
     * @param content
     * @param width
     * @param height
     * @return
     * @throws IOException
     */
    @GetMapping("/getBase64")
    public String getBase64(String content, int width, int height) throws IOException {

        if (!StringUtils.isEmpty(content)) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            HashMap<EncodeHintType, Comparable> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 指定字符编码为“utf-8”
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M); // 指定二维码的纠错等级为中级
            hints.put(EncodeHintType.MARGIN, 2); // 设置图片的边距

            try {
                QRCodeWriter writer = new QRCodeWriter();
                BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height, hints);

                BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(bitMatrix);
                ImageIO.write(bufferedImage, "png", os);
                /**
                 * 原生转码前面没有 data:image/png;base64 这些字段，返回给前端是无法被解析，可以让前端加，也可以在下面加上
                 */
                return "data:image/png;base64," + Base64.encode(os.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
