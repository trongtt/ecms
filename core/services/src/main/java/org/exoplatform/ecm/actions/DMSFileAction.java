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
package org.exoplatform.ecm.actions;

import java.util.Iterator;
import java.util.Set;

import javax.jcr.observation.Event;

import org.apache.commons.chain.Context;
import org.exoplatform.ecm.api.model.FileData;
import org.exoplatform.ecm.api.model.FileDataImpl;
import org.exoplatform.ecm.event.api.DMSEventManager;
import org.exoplatform.ecm.listener.api.FileListener;
import org.exoplatform.ecm.listener.api.FileListenerPlugin;
import org.exoplatform.services.command.action.Action;
import org.exoplatform.services.ext.action.InvocationContext;
import org.exoplatform.services.log.ExoLogger;
import org.exoplatform.services.log.Log;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 1, 2012
 * 3:54:04 PM  
 */
public class DMSFileAction implements Action {

  private static final Log LOG = ExoLogger.getLogger(DMSFileAction.class);
  @Override
  public boolean execute(Context ctx) throws Exception {
    DMSEventManager eventManager = WCMCoreUtils.getService(DMSEventManager.class);
    Set<FileListenerPlugin> fileListeners = eventManager.getFileListeners();
    Iterator<FileListenerPlugin> iter = fileListeners.iterator();
    int evt = (Integer)ctx.get(InvocationContext.EVENT);
    FileData fileData = new FileDataImpl("test", "test");
    while(iter.hasNext()) {
      FileListener listener = iter.next();
      switch(evt) {
      case Event.NODE_ADDED :
        listener.createFile(fileData);
        break;
      case Event.NODE_REMOVED :
        listener.removeFile(fileData);
        break;
      case Event.PROPERTY_ADDED :
        listener.updateFile(fileData);
        break;
      case Event.PROPERTY_CHANGED :
        listener.updateFile(fileData);
        break;
      case Event.PROPERTY_REMOVED :
        listener.updateFile(fileData);
        break;
      default:
        break;
      }
    }
    return false;
  }

}
