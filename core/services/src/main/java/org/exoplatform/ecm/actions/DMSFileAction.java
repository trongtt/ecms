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

import javax.jcr.Node;
import javax.jcr.Property;

import org.apache.commons.chain.Context;
import org.exoplatform.ecm.event.api.EventManager;
import org.exoplatform.ecm.model.impl.DMSFile;
import org.exoplatform.services.command.action.Action;
import org.exoplatform.services.ext.action.InvocationContext;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.security.IdentityConstants;
import org.exoplatform.services.wcm.utils.WCMCoreUtils;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 1, 2012
 * 3:54:04 PM  
 */
public class DMSFileAction implements Action {

  @SuppressWarnings("unchecked")
  @Override
  public boolean execute(Context ctx) throws Exception {
    int evt = (Integer)ctx.get(InvocationContext.EVENT);
    Node node = null;
    if(ctx.get("currentItem") instanceof Node) {
      node = (Node)ctx.get("currentItem");
    } else if(ctx.get("currentItem") instanceof Property) {
      node = ((Property)ctx.get("currentItem")).getParent();
    }
    boolean isSystem = false;
    if(node.getSession().getUserID().equals(IdentityConstants.SYSTEM)) {
      isSystem = true; 
    }
    DMSFile dmsFile = new DMSFile(node.getSession().getWorkspace().getName(), node.getPath(), isSystem);
    EventManager<DMSFile, Integer> eventManager = WCMCoreUtils.getService(EventManager.class);
    eventManager.broadcastEvent(new Event<DMSFile, Integer>(dmsFile.getObjectType(), dmsFile, evt));
    return false;
  }

}
