package com.galuszkat.devices;

import avro.CameraImage;

public interface VideoDevice {

    CameraImage getImage();

    void closeCamera();
}
