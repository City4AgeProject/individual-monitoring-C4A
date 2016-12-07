/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.city4age.dashboard.api.ws.jet;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.ext.ContextResolver;

/**
 *
 * @author mnou2
 */
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {
    
    private ObjectMapper mapper;

    public ObjectMapperProvider() {
        mapper = produceMapper();
    }

    @Override
    public ObjectMapper getContext(Class<?> type) {
        return mapper;
    }
    
    public static ObjectMapper produceMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure( SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false );
        mapper.configure( SerializationFeature.INDENT_OUTPUT, true );
        mapper.configure( SerializationFeature.WRITE_NULL_MAP_VALUES, true );
        mapper.setSerializationInclusion(Include.NON_NULL);
        return mapper;
    }
    
    public static String dump(Object toDump) {
        ObjectMapper dumper = ObjectMapperProvider.produceMapper();
        String result = "\n";
        try {
            result += dumper.writeValueAsString(toDump);
        } catch (JsonProcessingException ex) {
            Logger.getLogger(ObjectMapperProvider.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
    
}
