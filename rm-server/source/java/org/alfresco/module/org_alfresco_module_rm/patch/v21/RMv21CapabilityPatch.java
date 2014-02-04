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
package org.alfresco.module.org_alfresco_module_rm.patch.v21;

import java.util.Set;

import org.alfresco.error.AlfrescoRuntimeException;
import org.alfresco.module.org_alfresco_module_rm.capability.Capability;
import org.alfresco.module.org_alfresco_module_rm.capability.CapabilityService;
import org.alfresco.module.org_alfresco_module_rm.fileplan.FilePlanService;
import org.alfresco.module.org_alfresco_module_rm.role.FilePlanRoleService;
import org.alfresco.module.org_alfresco_module_rm.role.Role;
import org.alfresco.service.cmr.repository.NodeRef;

/**
 * RM v2.1 patch to updated modified capabilities.
 *
 * @author Roy Wetherall
 * @since 2.1
 */
@SuppressWarnings("deprecation")
public class RMv21CapabilityPatch extends RMv21PatchComponent
{
    /** File plan service */
    private FilePlanService filePlanService;

    /** File plan role service */
    private FilePlanRoleService filePlanRoleService;

    /** Capability service */
    private CapabilityService capabilityService;

    /**
     * @param filePlanRoleService   file plan role service
     */
    public void setFilePlanRoleService(FilePlanRoleService filePlanRoleService)
    {
        this.filePlanRoleService = filePlanRoleService;
    }

    /**
     * @param capabilityService capability service
     */
    public void setCapabilityService(CapabilityService capabilityService)
    {
        this.capabilityService = capabilityService;
    }

    /**
     * @param filePlanService   file plan service
     */
    public void setFilePlanService(FilePlanService filePlanService)
    {
        this.filePlanService = filePlanService;
    }

    /**
     * Helper method to get the file plans
     *
     * @return Set of file plan node references
     */
    protected Set<NodeRef> getFilePlans()
    {
        return filePlanService.getFilePlans();
    }
    
    /**
     * Adds a new capability to the specified roles.
     *
     * @param filePlan          file plan
     * @param capabilityName    capability name
     * @param roles             roles
     */
    protected void addCapability(NodeRef filePlan, String capabilityName, String ... roles)
    {
        Capability capability = capabilityService.getCapability(capabilityName);
        if (capability == null)
        {
            throw new AlfrescoRuntimeException("Unable to bootstrap RMv21 capabilities, because capability " + capabilityName + " does not exist.");
        }

        for (String roleName : roles)
        {
            Role role = filePlanRoleService.getRole(filePlan, roleName);

            if (role != null)
            {
                // get the roles current capabilities
                Set<Capability> capabilities = role.getCapabilities();

                // only update if the capability is missing
                if (capabilities.contains(capability) == false)
                {
                    if (logger.isDebugEnabled() == true)
                    {
                        logger.debug("  ... adding capability " + capabilityName + " to role " + role.getName());
                    }

                    capabilities.add(capability);
                    filePlanRoleService.updateRole(filePlan, role.getName(), role.getDisplayLabel(), capabilities);
                }
            }
        }
    }
    
    /**
     * @see org.alfresco.repo.module.AbstractModuleComponent#executeInternal()
     */
    @Override
    protected void executePatch() throws Throwable
    {
        Set<NodeRef> filePlans = getFilePlans();

        if (logger.isDebugEnabled() == true)
        {
            logger.debug("  ... updating " + filePlans.size() + " file plans");
        }

        for (NodeRef filePlan : filePlans)
        {
            if (logger.isDebugEnabled() == true)
            {
                logger.debug("  ... updating file plan " + filePlan.toString());
            }

            // add new capabilities
            addCapability(filePlan,
                          "CreateRecords",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_POWER_USER,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER,
                          FilePlanRoleService.ROLE_SECURITY_OFFICER);
            addCapability(filePlan,
                          "ManageRules",
                          FilePlanRoleService.ROLE_ADMIN);
            addCapability(filePlan,
                          "RequestRecordInformation",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_POWER_USER,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER,
                          FilePlanRoleService.ROLE_SECURITY_OFFICER);
            addCapability(filePlan,
                          "FileDestructionReport",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER);
            addCapability(filePlan,
                          "RejectRecords",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_POWER_USER,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER,
                          FilePlanRoleService.ROLE_SECURITY_OFFICER);
            addCapability(filePlan,
                          "FileUnfiledRecords",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_POWER_USER,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER,
                          FilePlanRoleService.ROLE_SECURITY_OFFICER);
            addCapability(filePlan,
                          "LinkToRecords",
                          FilePlanRoleService.ROLE_ADMIN,
                          FilePlanRoleService.ROLE_POWER_USER,
                          FilePlanRoleService.ROLE_RECORDS_MANAGER,
                          FilePlanRoleService.ROLE_SECURITY_OFFICER);
        }
    }
}