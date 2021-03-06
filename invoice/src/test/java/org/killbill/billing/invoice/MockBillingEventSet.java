/*
 * Copyright 2010-2013 Ning, Inc.
 *
 * Ning licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.killbill.billing.invoice;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.UUID;

import org.killbill.billing.callcontext.InternalTenantContext;
import org.killbill.billing.catalog.api.BillingMode;
import org.killbill.billing.catalog.api.Usage;
import org.killbill.billing.junction.BillingEvent;
import org.killbill.billing.junction.BillingEventSet;
import org.killbill.billing.util.AccountDateAndTimeZoneContext;
import org.killbill.billing.util.timezone.DefaultAccountDateAndTimeZoneContext;

public class MockBillingEventSet extends TreeSet<BillingEvent> implements BillingEventSet {

    private static final long serialVersionUID = 1L;

    private final InternalTenantContext internalTenantContext;

    private boolean isAccountInvoiceOff;
    private List<UUID> subscriptionIdsWithAutoInvoiceOff;
    private AccountDateAndTimeZoneContext accountDateAndTimeZoneContext;

    public MockBillingEventSet(final InternalTenantContext internalTenantContext) {
        super();
        this.internalTenantContext = internalTenantContext;
        this.isAccountInvoiceOff = false;
        this.subscriptionIdsWithAutoInvoiceOff = new ArrayList<UUID>();
    }

    @Override
    public boolean add(final BillingEvent e) {
        if (accountDateAndTimeZoneContext == null) {
            this.accountDateAndTimeZoneContext = new DefaultAccountDateAndTimeZoneContext(e.getEffectiveDate(), internalTenantContext);
        }
        return super.add(e);
    }

    @Override
    public boolean addAll(final Collection<? extends BillingEvent> all) {
        if (accountDateAndTimeZoneContext == null) {
            this.accountDateAndTimeZoneContext = new DefaultAccountDateAndTimeZoneContext(all.iterator().next().getEffectiveDate(), internalTenantContext);
        }
        return super.addAll(all);
    }


    public void addSubscriptionWithAutoInvoiceOff(final UUID subscriptionId) {
        subscriptionIdsWithAutoInvoiceOff.add(subscriptionId);
    }


    @Override
    public boolean isAccountAutoInvoiceOff() {
        return isAccountInvoiceOff;
    }

    @Override
    public BillingMode getRecurringBillingMode() {
        return BillingMode.IN_ADVANCE;
    }

    @Override
    public List<UUID> getSubscriptionIdsWithAutoInvoiceOff() {
        return subscriptionIdsWithAutoInvoiceOff;
    }

    @Override
    public AccountDateAndTimeZoneContext getAccountDateAndTimeZoneContext() {
        return accountDateAndTimeZoneContext;
    }

    @Override
    public Map<String, Usage> getUsages() {
        return Collections.emptyMap();
    }

    public void setAccountInvoiceOff(final boolean isAccountInvoiceOff) {
        this.isAccountInvoiceOff = isAccountInvoiceOff;
    }

    public void setSubscriptionIdsWithAutoInvoiceOff(final List<UUID> subscriptionIdsWithAutoInvoiceOff) {
        this.subscriptionIdsWithAutoInvoiceOff = subscriptionIdsWithAutoInvoiceOff;
    }

    public void clearSubscriptionsWithAutoInvoiceOff() {
        subscriptionIdsWithAutoInvoiceOff.clear();
    }
}
