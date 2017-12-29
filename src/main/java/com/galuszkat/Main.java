package com.galuszkat;

import avro.CameraImage;
import com.galuszkat.devices.LaptopCamera;
import com.galuszkat.devices.VideoDevice;

public class Main {
    private VideoDevice myLaptopCamera = new LaptopCamera();
    private DataProducer dataTransporter = new DataProducer();

    public static void main(String[] args) throws InterruptedException {
        Main main = new Main();
        while (true) {
            CameraImage image = main.myLaptopCamera.getImage();
            main.dataTransporter.send(image);

            Thread.sleep(1000);
        }
    }
}
