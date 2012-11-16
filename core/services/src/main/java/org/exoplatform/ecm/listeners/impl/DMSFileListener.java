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

import org.exoplatform.ecm.listener.api.EventListener;
import org.exoplatform.ecm.model.api.EventType;
import org.exoplatform.ecm.model.impl.DMSFile;
import org.exoplatform.services.listener.Event;
import org.exoplatform.services.listener.Listener;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 12, 2012
 * 5:43:51 PM  
 */
public class DMSFileListener extends Listener<DMSFile, Integer> implements EventListener<DMSFile> {

  @Override
  public void create(DMSFile file) {
    System.out.println("\n\nCreating dms file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void update(DMSFile file) {
    System.out.println("\n\nUpdating dms file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void remove(DMSFile file) {
    System.out.println("\n\nRemoving dms file ====" +file.getPath()+ "\n\n");    
  }

  @Override
  public void onEvent(Event<DMSFile, Integer> event) throws Exception {
    DMSFile dmsFile = event.getSource();
    int eventType = event.getData();
    switch(eventType) {
    case EventType.CREATED :
      create(dmsFile);
      break;
    case EventType.REMOVE :
      remove(dmsFile);
      break;
    case EventType.UPDATE :
      update(dmsFile);
      break;
    default :
      break;
    }
  }

}
