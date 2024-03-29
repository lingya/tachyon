package tachyon;

import java.net.InetSocketAddress;

import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadedSelectorServer;
import org.apache.thrift.transport.TNonblockingServerSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tachyon.thrift.MasterService;

/**
 * Entry point for the Master program. Master class is singleton. 
 * 
 * @author haoyuan
 */
public class Master {
  private static final Logger LOG = LoggerFactory.getLogger(Master.class);

  private static Master MASTER = null;

  private TServer mServer;
  private MasterServiceHandler mMasterServiceHandler;

  private Master(InetSocketAddress address, int selectorThreads, int acceptQueueSizePerThreads,
      int workerThreads) {
    try {
      mMasterServiceHandler = new MasterServiceHandler(address);
      MasterService.Processor<MasterServiceHandler> processor = 
          new MasterService.Processor<MasterServiceHandler>(mMasterServiceHandler);
      mServer = new TThreadedSelectorServer(new TThreadedSelectorServer
          .Args(new TNonblockingServerSocket(address)).processor(processor)
          .selectorThreads(selectorThreads).acceptQueueSizePerThread(acceptQueueSizePerThreads)
          .workerThreads(workerThreads));

      LOG.info("The master server started @ " + address);
      mServer.serve();
      LOG.info("The master server ended @ " + address);
    } catch (TTransportException e) {
      LOG.error(e.getMessage(), e);
      System.exit(-1);
    } catch (Exception e) {
      LOG.error(e.getMessage(), e);
      System.exit(-1);
    }
  }

  public static synchronized Master createMaster(InetSocketAddress address, int selectorThreads,
      int acceptQueueSizePerThreads, int workerThreads) {
    if (MASTER == null) {
      MASTER = new Master(address, selectorThreads, acceptQueueSizePerThreads, workerThreads);
    }
    return MASTER;
  }

  public static void main(String[] args) {
    if (args.length == 0) {
      Master.createMaster(new InetSocketAddress(Config.MASTER_HOSTNAME, Config.MASTER_PORT),
          Config.MASTER_SELECTOR_THREADS, Config.MASTER_QUEUE_SIZE_PER_SELECTOR,
          Config.MASTER_WORKER_THREADS);
    } else {
      LOG.info("java -cp target/tachyon-1.0-SNAPSHOT-jar-with-dependencies.jar tachyon.Master");
    }
  }
}