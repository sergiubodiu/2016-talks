package io.pivotal.apac;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.concurrent.ListenableFuture;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by sergiu on 8/12/16.
 */
@Service
public class ImageService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

    @Cacheable("qr-code-cache")
    public byte[] generateQRCode(String text, int width, int height) throws WriterException, IOException {

        Assert.hasText(text);
        Assert.isTrue(width > 0);
        Assert.isTrue(height > 0);

        LOGGER.info("Will generate image  text=[{}], width=[{}], height=[{}]", text, width, height);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitMatrix matrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height);
        MatrixToImageWriter.writeToStream(matrix, MediaType.IMAGE_PNG.getSubtype(), baos, new MatrixToImageConfig());
        return baos.toByteArray();
    }

    @Cacheable("qr-code-cache")
    public ListenableFuture<byte[]> generateQRCodeAsync(String text, int width, int height) throws Exception {
        return new AsyncResult<byte[]>(generateQRCode(text, width, height));
    }

}
