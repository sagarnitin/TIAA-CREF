package com;

import static org.testng.Assert.assertEquals;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import javax.imageio.ImageIO;
import org.apache.commons.codec.binary.Base64;
import org.openqa.selenium.By;
import org.openqa.selenium.NotFoundException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
public class QRTest {

	public static WebDriver driver;
public static void main(String[] args) throws Exception{
	


 
    	System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"\\Library\\IEDriverServer.exe");

        driver.get("https://eliasnogueira.github.io/selenium-read-qrcode/");


        String qrCodeFile = driver.findElement(By.id("qr")).getAttribute("src");

        // get the qr code content and assert the result
        String qrCodeResult = decodeQRCode(qrCodeFile);
        assertEquals(qrCodeResult, "Congratulations!");
 
        String qrCodeFile1 = driver.findElement(By.id("qr-base64")).getAttribute("src");

        /*
         * Split the content of src attribute from qr-base64 image to get only the Base64 String
         * Because the src starts with 'data:image/png;base64,' and following text is the Base64 String
         */
        String base64String = qrCodeFile1.split(",")[1];

        // get the qr code content and assert the result
        String qrCodeResult1 = decodeQRCode(base64String);
        assertEquals(qrCodeResult1, "QR Code Base64 output text");   
        driver.quit();
  
}
   
    public static String decodeQRCode(Object qrCodeImage) throws IOException, NotFoundException, Exception {
        BufferedImage bufferedImage;

        // if not (probably it is a URL
        if (((String) qrCodeImage).contains("http")) {
            bufferedImage = ImageIO.read((new URL((String)qrCodeImage)));

            // if is a Base64 String
        } else {
            byte[] decoded = Base64.decodeBase64((String)qrCodeImage);
            bufferedImage = ImageIO.read(new ByteArrayInputStream(decoded));
        }

        LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        Result result = new MultiFormatReader().decode(bitmap);
        return result.getText();
    }
}