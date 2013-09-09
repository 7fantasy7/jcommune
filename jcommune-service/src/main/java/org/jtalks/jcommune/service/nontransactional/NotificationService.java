/**
 * Copyright (C) 2011  JTalks.org Team
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package org.jtalks.jcommune.service.nontransactional;

import org.jtalks.jcommune.model.entity.JCUser;
import org.jtalks.jcommune.model.entity.JCommuneProperty;
import org.jtalks.jcommune.model.entity.SubscriptionAwareEntity;
import org.jtalks.jcommune.model.entity.Topic;
import org.jtalks.jcommune.service.SubscriptionService;
import org.jtalks.jcommune.service.UserService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Send email notifications to the users subscribed.
 * If the update author is subscribed he won't get the notification message.
 * This service also assumes, that topic update as a enclosing branch update as well.
 * <p/>
 * Errors occurred while sending emails are suppressed (logged only) as updates
 * notifications are themselves a kind of a side effect, so they should not prevent
 * the whole operation from being completed.
 *
 * @author Evgeniy Naumenko
 * @author Vitaliy Kravchenko
 */
public class NotificationService {

    private UserService userService;
    private MailService mailService;
    SubscriptionService subscriptionService;
    private JCommuneProperty notificationsEnabledProperty;

    /**
     * @param userService to determine the update author
     * @param mailService     to perform actual email notifications
     * @param notificationsEnabledProperty lets us know whether we can send notifications
     */
    public NotificationService(
            UserService userService,
            MailService mailService,
            SubscriptionService subscriptionService,
            JCommuneProperty notificationsEnabledProperty) {
        this.userService = userService;
        this.mailService = mailService;
        this.subscriptionService = subscriptionService;
        this.notificationsEnabledProperty = notificationsEnabledProperty;
    }

    /**
     * Notifies subscribers about subscribed entity updates by email.
     * If mailing failed this implementation simply continues
     * with other subscribers.
     *
     * @param entity changed subscribed entity.
     */
    public void subscribedEntityChanged(SubscriptionAwareEntity entity) {
        if (notificationsEnabledProperty.booleanValue()) {
            JCUser current = userService.getCurrentUser();
            Collection<JCUser> subscribers = subscriptionService.getAllowedSubscribers(entity);
            for (JCUser user : subscribers) {
                if (!user.equals(current)) {
                    mailService.sendUpdatesOnSubscription(user, entity);
                }
            }
        }
    }

    /**
     * Notifies topic starter by email that his or her topic
     * was moved to another sections and also notifies all branch
     * subscribers
     *
     * @param topic   topic moved
     * @param topicId topic id
     */
    public void topicMoved(Topic topic, long topicId) {
        if (notificationsEnabledProperty.booleanValue()) {

            JCUser currentUser = userService.getCurrentUser();
            JCUser topicStarter = topic.getTopicStarter();

            //send notification to branch subscribers
            Set<JCUser> branchSubscribers = new HashSet<JCUser>(topic.getBranch().getSubscribers());

            // temp transient collection modification to ease the iteration
            branchSubscribers.add(topicStarter);
            branchSubscribers.remove(currentUser);
            for (JCUser subscriber : branchSubscribers) {
                mailService.sendTopicMovedMail(subscriber, topicId);
            }

            //send notification to topic's subscribers
            Collection<JCUser> topicSubscribers = subscriptionService.getAllowedSubscribers(topic);
            for (JCUser subscriber : topicSubscribers) {
                mailService.sendTopicMovedMail(subscriber, topicId);
            }
        }
    }

    /**
     * Send notification to subscribers about removing topic or code review.
     *
     * @param entity Current topic
     * @param subscribers Collection of subscribers
     */
    public void sendNotificationAboutRemovingTopic(Topic entity, Collection<JCUser> subscribers) {
        if (notificationsEnabledProperty.booleanValue()) {
            for (JCUser subscriber : subscribers) {
                mailService.sendRemovingTopicMail(subscriber, entity);
            }
        }
    }
}

