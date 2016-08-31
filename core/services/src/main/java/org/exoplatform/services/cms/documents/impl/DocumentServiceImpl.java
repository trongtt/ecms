/*
 * Copyright (C) 2003-2011 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.cms.documents.impl;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.jcr.Node;
import javax.jcr.RepositoryException;

import org.exoplatform.container.xml.PortalContainerInfo;
import org.exoplatform.portal.config.UserPortalConfig;
import org.exoplatform.portal.config.UserPortalConfigService;
import org.exoplatform.portal.mop.SiteKey;
import org.exoplatform.portal.mop.user.UserNavigation;
import org.exoplatform.portal.mop.user.UserPortalContext;
import org.exoplatform.services.cms.documents.DocumentService;
import org.exoplatform.services.cms.documents.model.Document;
import org.exoplatform.services.cms.drives.DriveData;
import org.exoplatform.services.cms.drives.ManageDriveService;
import org.exoplatform.services.cms.drives.impl.ManageDriveServiceImpl;
import org.exoplatform.services.cms.impl.Utils;
import org.exoplatform.services.jcr.RepositoryService;
import org.exoplatform.services.jcr.core.ManageableRepository;
import org.exoplatform.services.jcr.ext.common.SessionProvider;
import org.exoplatform.services.security.ConversationState;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

/**
 * Created by The eXo Platform SAS Author : eXoPlatform exo@exoplatform.com Mar
 * 22, 2011
 */
public class DocumentServiceImpl implements DocumentService {

  public static final String MIX_REFERENCEABLE = "mix:referenceable";
  public static final String EXO_LAST_MODIFIER_PROP = "exo:lastModifier";
  public static final String EXO_DATE_CREATED_PROP = "exo:dateCreated";
  public static final String JCR_LAST_MODIFIED_PROP = "jcr:lastModified";
  public static final String JCR_CONTENT = "jcr:content";
  public static final String EXO_OWNER_PROP = "exo:owner";
  public static final String EXO_TITLE_PROP = "exo:title";
  public static final String CURRENT_STATE_PROP = "publication:currentState";
  public static final String DOCUMENTS_APP_NAVIGATION_NODE_NAME = "documents";

  private ManageDriveService manageDriveService;

  public DocumentServiceImpl(ManageDriveService manageDriveService) {
    this.manageDriveService = manageDriveService;
  }

  @Override
  public Document findDocById(String documentId) throws RepositoryException {
    RepositoryService repositoryService = WCMCoreUtils.getService(RepositoryService.class);
    ManageableRepository manageRepo = repositoryService.getCurrentRepository();
    SessionProvider sessionProvider = WCMCoreUtils.getUserSessionProvider();

    String ws = documentId.split(":/")[0];
    String uuid = documentId.split(":/")[1];

    Node node = sessionProvider.getSession(ws, manageRepo).getNodeByUUID(uuid);
    // Create Document
    String title = node.hasProperty(EXO_TITLE_PROP) ? node.getProperty(EXO_TITLE_PROP).getString() : "";
    String id = node.isNodeType(MIX_REFERENCEABLE) ? node.getUUID() : "";
    String state = node.hasProperty(CURRENT_STATE_PROP) ? node.getProperty(CURRENT_STATE_PROP).getValue().getString() : "";
    String author = node.hasProperty(EXO_OWNER_PROP) ? node.getProperty(EXO_OWNER_PROP).getString() : "";
    Calendar lastModified = (node.hasNode(JCR_CONTENT) ? node.getNode(JCR_CONTENT)
                                                             .getProperty(JCR_LAST_MODIFIED_PROP)
                                                             .getValue()
                                                             .getDate() : null);
    Calendar dateCreated = (node.hasProperty(EXO_DATE_CREATED_PROP) ? node.getProperty(EXO_DATE_CREATED_PROP)
                                                                          .getValue()
                                                                          .getDate()
                                                                   : null);
    String lastEditor = (node.hasProperty(EXO_LAST_MODIFIER_PROP) ? node.getProperty(EXO_LAST_MODIFIER_PROP)
                                                                        .getValue()
                                                                        .getString()
                                                                 : "");
    Document doc = new Document(id, node.getName(), title, node.getPath(), 
                                ws, state, author, lastEditor, lastModified, dateCreated);
    return doc;
  }

  /**
   * Get link to open a document in the Documents application.
   * This method will try to guess what is the best drive to use based on the node path.
   * @param nodePath path of the nt:file node to open
   * @return Link to open the document
   * @throws Exception
   */
  @Override
  public String getLinkInDocumentsApp(String nodePath) throws Exception {
    if(nodePath == null) {
      return null;
    }

    // find the best matching drive to display the document
    DriveData drive = this.getDriveOfNode(nodePath);

    return getLinkInDocumentsApp(nodePath, drive);
  }

