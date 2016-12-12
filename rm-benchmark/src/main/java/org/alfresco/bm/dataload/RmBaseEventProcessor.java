package org.alfresco.bm.dataload;

import static org.apache.commons.lang3.StringUtils.EMPTY;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.alfresco.bm.cm.FileFolderService;
import org.alfresco.bm.cm.FolderData;
import org.alfresco.bm.event.AbstractEventProcessor;
import org.alfresco.rest.rm.community.model.fileplancomponents.FilePlanComponent;
import org.alfresco.rest.rm.community.model.fileplancomponents.FilePlanComponentProperties;
import org.alfresco.rest.rm.community.requests.FilePlanComponentAPI;

public abstract class RmBaseEventProcessor extends AbstractEventProcessor implements RMEventConstants
{
    protected FileFolderService fileFolderService;

    public void setFileFolderService(FileFolderService fileFolderService)
    {
        this.fileFolderService = fileFolderService;
    }

    protected int calculateRequiredFilePlanComponentNumber(int average, int standardDeviation)
    {
        if (standardDeviation == 0) return average;
        Random randomGenerator = new Random();
        int absoluteDeviation = randomGenerator.nextInt(standardDeviation + 1);
        float coinFlip = randomGenerator.nextFloat();
        int currentDeviation = (coinFlip > 0.5) ? absoluteDeviation : (0 - absoluteDeviation);
        return (average + currentDeviation);
    }

    public void createFilePlanComponent(FolderData folder, FilePlanComponentAPI api,
                FilePlanComponent parentFilePlanComponent, int componentsToCreate, String nameIdentifier, String type, String context,
                long loadFilePlanComponentDelay) throws Exception // to-check: type of exception
    {
        String unique;

        String folderPath = folder.getPath();
        for (int i = 0; i < componentsToCreate; i++)
        {
            unique = UUID.randomUUID().toString();
            String newfilePlanComponentName = nameIdentifier + unique;
            String newfilePlanComponentTitle = "title: " + newfilePlanComponentName;

            try
            {
                api.setParameters("include=path");

                // Build filePlan component records folder properties
                FilePlanComponent filePlanComponentModel = FilePlanComponent.builder()
                    .name(newfilePlanComponentName)
                    .nodeType(type)
                    .properties(FilePlanComponentProperties.builder()
                            .title(newfilePlanComponentTitle)
                            .description(EMPTY)
                            .build())
                    .build();

                FilePlanComponent filePlanComponent = api.createFilePlanComponent(filePlanComponentModel, parentFilePlanComponent.getId());

                String newfilePlanComponentId = filePlanComponent.getId();
                fileFolderService.createNewFolder(newfilePlanComponentId, context,
                            folderPath + "/" + newfilePlanComponentName);
                TimeUnit.MILLISECONDS.sleep(loadFilePlanComponentDelay);
            }
            catch (Exception e)
            {
                throw e;
            }
        }

        // Increment counts
        fileFolderService.incrementFolderCount(folder.getContext(), folderPath, componentsToCreate);
    }

}