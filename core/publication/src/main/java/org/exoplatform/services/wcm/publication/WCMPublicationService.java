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
package org.exoplatform.services.wcm.publication;

import java.util.Map;

import javax.jcr.Node;

import org.exoplatform.services.ecm.publication.NotInPublicationLifecycleException;

/**
 * Created by The eXo Platform SAS
 * Author : Hoa Pham (hoa.pham@exoplatform.com)
 * Sep 29, 2008
 * Modified : Jul 29, 2009 - Benjamin Paillereau (benjamin.paillereau@exoplatform.com)
 *    - added enrollNodeInLifecycle by context
 *    - added updateLifecyleOnChangeContent by context
 */
public interface WCMPublicationService {

  public static final String UPDATE_EVENT = "WCMPublicationService.event.updateState";

 /**
  * Add a Web Publication Plugin to the service.
  * The method caches all added plugins.
  *
  * @param p the plugin to add
  */
 public void addPublicationPlugin(WebpagePublicationPlugin p);

 /**
  * Retrieves all added web page publication plugins.
  * This method is notably used to enumerate possible lifecycles.
  *
  * @return the map of web page publication plugin
  */
  public Map<String, WebpagePublicationPlugin> getWebpagePublicationPlugins();

 /**
  * Checks if is enrolled in wcm lifecycle.
  *
  * @param node the node
  *
  * @return true, if is enrolled in wcm lifecycle
  *
  * @throws NotInPublicationLifecycleException the not in publication lifecycle exception
  * @throws Exception the exception
  */
  public boolean isEnrolledInWCMLifecycle(Node node) throws NotInPublicationLifecycleException,
                                                    Exception;

 /**
  * Enroll in a web page publication lifecycle. The method will be retrieve the
  * web page publication lifecycle by lifecycle name and enroll to lifecycle
  *
  * @param node the node
  * @param lifecycleName the lifecycle name
  *
  * @throws Exception the exception
  */
  public void enrollNodeInLifecycle(Node node, String lifecycleName) throws Exception;

 /**
  * Enroll this node in the default publication lifecycle.
  * Depending on implementation, the default lifecycle could be based on :
  * - lifecycle per site (site provided as a parameter)
  * - lifecycle per author (remoteUser provided as a parameter)
  * - lifecycle per content type (based on the node primary nodetype or its jcr path)
  *
  * @param node
  * @param siteName
  * @param author
  * @throws Exception
  */
  public void enrollNodeInLifecycle(Node node, String siteName, String remoteUser) throws Exception;

 /**
  * Unsubcribe node from a lifecycle plugin. After unsubcribe, the node can enroll to other publication lifecycle
  *
  * @param node the node
  *
  * @throws NotInPublicationLifecycleException the not in publication lifecycle exception
  * @throws Exception the exception
  */
  public void unsubcribeLifecycle(Node node) throws NotInPublicationLifecycleException, Exception;

  /**
   * Called by create and edit listeners. It allows to update the lifecycle of
   * the content depending of its current state.
   *
   * @see org.exoplatform.services.wcm.publication.listener.post.PostCreateContentEventListener
   * @see org.exoplatform.services.wcm.publication.listener.post.PostEditContentEventListener
   * @param node
   * @param currentSite
   * @param remoteUser
   * @throws Exception
   */
  public void updateLifecyleOnChangeContent(Node node, String siteName, String remoteUser) throws Exception;

  /**
   * It allows to update the lifecycle of the content with a new state.
   *
   * @param node
   * @param currentSite
   * @param remoteUser
   * @param newState
   * @throws Exception
   */
  public void updateLifecyleOnChangeContent(Node node,
                                            String siteName,
                                            String remoteUser,
                                            String newState) throws Exception;

  /**
   * returns the current state of the content.
   * We consider the publication:currentState property mandatory in all lifecycles.
   *
   * @param node
   * @return the revision state stored in publication:currentState property
   */
  public String getContentState(Node node) throws Exception ;

}
