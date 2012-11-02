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
package org.exoplatform.ecm.event.impl;

import java.util.HashSet;
import java.util.Set;

import org.exoplatform.ecm.event.DMSEventManager;
import org.exoplatform.ecm.listeners.FileListenerPlugin;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 2, 2012
 * 12:23:12 AM  
 */
public class DMSEventManagerImpl implements DMSEventManager {

  protected Set<FileListenerPlugin> fileListeners = new HashSet<FileListenerPlugin>();

  public DMSEventManagerImpl() {
  }
  
  public void addFileListener(FileListenerPlugin listener) {
    fileListeners.add(listener);
  }

  public void removeFileListener(FileListenerPlugin listener) {
    fileListeners.remove(listener);
  }

  public Set<FileListenerPlugin> getFileListeners() {
    return fileListeners;
  }
}
