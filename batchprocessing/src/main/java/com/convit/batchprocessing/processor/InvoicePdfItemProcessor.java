//package com.convit.batchprocessing.processor;
//
//import com.convit.batchprocessing.dto.VehiclePdfDTO;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.core.io.Resource;
//import org.springframework.stereotype.Component;
//
//@Slf4j
//@Component
//public class InvoicePdfItemProcessor implements ItemProcessor<Resource, VehiclePdfDTO> {
//
//    @Override
//    public VehiclePdfDTO process(Resource item) throws Exception {
//        log.info("===============> Processing the: {}", item);
//
//        return new VehiclePdfDTO(item.getFilename(), item.getFile());
//    }
//}
