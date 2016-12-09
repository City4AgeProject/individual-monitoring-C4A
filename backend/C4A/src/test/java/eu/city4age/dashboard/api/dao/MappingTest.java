package eu.city4age.dashboard.api.dao;
/*******************************************************************************
 * Copyright (c) 2001, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
import org.unitils.orm.hibernate.HibernateUnitils;
import org.unitils.UnitilsJUnit4TestClassRunner;
import org.unitils.spring.annotation.SpringApplicationContext;
import org.junit.Test;
import org.junit.runner.RunWith;

@SpringApplicationContext({"classpath:test-context-dao.xml"})
@RunWith(UnitilsJUnit4TestClassRunner.class)
public class MappingTest {

	@Test
	public void testMappingToDatabase() {
        HibernateUnitils.assertMappingWithDatabaseConsistent();
    }
}
