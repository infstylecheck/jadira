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

import javax.time.calendar.YearMonth;
import javax.time.calendar.ZoneOffset;

import org.jadira.usertype.jsr310.columnmapper.StringColumnZoneOffsetMapper;
import org.jadira.usertype.jsr310.spi.AbstractUserType;


/**
 * Maps a {@link YearMonth} to and from String for Hibernate.
 */
public class PersistentZoneOffsetAsString extends AbstractUserType<ZoneOffset, String, StringColumnZoneOffsetMapper> {
}
