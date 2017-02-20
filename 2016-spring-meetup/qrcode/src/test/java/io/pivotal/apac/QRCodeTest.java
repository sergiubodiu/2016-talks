package io.pivotal.apac;

import com.google.common.io.ByteStreams;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by sergiu on 8/12/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QRCodeTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext context;

    @Value("${security.user.password}")
    private String password;

    private byte[] testImage;

    @Before
    public void setup() throws IOException {
        testImage = ByteStreams.toByteArray(getClass().getResourceAsStream("/test.png"));
    }

    @Test
    public void testQrCodeControllerSuccess() throws IOException {
        ResponseEntity<byte[]> entity = this.restTemplate
                .withBasicAuth("user", password)
                .getForEntity(Application.QRCODE_ENDPOINT + "?text=This is a test", byte[].class);
        System.out.println(entity);

        assertEquals(entity.getStatusCode(), HttpStatus.OK);
        assertEquals(entity.getHeaders().getContentType(), MediaType.IMAGE_PNG);
        assertArrayEquals(entity.getBody(), testImage);
    }

    @Test
    public void testImageServiceQrCodeGenerationSuccess() throws Exception {
        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .build();
        MvcResult result = mockMvc.perform(get(Application.QRCODE_ENDPOINT)
                    .param("text", "This is a test").contentType(MediaType.IMAGE_PNG))
                .andExpect(status().isOk())
                .andReturn();

        mockMvc.perform(asyncDispatch(result))
                .andExpect(content().contentType(MediaType.IMAGE_PNG))
                .andExpect(content().bytes(testImage))
                .andReturn();
    }
}
