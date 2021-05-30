package com.test.ingestion.service;

import com.test.ingestion.model.FieldRows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

@Component
public class FlexibleFieldSetMapper implements FieldSetMapper<FieldRows> {

    private static final Logger logger = LoggerFactory.getLogger(FlexibleFieldSetMapper.class);
    @Override
    public FieldRows mapFieldSet(FieldSet fieldSet) throws BindException {
        logger.info("Received for reading");
        String[] colNames = fieldSet.getNames();
        Map<String, String> kvs = new HashMap<String, String>();

        for (String colName : colNames) {
            logger.info("Received {}",colNames);
            kvs.put(colName, fieldSet.readString(colName));
        }

        return new FieldRows(kvs);
    }
}
