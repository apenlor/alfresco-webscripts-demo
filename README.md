# alfresco-webscripts-demo

Includes two examples used in training courses about the development and extension of the Alfresco document manager. The aspects, metadata or references to types used are all fictitious.

## java-amp-alfrescosdk3
Example of AMP for Alfresco with JAVA webscripts. It uses [Alfresco SDK 3] (https://docs.alfresco.com/5.2/concepts/sdk-intro.html).

Project allows to deploy an instance of Alfresco with the amp already installed. The amp includes three Java webscript
 - **helloWorld** 
 - **crearDocumento** 
 - **getDocumento**

## javascript-webscripts
It includes three webscripts developed with javascript. For the deployment in the document manager they should simply be included in the folder:

 - **Data Dictionary > Web Scripts**

Included scripts allow:
- **delete-node** (includes log in user home)
- **search-document-title**
- **update-content** (access through post and get)


