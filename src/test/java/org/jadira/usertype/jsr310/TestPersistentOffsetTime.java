/*
 *  Copyright 2010 Christopher Pheby
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.jadira.usertype.jsr310;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Table;
import javax.time.calendar.LocalTime;
import javax.time.calendar.OffsetTime;
import javax.time.calendar.TimeAdjuster;
import javax.time.calendar.ZoneOffset;


import org.jadira.usertype.jsr310.dbunit.DatabaseCapable;
import org.jadira.usertype.jsr310.testmodel.jsr310.OffsetTimeHolder;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPersistentOffsetTime extends DatabaseCapable {

    private static final OffsetTime[] offsetTimes = new OffsetTime[] { OffsetTime.of(12, 10, 31, ZoneOffset.UTC), OffsetTime.of(23, 7, 43, 120, ZoneOffset.hours(2)) };

    private static final TimeAdjuster NORMALISE_NANOS = new NormaliseNanosAdjuster();
    
    private static EntityManagerFactory factory;

    @BeforeClass
    public static void setup() {
        factory = Persistence.createEntityManagerFactory("test1");
    }

    @AfterClass
    public static void tearDown() {
        factory.close();
    }

    @Test
    public void testPersist() {

        EntityManager manager = factory.createEntityManager();

        manager.getTransaction().begin();

        for (int i = 0; i < offsetTimes.length; i++) {

            OffsetTimeHolder item = new OffsetTimeHolder();
            item.setId(i);
            item.setName("test_" + i);
            item.setOffsetTime(offsetTimes[i]);

            manager.persist(item);
        }

        manager.flush();

        manager.getTransaction().commit();

        manager.close();

        manager = factory.createEntityManager();

        for (int i = 0; i < offsetTimes.length; i++) {

            OffsetTimeHolder item = manager.find(OffsetTimeHolder.class, Long.valueOf(i));

            assertNotNull(item);
            assertEquals(i, item.getId());
            assertEquals("test_" + i, item.getName());
            assertEquals(offsetTimes[i].with(NORMALISE_NANOS), item.getOffsetTime());
        }
        
        verifyDatabaseTable(manager, OffsetTimeHolder.class.getAnnotation(Table.class).name());
        
        manager.close();
    }
       
    private static final class NormaliseNanosAdjuster implements TimeAdjuster {

        public LocalTime adjustTime(LocalTime time) {
            if(time == null) { return null; }
            
            int millis = (int)(time.getNanoOfSecond() / 1000000);
            
            return LocalTime.of(time.getHourOfDay(), time.getMinuteOfHour(), time.getSecondOfMinute(), millis * 1000000);
        }
    }
}
