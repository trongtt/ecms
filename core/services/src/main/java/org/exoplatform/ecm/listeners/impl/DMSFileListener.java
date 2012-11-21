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

import org.exoplatform.ecm.event.impl.EventType;
import org.exoplatform.ecm.model.impl.DMSFile;
import org.exoplatform.services.listener.Event;

/**
 * Created by The eXo Platform SARL
 * Author : Dang Van Minh
 *          minh.dang@exoplatform.com
 * Nov 12, 2012
 * 5:43:51 PM  
 */
public class DMSFileListener extends AbstractEventListener<DMSFile, Integer> {

    @Override
    public void create(Event<DMSFile, Integer> event) {
        System.out.println("\n\nCreating dms file ====" +event.getSource().getPath()+ "\n\n");    
    }

    @Override
    public void update(Event<DMSFile, Integer> event) {
        System.out.println("\n\nUpdating dms file ====" +event.getSource().getPath()+ "\n\n");    
    }

    @Override
    public void remove(Event<DMSFile, Integer> event) {
        System.out.println("\n\nRemoving dms file ====" +event.getSource().getPath()+ "\n\n");    
    }

    @Override
    public void onEvent(Event<DMSFile, Integer> event) throws Exception {
        int eventType = event.getData();
        switch(eventType) {
        case EventType.CREATED :
            create(event);
            break;
        case EventType.REMOVE :
            remove(event);
            break;
        case EventType.UPDATE :
            update(event);
            break;
        default :
            break;
        }
    }

}
