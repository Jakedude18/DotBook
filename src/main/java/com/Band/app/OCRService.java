package com.Band.app;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.leptonica.PIX;
import org.bytedeco.leptonica.global.lept;
import org.bytedeco.tesseract.TessBaseAPI;

import java.io.File;


public class OCRService {
    public static final String ENG_LANG = "eng";


    public String parseImage(File file) {
        final TessBaseAPI api = new TessBaseAPI();
        if (api.Init("/home/fuzzy/src/Java/DotBook/src/main/resources", ENG_LANG) != 0) {
            System.out.println("Could not initialize tesseract.");
        }

        final String completableFuture;
        final PIX pix = lept.pixRead(file.getPath());
        pix.xres(300);
        pix.yres(300);

        api.SetImage(pix);
        api.SetSourceResolution(300);

        final BytePointer bytePointer = api.GetUTF8Text();
        String text = bytePointer.getString();

        bytePointer.deallocate();
        lept.pixDestroy(pix);
        file.delete();

        completableFuture = text;

        api.End();
        return completableFuture;
    }


}
