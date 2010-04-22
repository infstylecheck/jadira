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
package org.jadira.usertype.jsr310.spi;

import org.hibernate.type.NullableType;

public interface ColumnMapper<T, J> {

    public abstract NullableType getHibernateType();

    public abstract int getSqlType();

    public abstract T fromNonNullValue(J value);

    public abstract T fromNonNullString(String s);

    public abstract J toNonNullValue(T value);

    public abstract String toNonNullString(T value);

    public abstract Class<T> returnedClass();

    public abstract Class<J> jdbcClass();

}