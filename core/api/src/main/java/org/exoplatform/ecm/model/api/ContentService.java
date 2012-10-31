/***************************************************************************
 * Copyright (C) 2003-2009 eXo Platform SAS.
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
 *
 **************************************************************************/
package org.exoplatform.ecm.model.api;


/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 29, 2012
 * 2:37:11 PM  
 */
/**
 * This service used to manage all the operations related to contents such as 
 * create/edit/delete documents/folders and so on 
 *
 */
public interface ContentService {

  /**
   * Create new folder with the given folder object
   * @param folder FolderData object
   */
  public void createFolder(FolderData folderObj);
  
  /**
   * Modify folder informations
   * @param folderObj FolderData object
   */
  public void updateFolder(FolderData folderObj);
  

  /**
   * Create new document with the given content object
   * @param contentObj ContentData object which contains all informations of content
   */
  public ContentData createDocument(ContentData contentObj);

  /**
   * Modify document content
   * @param contentObj ContentData object
   */
  public ContentData updateDocument(ContentData contentObj);


  /**
   * Copy content to new destination
   * @param fromPath
   * @param destPath
   */
  public void copyContent(String fromPath, String destPath);

  /**
   * Remove content
   * @param node Node to remove
   */
  public void removeContent(ObjectData baseObj);

  /**
   * Remove document/folder based on its jcr path
   * @param nodePath JCR path which point to node
   * @param workspace The workspace name
   */
  public void removeContent(String nodePath, String workspace);

}
