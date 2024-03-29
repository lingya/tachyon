namespace java tachyon.thrift

struct NetAddress {
  1: string mHost
  2: i32 mPort
}

struct PartitionInfo {
  1: i32 mSizeBytes
  2: map<i64, NetAddress> mLocations
  3: string mHdfsPath
}

struct DatasetInfo {
  1: i32 mId
  2: i32 mVersion
  3: string mPath
  4: i64 mSizeBytes
  5: i32 mNumOfPartitions
  6: list<PartitionInfo> mPartitionList
  7: string mHdfsPath
  8: bool mCache
  9: bool mPin
}

enum CommandType {
  Unknown = 0,
  Nothing = 1,
  Register = 2,   // Ask the worker to re-register.
  Free = 3,				// Ask the worker to free partitions from a dataset.
  Delete = 4,			// Ask the worker to delete partitions from a dataset.
}

struct Command {
  1: CommandType mCommandType
  2: binary mData
}

exception OutOfMemoryForPinDatasetException {
  1: string message
}

exception DatasetAlreadyExistException {
  1: string message
}

exception DatasetDoesNotExistException {
  1: string message
}

exception NoLocalWorkerException {
  1: string message
}

exception PartitionDoesNotExistException {
  1: string message
}

exception PartitionAlreadyExistException {
  1: string message
}

exception SuspectedPartitionSizeException {
  1: string message
}

exception InvalidPathException {
  1: string message
}

service MasterService {
  // Services to Workers
  i64 worker_register(1: NetAddress workerNetAddress, 2: i64 totalBytes, 3: i64 usedBytes, 4: list<i64> currentPartitionList) // Returned value rv % 100,000 is really workerId, rv / 1000,000 is master started time.
  Command worker_heartbeat(1: i64 workerId, 2: i64 usedBytes, 3: list<i64> removedPartitionList)
  void worker_addPartition(1: i64 workerId, 2: i64 workerUsedBytes, 3: i32 datasetId, 4: i32 partitionId, 5: i32 partitionSizeBytes, 6: string hdfsPath) throws (1: PartitionDoesNotExistException eP, 2: SuspectedPartitionSizeException eS)
  set<i32> worker_getPinList()

  // Services to Users
  i64 user_getUserId()
  i32 user_createDataset(1: string datasetPath, 2: i32 partitions, 3: string hdfsPath) throws (1: DatasetAlreadyExistException eR, 2: InvalidPathException eI)
  i32 user_getDatasetId(1: string datasetPath)  // Return 0 if does not contain the dataset, return datasetId if it exists.
  NetAddress user_getLocalWorker(1: string host) throws (1: NoLocalWorkerException e) // Get local worker NetAddress
  PartitionInfo user_getPartitionInfo(1: i32 datasetId, 2: i32 partitionId) throws (1: PartitionDoesNotExistException e)  // Get partition info.
  DatasetInfo user_getDatasetById(1: i32 datasetId) throws (1: DatasetDoesNotExistException e)        // Get Dataset info by dataset Id.
  DatasetInfo user_getDatasetByPath(1: string datasetPath) throws (1: DatasetDoesNotExistException e) // Get Dataset info by path
  void user_deleteDataset(1: i32 datasetId) throws (1: DatasetDoesNotExistException e) // Delete dataset
  void user_unpinDataset(1: i32 datasetId) throws (1: DatasetDoesNotExistException e)   // Remove dataset from memory
  void user_renameDataset(1: string srcDataset, 2: string dstDataset) throws (1: DatasetDoesNotExistException e)
  void user_outOfMemoryForPinDataset(1: i32 datasetId)
  
  // cmd to scripts
  list<DatasetInfo> cmd_ls(1: string folder)  
}

service WorkerService {
  void accessPartition(1: i32 datasetId, 2: i32 partitionId)
  void addPartition(1: i64 userId, 2: i32 datasetId, 3: i32 partitionId, 4: string hdfsPath) throws (1: PartitionDoesNotExistException eP, 2: SuspectedPartitionSizeException eS, 3: PartitionAlreadyExistException eA)
  string getDataFolder()
  string getUserTempFolder(1: i64 userId)
  void returnSpace(1: i64 userId, 2: i64 returnedBytes)
  bool requestSpace(1: i64 userId, 2: i64 requestBytes)   // Should change this to return i64, means how much space to grant.
  void userHeartbeat(1: i64 userId)   // Local user send heartbeat to local worker to keep its temp folder.
}
