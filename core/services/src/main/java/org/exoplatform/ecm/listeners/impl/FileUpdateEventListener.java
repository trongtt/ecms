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
package org.exoplatform.ecm.listeners.impl;

import org.exoplatform.ecm.api.model.FileData;
import org.exoplatform.ecm.listener.api.FileListenerPlugin;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 1, 2012
 * 5:24:12 PM  
 */
public class FileUpdateEventListener extends FileListenerPlugin {

  @Override
  public void createFile(FileData fileData) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void updateFile(FileData event) {
    System.out.println("\n\nFileUpdateEventListener executed");
  }

  @Override
  public void removeFile(FileData event) {
    // TODO Auto-generated method stub
    
  }

  
}
