/*
 * City4Age Project
 * Horizon 2020  * 
 */
package eu.city4age.dashboard.api.service;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * 
 */
@javax.ws.rs.ApplicationPath("v1")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<Class<?>>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(eu.city4age.dashboard.api.ws.AssessmentsService.class);
        resources.add(eu.city4age.dashboard.api.ws.jet.CORSResponseFilter.class);
        resources.add(eu.city4age.dashboard.api.ws.jet.OJAnnotation.class);
        resources.add(eu.city4age.dashboard.api.ws.jet.OJCodeBook.class);
        resources.add(eu.city4age.dashboard.api.ws.jet.OJDataSet.class);
    }
    
}
