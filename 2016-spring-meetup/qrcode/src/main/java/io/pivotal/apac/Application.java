package io.pivotal.apac;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by sergiu on 8/12/16.
 */
@RestController
@SpringBootApplication
public class Application  {
    public static final String QRCODE_ENDPOINT = "/qrcode";

    @Autowired
    ImageService imageService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    @GetMapping(value = QRCODE_ENDPOINT, produces = MediaType.IMAGE_PNG_VALUE)
    public ListenableFuture<byte[]> getQRCode(@RequestParam(value = "text", required = true) String text) {
        try {
            return imageService.generateQRCodeAsync(text, 256, 256);
        } catch (Exception ex) {
            throw new InternalServerError("Error while generating QR code image.", ex);
        }
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public class InternalServerError extends RuntimeException {

        private static final long serialVersionUID = 1L;

        public InternalServerError(final String message, final Throwable cause) {
            super(message);
        }

    }
}
