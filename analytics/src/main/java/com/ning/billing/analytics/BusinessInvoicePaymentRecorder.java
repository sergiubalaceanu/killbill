/*
 * Copyright 2010-2012 Ning, Inc.
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

package com.ning.billing.analytics;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.UUID;

import org.skife.jdbi.v2.Transaction;
import org.skife.jdbi.v2.TransactionStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ning.billing.account.api.Account;
import com.ning.billing.account.api.AccountApiException;
import com.ning.billing.account.api.AccountUserApi;
import com.ning.billing.analytics.dao.BusinessAccountSqlDao;
import com.ning.billing.analytics.dao.BusinessInvoicePaymentSqlDao;
import com.ning.billing.analytics.dao.BusinessInvoiceSqlDao;
import com.ning.billing.analytics.model.BusinessInvoicePayment;
import com.ning.billing.payment.api.Payment;
import com.ning.billing.payment.api.PaymentApi;
import com.ning.billing.payment.api.PaymentApiException;
import com.ning.billing.payment.api.PaymentMethod;
import com.ning.billing.payment.api.PaymentMethodPlugin;
import com.ning.billing.util.clock.Clock;

public class BusinessInvoicePaymentRecorder {
    private static final Logger log = LoggerFactory.getLogger(BusinessInvoicePaymentRecorder.class);

    private final BusinessInvoicePaymentSqlDao invoicePaymentSqlDao;
    private final AccountUserApi accountApi;
    private final PaymentApi paymentApi;
    private final Clock clock;
    private final BusinessInvoiceRecorder invoiceRecorder;
    private final BusinessAccountRecorder accountRecorder;

    @Inject
    public BusinessInvoicePaymentRecorder(final BusinessInvoicePaymentSqlDao invoicePaymentSqlDao, final AccountUserApi accountApi,
                                          final PaymentApi paymentApi, final Clock clock, final BusinessInvoiceRecorder invoiceRecorder,
                                          final BusinessAccountRecorder accountRecorder) {
        this.invoicePaymentSqlDao = invoicePaymentSqlDao;
        this.accountApi = accountApi;
        this.paymentApi = paymentApi;
        this.clock = clock;
        this.invoiceRecorder = invoiceRecorder;
        this.accountRecorder = accountRecorder;
    }

    public void invoicePaymentPosted(final UUID accountId, final UUID paymentId, @Nullable final String extPaymentRefId, final String message) {
        final Account account;
        try {
            account = accountApi.getAccountById(accountId);
        } catch (AccountApiException e) {
            log.warn("Ignoring payment {}: account {} does not exist", paymentId, accountId);
            return;
        }

        final Payment payment;
        try {
            payment = paymentApi.getPayment(paymentId);
        } catch (PaymentApiException e) {
            log.warn("Ignoring payment {}: payment does not exist", paymentId);
            return;
        }

        final PaymentMethod paymentMethod;
        try {
            paymentMethod = paymentApi.getPaymentMethod(account, payment.getPaymentMethodId(), true);
        } catch (PaymentApiException e) {
            log.warn("Ignoring payment {}: payment method {} does not exist", paymentId, payment.getPaymentMethodId());
            return;
        }

        createPayment(account, payment, paymentMethod, extPaymentRefId, message);
    }

    private void createPayment(final Account account, final Payment payment, final PaymentMethod paymentMethod,
                               final String extPaymentRefId, final String message) {
        final PaymentMethodPlugin pluginDetail = paymentMethod.getPluginDetail();
        // TODO - make it generic
        final String cardCountry = pluginDetail != null ? pluginDetail.getValueString("country") : null;
        final String cardType = pluginDetail != null ? pluginDetail.getValueString("cardType") : null;
        // TODO support CreditCard, DebitCard, WireTransfer, BankTransfer, Check, ACH, Cash, Paypal
        final String paymentMethodString = cardType != null ? "CreditCard" : "Other";

        invoicePaymentSqlDao.inTransaction(new Transaction<Void, BusinessInvoicePaymentSqlDao>() {
            @Override
            public Void inTransaction(final BusinessInvoicePaymentSqlDao transactional, final TransactionStatus status) throws Exception {
                // Create the bip record
                final BusinessInvoicePayment invoicePayment = new BusinessInvoicePayment(
                        account.getExternalKey(),
                        payment.getAmount(),
                        extPaymentRefId,
                        cardCountry,
                        cardType,
                        clock.getUTCNow(),
                        payment.getCurrency(),
                        payment.getEffectiveDate(),
                        payment.getInvoiceId(),
                        message,
                        payment.getId(),
                        paymentMethodString,
                        "Electronic",
                        paymentMethod.getPluginName(),
                        payment.getPaymentStatus().toString(),
                        payment.getAmount(),
                        clock.getUTCNow()
                );
                transactional.createInvoicePayment(invoicePayment);

                // Update bin to get the latest invoice(s) balance(s)
                final BusinessInvoiceSqlDao invoiceSqlDao = transactional.become(BusinessInvoiceSqlDao.class);
                invoiceRecorder.rebuildInvoicesForAccountInTransaction(account.getId(), invoiceSqlDao);

                // Update bac to get the latest account balance, total invoice balance, etc.
                final BusinessAccountSqlDao accountSqlDao = transactional.become(BusinessAccountSqlDao.class);
                accountRecorder.updateAccountInTransaction(account, accountSqlDao);

                log.info("Added payment {}", invoicePayment);
                return null;
            }
        });
    }
}
