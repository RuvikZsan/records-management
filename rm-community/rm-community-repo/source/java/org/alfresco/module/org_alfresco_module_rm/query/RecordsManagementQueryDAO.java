/*
 * #%L
 * Alfresco Records Management Module
 * %%
 * Copyright (C) 2005 - 2017 Alfresco Software Limited
 * %%
 * This file is part of the Alfresco software.
 * -
 * If the software was purchased under a paid Alfresco license, the terms of
 * the paid license agreement will prevail.  Otherwise, the software is
 * provided under the following open source license terms:
 * -
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * -
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * -
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

package org.alfresco.module.org_alfresco_module_rm.query;

import java.util.Collection;

import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;

/**
 * Records management query DAO
 * 
 * NOTE:  a place holder that can be extended later when we want to enhance performance with canned queries.
 * 
 * @author Roy Wetherall
 * @since 2.1
 */
public interface RecordsManagementQueryDAO
{
    /**
     * Get the number of objects with the given identifier value.
     * 
     * Note:  this is provided as an example and is not currently used
     * 
     * @param identifierValue   id value
     * @return int  count
     */
    int getCountRmaIdentifier(String identifierValue);

    /**
     * Returns whether a given node contains children with one of the given values for the given property
     *
     * @param parent         the parent to evaluate
     * @param property       the QName of the property to evaluate
     * @param propertyValues the list of values to look for
     * @return true if there is at least one child with one of the values from the list set on the given property
     * false otherwise
     */
    public boolean hasChildrenWithPropertyValues(NodeRef parent, QName property, Collection propertyValues);
}
