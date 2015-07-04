package tachyon.client;

import tachyon.CommonUtils;
import tachyon.thrift.PartitionInfo;
import tachyon.thrift.DatasetInfo;

/**
 * Dataset handler.
 * 
 * @author haoyuan
 */
public class Dataset {
  private final TachyonClient mTachyonClient;

  private DatasetInfo mDatasetInfo;

  public Dataset(TachyonClient tachyonClient, DatasetInfo datasetInfo) {
    mTachyonClient = tachyonClient;
    mDatasetInfo = datasetInfo;
  }

  public synchronized Partition getPartition(int pId) {
    if (pId < 0 || pId >= mDatasetInfo.mNumOfPartitions) {
      CommonUtils.runtimeException(mDatasetInfo.mPath + " does not have partition " + pId + ". "
          + "It has " + mDatasetInfo.mNumOfPartitions + " partitions.");
    }
    return new Partition(mTachyonClient, this, mDatasetInfo.mId, pId);
  }

  public int getNumPartitions() {
    return mDatasetInfo.mNumOfPartitions;
  }

  public int getDatasetId() {
    return mDatasetInfo.mId;
  }

  public String getDatasetName() {
    return mDatasetInfo.mPath;
  }

  public PartitionInfo getPartitionInfo(int pId) {
    return mDatasetInfo.mPartitionList.get(pId);
  }

  public boolean needCache() {
    return mDatasetInfo.mCache || mDatasetInfo.mPin;
  }

  public boolean needPin() {
    return mDatasetInfo.mPin;
  }
}