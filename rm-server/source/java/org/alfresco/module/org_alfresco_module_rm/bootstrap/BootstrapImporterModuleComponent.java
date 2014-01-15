/*
 * Copyright (C) 2005-2011 Alfresco Software Limited.
 *
 * This file is part of Alfresco
 *
 * Alfresco is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Alfresco is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with Alfresco. If not, see <http://www.gnu.org/licenses/>.
 */
package org.alfresco.module.org_alfresco_module_rm.bootstrap;

import org.alfresco.module.org_alfresco_module_rm.patch.ModulePatchExecuter;
import org.alfresco.repo.module.ImporterModuleComponent;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;

/**
 * Custom implementation of module component importer
 * 
 * @author Roy Wetherall
 * @since 2.0
 */
public class BootstrapImporterModuleComponent extends ImporterModuleComponent
{
    /** root config nore id */
    private static final String CONFIG_NODEID = "rm_config_folder";
    
    /** node service */
    private NodeService nodeService;
    
    /** module patch executer */
    private ModulePatchExecuter modulePatchExecuter;
    
    /**
     * @param nodeService   node service
     */
    public void setNodeService(NodeService nodeService)
    {
        this.nodeService = nodeService;
    }
    
    /**
     * @param modulePatchExecuter   module patch executer
     */
    public void setModulePatchExecuter(ModulePatchExecuter modulePatchExecuter)
    {
        this.modulePatchExecuter = modulePatchExecuter;
    }
    
    /**
     * Need to check whether this module has already been executed.
     * 
     * @see org.alfresco.repo.module.ImporterModuleComponent#executeInternal()
     */
    @Override
    protected void executeInternal() throws Throwable
    {
        try
        {
            NodeRef nodeRef = new NodeRef(StoreRef.STORE_REF_WORKSPACE_SPACESSTORE, CONFIG_NODEID); 
            if (nodeService.exists(nodeRef) == false)
            {
                // execute internal
                super.executeInternal();
                
                System.out.println("Initialising schema version from config value");
                
                // initialise the schema version (based on current configured value)
                modulePatchExecuter.initSchemaVersion();
            }
        }
        catch (Throwable exception)
        {
            exception.printStackTrace();
            throw exception;
        }
    }    
}
