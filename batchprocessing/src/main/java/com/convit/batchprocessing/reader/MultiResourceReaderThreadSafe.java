//package com.convit.batchprocessing.reader;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.item.ExecutionContext;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.NonTransientResourceException;
//import org.springframework.batch.item.ParseException;
//import org.springframework.batch.item.UnexpectedInputException;
//import org.springframework.batch.item.file.MultiResourceItemReader;
//import org.springframework.core.io.Resource;
//
//@RequiredArgsConstructor
//public class MultiResourceReaderThreadSafe<T> implements ItemReader<T> {
//
//    private final Object lock = new Object();
//
//    private final MultiResourceItemReader<T> delegate;
//
//    @Override
//    public T read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
//        synchronized (lock) {
//            return delegate.read();
//        }
//    }
//
//    public void setResources(Resource[] resources) {
//        synchronized (lock) {
//            delegate.setResources(resources);
//        }
//    }
//
//    public void open(ExecutionContext executionContext) {
//        synchronized (lock) {
//            delegate.open(executionContext);
//        }
//    }
//
//    public void close() {
//        synchronized (lock) {
//            delegate.close();
//        }
//    }
//}
