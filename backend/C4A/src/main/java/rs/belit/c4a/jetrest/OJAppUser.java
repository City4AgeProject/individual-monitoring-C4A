/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rs.belit.c4a.jetrest;

import javax.ws.rs.Path;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author mnou2
 */
@Path(OJAppUser.PATH)
public class OJAppUser {
    
    public static final String PATH = "OJAppUser";

    @Autowired
    private SessionFactory sessionFactory;
    
}
