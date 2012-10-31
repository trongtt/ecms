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

import java.util.List;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:46 PM  
 */
public class FolderData extends ObjectData {
  
  public FolderData(String name, String path) {
    super(name, path);
  }
  
  public FolderData(String UUID) {
    super(UUID);
  }
  /**
   * Get type of folder
   * @return Type of folder such as nt:folder or nt:unstructured
   */
  public String getFolderType() {
    return null;
  }
  
  /**
   * Create sub folder
   * @param path Sub folder path.
   * The parameter allows a path such as /test/test1/test2 or just simple test2
   * If test or test1 doesn't exist then it will be created automatically, and finally test2.
   * @return Object FolderData
   */
  public FolderData addSubFolder(String path) {
    return null;
  }
  
  /**
   * Remove sub folder
   * @param path Sub folder path
   */
  public void removeSubFolder(String path) {
    
  }
  
  /**
   * Check to see is there any sub folder inside current folder.
   * @return <code>True</code> if current folder contain sub folder and
   *         <code>otherwise</code>
   */
  public boolean hasSubFolder() {
    return false;
  }
  
  /**
   * Get sub folder form its path
   * @param path Path of sub folder
   * @return The FolderData object
   */
  public FolderData getSubFolder(String path) {
    return null;
  }

  @Override
  public List<?> getChildren() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ObjectData getParent() {
    // TODO Auto-generated method stub
    return null;
  }

}
