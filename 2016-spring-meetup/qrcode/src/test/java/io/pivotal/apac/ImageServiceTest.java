package io.pivotal.apac;

import com.google.common.io.ByteStreams;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Created by sergiu on 8/12/16.
 */
public class ImageServiceTest {

    private ImageService imageService;

    @Before
    public void setup() {
        imageService = new ImageService();
    }

    @Test
    public void testImageServiceQrCodeGenerationSuccess() throws Exception {
        byte[] imageBlob = imageService.generateQRCode("This is a test", 256, 256);
        assertNotNull(imageBlob);
        byte[] testImage = ByteStreams.toByteArray(getClass().getResourceAsStream("/test.png"));

        Assert.assertArrayEquals(imageBlob, testImage);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageServiceQrCodeGenerationErrorNullText() throws Exception {
        imageService.generateQRCode(null, 256, 256);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageServiceQrCodeGenerationErrorEmptyText() throws Exception {
        imageService.generateQRCode("", 256, 256);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageServiceQrCodeGenerationErrorInvalidWidth() throws Exception {
        imageService.generateQRCode("This is a test", 0, 256);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testImageServiceQrCodeGenerationErrorInvalidHeight() throws Exception {
        imageService.generateQRCode("This is a test", 256, 0);

    }

}