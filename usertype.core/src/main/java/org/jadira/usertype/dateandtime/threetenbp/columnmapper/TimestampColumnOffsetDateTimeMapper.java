/*
 *  Copyright 2010, 2011 Christopher Pheby
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
package org.jadira.usertype.dateandtime.threetenbp.columnmapper;

import static org.jadira.usertype.dateandtime.threetenbp.utils.ZoneHelper.getDefaultZoneId;

import java.sql.Timestamp;

import org.jadira.usertype.spi.shared.DatabaseZoneConfigured;
import org.jadira.usertype.spi.shared.JavaZoneConfigured;
import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;
import org.threeten.bp.format.DateTimeFormatterBuilder;
import org.threeten.bp.temporal.ChronoField;

/**
 * Maps a precise datetime column for storage. The UTC Zone will be used to store the value
 * @deprecated Jadira now depends on Java 8 so you are recommended to switch to the threeten package types
 */
@Deprecated
public class TimestampColumnOffsetDateTimeMapper extends AbstractTimestampThreeTenBPColumnMapper<OffsetDateTime> implements DatabaseZoneConfigured<ZoneId>, JavaZoneConfigured<ZoneId> {

    private static final long serialVersionUID = -7670411089210984705L;

    public static final DateTimeFormatter DATETIME_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, false).toFormatter();

	private static final int MILLIS_IN_SECOND = 1000;

    private ZoneId javaZone = null;

	public TimestampColumnOffsetDateTimeMapper() {
		super();
	}

	public TimestampColumnOffsetDateTimeMapper(ZoneId databaseZone, ZoneId javaZone) {
		super(databaseZone);
		this.javaZone = javaZone;
	}
    
    @Override
    public OffsetDateTime fromNonNullString(String s) {
        return OffsetDateTime.parse(s);
    }

    @Override
    public OffsetDateTime fromNonNullValue(Timestamp value) {

        ZoneId currentDatabaseZone = getDatabaseZone() == null ? getDefaultZoneId() : getDatabaseZone();
        ZoneId currentJavaZone = javaZone == null ? getDefaultZoneId() : javaZone;

        OffsetDateTime dateTime = OffsetDateTime.ofInstant(Instant.ofEpochMilli(value.getTime()), currentDatabaseZone);
        dateTime = dateTime.with(ChronoField.NANO_OF_SECOND, value.getNanos()).atZoneSameInstant(currentJavaZone).toOffsetDateTime();
        
        return dateTime;
    }

    @Override
    public String toNonNullString(OffsetDateTime value) {
        return value.toString();
    }

    @Override
    public Timestamp toNonNullValue(OffsetDateTime value) {
        
        final Timestamp timestamp = new Timestamp((value.toEpochSecond() * MILLIS_IN_SECOND));
        timestamp.setNanos(value.getNano());
        return timestamp;
    }
    
    @Override
    public void setJavaZone(ZoneId javaZone) {
        this.javaZone = javaZone;
    }
        
	@Override
	public ZoneId parseZone(String zoneString) {
		return ZoneId.of(zoneString);
	}
}
