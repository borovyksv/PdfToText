package com.borovyksv;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.junit.Test;

public class testUnzip {
  @Test
  public void unzip(){
      String source = "C:\\Users\\gamecaching\\Cache.zip";
      String destination = "C:\\Users\\gamecaching\\";
      String password = "mypassword";

      try {
        ZipFile zipFile = new ZipFile(source);
        if (zipFile.isEncrypted()) {
          zipFile.setPassword(password);
        }
        zipFile.extractAll(destination);
      } catch (ZipException e) {
        e.printStackTrace();
      }
  }
}
