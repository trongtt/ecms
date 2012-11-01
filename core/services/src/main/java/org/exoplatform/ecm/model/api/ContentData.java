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
 * 3:26:19 PM  
 */
public class ContentData extends ObjectData{

  public ContentData(String name, String path) {
    super(name, path);
  }
  
  public ContentData(String UUID) {
    super(UUID);
  }
  /**
   * Check the type of content
   * @return <code>true</code> if current node is document type and
    *         <code>otherwise</code>
   */
  public boolean isDocument() {
    return false;
  }
  
  /**
   * Get document type
   * @return Type of document such as exo:webContent or exo:accessibleMedia...
   */
  public String getDocumentType() {
    return null;
  }
  
  /**
   * 
   * @return
   */
  public String getDocumentLabel() {
    return null;
  }
  
  /**
   * 
   * @return
   */
  public String getTemplate() {
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
