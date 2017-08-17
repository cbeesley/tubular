package com.thoughtpeak.tubular.core.runners;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.log4j.Logger;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.thoughtpeak.tubular.core.container.CommonAnalysisStructure;
import com.thoughtpeak.tubular.core.processengine.Pipeline;
import com.thoughtpeak.tubular.core.processengine.PipelineCreationFactory;
import com.thoughtpeak.tubular.core.worklist.BaseWorkItem;
import com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector;

/**
 *
 */
public class BatchPipelineRunner implements CoreRunner {
    
    
    private Logger log = Logger.getLogger(BatchPipelineRunner.class);
    
    private int NUM_WORKERS = 2;
    
    /**
     * when true, tells the runner to wait until the worklist is completed and halting the main
     * execution thread
     */
    private boolean waitToComplete = true;
    
    //TODO - add this into property
    private int batch_size = 999;
    
    /**
     * @see com.thoughtpeak.tubular.core.runners.CoreRunner#execute(com.thoughtpeak.tubular.core.processengine.Pipeline,
     *      com.thoughtpeak.tubular.core.worklist.WorkListDocumentCollector)
     */
    @Override
    public <T extends BaseWorkItem, U> void execute(Pipeline pipeline, final WorkListDocumentCollector<T, U> worklist) {
        
        ListeningExecutorService jobServicePool = MoreExecutors
                .listeningDecorator(Executors.newFixedThreadPool(NUM_WORKERS));
        
        final GenericObjectPool<Pipeline> pipelinePool = new GenericObjectPool<Pipeline>(
                new PipelineCreationFactory(pipeline));
        // mirror the number of worker threads for now
        pipelinePool.setMaxTotal(NUM_WORKERS);
        
        while (!worklist.isComplete()) {
            
            List<T> itemList = new ArrayList<T>();
            
            for (int i = 0; i < this.batch_size && !worklist.isComplete(); i++) {
                itemList.add(worklist.getNext());
            }
            
            worklist.loadDocuments(itemList);
            
            for (final T eachItem : itemList) {
                
                final ListenableFuture<T> future = jobServicePool.submit(new Callable<T>() {
                    
                    
                    @Override
                    public T call() throws Exception {
                        Pipeline pipeline = null;
                        CommonAnalysisStructure bin = null;
                        try {
                            pipeline = pipelinePool.borrowObject();
                            bin = pipeline.executePipeline(eachItem.getDocumentText());
                            
                            worklist.workItemCompleted(bin, eachItem);
                            // Send to results collection
                            
                        } catch (Exception e) {
                            log.error("Error during pipeline processing: ", e);
                            throw new Exception("Error during pipeline processing:", e);
                            
                        } finally {
                            
                            try {
                                if (pipeline != null) {
                                    pipelinePool.returnObject(pipeline);
                                }
                            } catch (Exception e) { // catch and throw anything awry
                                log.error("Error returning pipeline object to job pool", e);
                                throw new Exception("Error returning pipeline object to job pool", e);
                            }
                        }
                        
                        return eachItem;//Return annotations
                    }
                });
                
                /**
                 * Adds a listener to each task which tracks its state and completion
                 */
                future.addListener(new Runnable() {
                    
                    
                    @Override
                    public void run() {
                        try {
                            
                            future.get();
                            
                            // TODO check code here for termination flags, update status counters, etc
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }, MoreExecutors.sameThreadExecutor());
            }
        }
        
        // Tell the job pool to shutdown but not terminate pending tasks
        jobServicePool.shutdown();
        // Monitor loop to determine when the pool is finished
        while (waitToComplete) {
            try {
                Thread.sleep(6000);
            } catch (InterruptedException e) {
                
                e.printStackTrace();
            }
            if (jobServicePool.isTerminated())
                break;
        }
        try {
            worklist.close();
            // call the completed worklist method
            worklist.collectionProcessCompleted();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
}