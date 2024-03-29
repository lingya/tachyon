package tachyon;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import tachyon.thrift.DatasetInfo;

public class MasterLogWriter {
  private final String LOG_FILE_NAME;
  
  private ObjectOutputStream mOutputStream;
  
  public MasterLogWriter(String fileName) {
    LOG_FILE_NAME = fileName;
    try {
      mOutputStream = new ObjectOutputStream(new FileOutputStream(LOG_FILE_NAME));
    } catch (FileNotFoundException e) {
      CommonUtils.runtimeException(e);
    } catch (IOException e) {
      CommonUtils.runtimeException(e);
    }
  }
  
  public void appendAndFlush(DatasetInfo datasetInfo) {
    try {
      mOutputStream.writeObject(datasetInfo);
      mOutputStream.flush();
    } catch (IOException e) {
      CommonUtils.runtimeException(e);
    }
  }
  
  public void close() {
    try {
      mOutputStream.close();
    } catch (IOException e) {
      CommonUtils.runtimeException(e);
    }
  }
}