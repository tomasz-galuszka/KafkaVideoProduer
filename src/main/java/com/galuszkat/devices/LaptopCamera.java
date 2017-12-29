package com.galuszkat.devices;

import avro.CameraImage;
import com.github.sarxos.webcam.Webcam;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class LaptopCamera implements VideoDevice {

    private Webcam camera;

    public LaptopCamera() {
        camera = Webcam.getDefault();
        camera.open();
    }

    @Override
    public CameraImage getImage() {
        try {
            BufferedImage image = camera.getImage();
            log("Image captured");

            File tmpFile = new File("test.png");
            ImageIO.write(image, "PNG", tmpFile);
            log("Tmp file created");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();

            FileInputStream fis = new FileInputStream(tmpFile);
            byte[] buffer = new byte[1024];
            for (int readNumber; (readNumber = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, readNumber);
            }
            log("Output stream ready");

            tmpFile.delete();
            log("Tmp file deleted");

            return new CameraImage(ByteBuffer.wrap(bos.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void log(String msg) {
        System.out.println("-- " + msg);
    }

    @Override
    public void closeCamera() {
        camera.close();
    }

}
