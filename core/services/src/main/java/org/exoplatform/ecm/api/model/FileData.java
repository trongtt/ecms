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
package org.exoplatform.ecm.api.model;

import java.io.InputStream;
import java.util.List;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Oct 25, 2012
 * 3:26:30 PM  
 */
public class FileData extends ObjectData {

  public FileData(String workspace, String path) {
    super(workspace, path);
  }
  /**
   * 
   * @return
   */
  public InputStream getData() {
    return null;
  }
  
  public String getMimeType() {
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
