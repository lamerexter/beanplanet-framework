package org.beanplanet.core.io;

public interface ByteArrayReadCallback {
  void bytesRead(byte[] buffer, int fromindex, int bytesRead);
}
