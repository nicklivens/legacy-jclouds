/**
 *
 * Copyright (C) 2010 Cloud Conscious, LLC. <info@cloudconscious.com>
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.jclouds.openstack.nova.predicates;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.Resource;
import javax.inject.Singleton;

import org.jclouds.logging.Logger;
import org.jclouds.openstack.nova.NovaClient;
import org.jclouds.openstack.nova.domain.Server;
import org.jclouds.openstack.nova.domain.ServerStatus;

import com.google.common.base.Predicate;
import com.google.inject.Inject;

/**
 * 
 * Tests to see if a task succeeds.
 * 
 * @author Adrian Cole
 */
@Singleton
public class ServerDeleted implements Predicate<Server> {

   private final NovaClient client;

   @Resource
   protected Logger logger = Logger.NULL;

   @Inject
   public ServerDeleted(NovaClient client) {
      this.client = client;
   }

   public boolean apply(Server server) {
      logger.trace("looking for state on server %s", checkNotNull(server, "server"));
      server = refresh(server);
      if (server == null)
         return true;
      logger.trace("%s: looking for server state %s: currently: %s", server.getId(),
               ServerStatus.DELETED, server.getStatus());
      return server.getStatus() == ServerStatus.DELETED;
   }

   private Server refresh(Server server) {
      return client.getServer(server.getId());
   }
}
