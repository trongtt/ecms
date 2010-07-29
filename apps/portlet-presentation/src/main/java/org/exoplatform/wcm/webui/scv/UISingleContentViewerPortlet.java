/*
 * Copyright (C) 2003-2008 eXo Platform SAS.
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
 */
package org.exoplatform.wcm.webui.scv;

import javax.jcr.Node;
import javax.portlet.PortletMode;
import javax.portlet.PortletPreferences;

import org.exoplatform.services.wcm.core.WCMService;
import org.exoplatform.wcm.webui.Utils;
import org.exoplatform.webui.application.WebuiApplication;
import org.exoplatform.webui.application.WebuiRequestContext;
import org.exoplatform.webui.application.portlet.PortletRequestContext;
import org.exoplatform.webui.config.annotation.ComponentConfig;
import org.exoplatform.webui.core.UIPopupContainer;
import org.exoplatform.webui.core.UIPortletApplication;
import org.exoplatform.webui.core.lifecycle.UIApplicationLifecycle;

/**
 * Created by The eXo Platform SAS
 * Author : DANG TAN DUNG
 * dzungdev@gmail.com
 * Jun 9, 2008
 */
@ComponentConfig(
  lifecycle = UIApplicationLifecycle.class,
  template = "app:/groovy/SingleContentViewer/UISingleContentView.gtmpl"
)

public class UISingleContentViewerPortlet extends UIPortletApplication {

  /** The REPOSITORY. */
  public static String REPOSITORY = "repository" ;

  /** The WORKSPACE. */
  public static String WORKSPACE = "workspace" ;

  /** The IDENTIFIER. */
  public static String IDENTIFIER = "nodeIdentifier" ;

  /** The mode_. */
  private PortletMode mode = PortletMode.VIEW ;
  
  public static final String UIPreferencesPopupID = "UIPreferencesPopupWindows";
  
  private UISCVPreferences popPreferences;
  private UIPresentationContainer uiPresentation;
  /**
   * Instantiates a new uI single content viewer portlet.
   * 
   * @throws Exception the exception
   */
  public UISingleContentViewerPortlet() throws Exception {
    addChild(UIPopupContainer.class, null, null);
    popPreferences = addChild(UISCVPreferences.class, null, null).setRendered(false);
    uiPresentation = addChild(UIPresentationContainer.class, null, null);
  }

  /**
   * Activate mode.
   * 
   * @param newMode the mode
   * 
   * @throws Exception the exception
   */
  public void activateMode(PortletMode newMode){
    if(PortletMode.VIEW.equals(newMode)) {
      popPreferences.setRendered(false);
    	uiPresentation.setRendered(true);
    } else if (PortletMode.EDIT.equals(newMode)) {
      uiPresentation.setRendered(false);
      popPreferences.setRendered(true);        
    }
  }
  
  /* (non-Javadoc)
   * @see org.exoplatform.webui.core.UIPortletApplication#processRender(org.exoplatform.webui.application.WebuiApplication, org.exoplatform.webui.application.WebuiRequestContext)
   */
  public void processRender(WebuiApplication app, WebuiRequestContext context) throws Exception {
    PortletRequestContext pContext = (PortletRequestContext) context ;
    PortletMode newMode = pContext.getApplicationMode() ;
    if(!mode.equals(newMode)) {
      activateMode(newMode) ;
      mode = newMode ;
    }
    super.processRender(app, context) ;
  }
  
  public void changeMode(PortletMode newMode) throws Exception{
    if (!newMode.equals((mode))) {
      this.mode = newMode;
      activateMode(newMode) ;
    }
  }
  public Node getNodeByPreference() {
    try {
      PortletRequestContext portletRequestContext = WebuiRequestContext.getCurrentInstance();
      PortletPreferences preferences = portletRequestContext.getRequest().getPreferences();
      String repository = preferences.getValue(REPOSITORY, null);    
      String workspace = preferences.getValue(WORKSPACE, null);
      String nodeIdentifier = preferences.getValue(IDENTIFIER, null) ;
      WCMService wcmService = getApplicationComponent(WCMService.class);
      return wcmService.getReferencedContent(Utils.getSessionProvider(), repository, workspace, nodeIdentifier);
    } catch (Exception e) {
      return null;
    }
  }
    
}