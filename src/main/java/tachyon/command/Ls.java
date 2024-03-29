package tachyon.command;

import java.util.List;

import org.apache.thrift.TException;

import tachyon.MasterClient;
import tachyon.thrift.DatasetInfo;

public class Ls {
  public static void main(String[] args) throws TException {
    if (args.length != 1) {
      System.out.println("Usage: tachyon\n [-ls <path>]");
      System.exit(-1);
    }

    System.out.println(args[0]);
    String folder = Utils.getDatasetName(args[0]);
    MasterClient masterClient = new MasterClient(Utils.getTachyonMasterAddress(args[0]));
    masterClient.open();
    List<DatasetInfo> files = masterClient.cmd_ls(folder);
    System.out.println("The folder " + folder + " contains " + files.size() + " files");
    for (int i = 0; i < files.size(); i ++) {
      for (int j = i + 1; j < files.size(); j ++) {
        if (files.get(i).mPath.compareToIgnoreCase(files.get(j).mPath) > 0) {
          DatasetInfo tmp = files.get(i);
          files.set(i, files.get(j));
          files.set(j, tmp);
        }
      }
    }
    for (int k = 0; k < files.size(); k ++) {
      System.out.println(files.get(k) + " file(s) in total.");
    }
  }
}