  /**
   * Get link to open a document in the Documents application with the given drive
   * @param nodePath path of the nt:file node to open
   * @param drive drive to use to open the nt:file node
   * @return Link to open the document
   * @throws Exception
   */
  @Override
  public String getLinkInDocumentsApp(String nodePath, DriveData drive) throws Exception {
    if(nodePath == null) {
      return null;
    }

    String containerName = WCMCoreUtils.getService(PortalContainerInfo.class).getContainerName();
    StringBuffer url = new StringBuffer();
    url.append("/").append(containerName);

    if(drive.getName().equals(ManageDriveServiceImpl.GROUPS_DRIVE_NAME)) {
      // handle group drive case
      String groupId = drive.getParameters().get(ManageDriveServiceImpl.DRIVE_PARAMATER_GROUP_ID);
      if(groupId != null) {
        String groupPageName;
        String[] splitedGroupId = groupId.split("/");
        if (splitedGroupId != null && splitedGroupId.length == 3 && splitedGroupId[1].equals("spaces")) {
          // the doc is in a space -> we use the documents application of the space
          groupPageName = splitedGroupId[2] + "/" + DOCUMENTS_APP_NAVIGATION_NODE_NAME;
        } else {
          // otherwise we use the portal documents application
          groupPageName = DOCUMENTS_APP_NAVIGATION_NODE_NAME;
        }
        url.append("/g/").append(groupId.replaceAll("/", ":")).append("/").append(groupPageName)
                .append("?path=").append(drive.getName()).append(nodePath)
                .append("&").append(ManageDriveServiceImpl.DRIVE_PARAMATER_GROUP_ID).append("=").append(groupId);
      } else {
        throw new Exception("Cannot get group id from node path " + nodePath);
      }
    } else if(drive.getName().equals(ManageDriveServiceImpl.USER_DRIVE_NAME)
            || drive.getName().equals(ManageDriveServiceImpl.PERSONAL_DRIVE_NAME)) {
      // handle personal drive case
      SiteKey siteKey = getDefaultSiteKey();
      url.append("/").append(siteKey.getName()).append("/").append(DOCUMENTS_APP_NAVIGATION_NODE_NAME)
              .append("?path=" + drive.getName() + nodePath);
      String[] splitedNodePath = nodePath.split("/");
      if(splitedNodePath != null && splitedNodePath.length >= 6) {
        String userId = splitedNodePath[5];
        url.append("&").append(ManageDriveServiceImpl.DRIVE_PARAMATER_USER_ID).append("=").append(userId);
      }
    } else {
      // default case
      SiteKey siteKey = getDefaultSiteKey();
      url.append("/").append(siteKey.getName()).append("/").append(DOCUMENTS_APP_NAVIGATION_NODE_NAME)
              .append("?path=" + drive.getName() + nodePath);
    }

    return url.toString();
  }

  @Override
  public DriveData getDriveOfNode(String nodePath) throws Exception {
    return getDriveOfNode(nodePath, ConversationState.getCurrent().getIdentity().getUserId(), Utils.getMemberships());
  }

  @Override
  public DriveData getDriveOfNode(String nodePath, String userId, List<String> memberships) throws Exception {
    DriveData nodeDrive = null;
    List<DriveData> drives = manageDriveService.getDriveByUserRoles(userId, memberships);

    // Manage special cases
    String[] splitedPath = nodePath.split("/");
    if (splitedPath != null && splitedPath.length >= 2) {
      if (splitedPath[1].equals(ManageDriveServiceImpl.GROUPS_DRIVE_ROOT_NODE)) {
        nodeDrive = manageDriveService.getGroupDriveTemplate();

        int groupDocumentsRootNodeName = nodePath.indexOf("/Documents");
        if(groupDocumentsRootNodeName >= 0) {
          // extract group id for doc path
          String groupId = nodePath.substring(ManageDriveServiceImpl.GROUPS_DRIVE_ROOT_NODE.length() + 1, groupDocumentsRootNodeName);
          nodeDrive.getParameters().put(ManageDriveServiceImpl.DRIVE_PARAMATER_GROUP_ID, groupId);
        } else {
          throw new Exception("Cannot extract group id from node path " + nodePath);
        }
      } else if (splitedPath != null && splitedPath.length >= 6 && splitedPath[1].equals(ManageDriveServiceImpl.PERSONAL_DRIVE_ROOT_NODE)) {
        if(splitedPath[5].equals(userId)) {
          nodeDrive = manageDriveService.getDriveByName(ManageDriveServiceImpl.PERSONAL_DRIVE_NAME);
        } else {
          nodeDrive = manageDriveService.getDriveByName(ManageDriveServiceImpl.USER_DRIVE_NAME);
        }
        nodeDrive.getParameters().put(ManageDriveServiceImpl.DRIVE_PARAMATER_USER_ID, splitedPath[2] + "/" + splitedPath[3] + "/" + splitedPath[4] + "/" + splitedPath[5]);
      }
    }
    if(nodeDrive == null) {
      for (DriveData drive : drives) {
        if (nodePath.startsWith(drive.getHomePath())) {
          if (nodeDrive == null || nodeDrive.getHomePath().length() < drive.getHomePath().length()) {
            nodeDrive = drive;
          }
        }
      }
    }
    return nodeDrive;
  }


  protected UserPortalConfig getDefaultUserPortalConfig() throws Exception {
    UserPortalConfigService userPortalConfigSer = WCMCoreUtils.getService(UserPortalConfigService.class);
    UserPortalContext NULL_CONTEXT = new UserPortalContext() {
      public ResourceBundle getBundle(UserNavigation navigation) {
        return null;
      }
      public Locale getUserLocale() {
        return Locale.ENGLISH;
      }
    };
    String remoteId = ConversationState.getCurrent().getIdentity().getUserId() ;
    UserPortalConfig userPortalCfg = userPortalConfigSer.
            getUserPortalConfig(userPortalConfigSer.getDefaultPortal(), remoteId, NULL_CONTEXT);
    return userPortalCfg;
  }

  protected SiteKey getDefaultSiteKey() throws Exception {
    UserPortalConfig prc = getDefaultUserPortalConfig();
    if (prc == null) {
      return null;
    }
    SiteKey siteKey = SiteKey.portal(prc.getPortalConfig().getName());
    return siteKey;
  }
}
