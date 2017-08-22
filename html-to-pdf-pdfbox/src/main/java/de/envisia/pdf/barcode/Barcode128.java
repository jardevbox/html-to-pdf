package de.envisia.pdf.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.awt.image.BufferedImage;

public class Barcode128 {

    private static MultiFormatWriter writer = new MultiFormatWriter();

    public static BufferedImage create(String text, int width, int height) {
        try {
            BitMatrix matrix = writer.encode(text, BarcodeFormat.CODE_128, width, height);
            return MatrixToImageWriter.toBufferedImage(matrix);
        } catch (WriterException we) {
            throw new RuntimeException(we);
        }
    }

}
