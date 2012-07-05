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

package com.ning.billing.analytics.model;

import java.math.BigDecimal;
import java.util.UUID;

import org.joda.time.DateTime;

import com.ning.billing.analytics.utils.Rounder;
import com.ning.billing.catalog.api.Currency;

public class BusinessInvoicePayment {
    private final UUID paymentId;
    private final DateTime createdDate;
    private final String extPaymentRefId;
    private final DateTime updatedDate;
    private final String accountKey;
    private final UUID invoiceId;
    private final DateTime effectiveDate;
    private final BigDecimal amount;
    private final Currency currency;
    private final String paymentError;
    private final String processingStatus;
    private final BigDecimal requestedAmount;
    private final String pluginName;
    private final String paymentType;
    private final String paymentMethod;
    private final String cardType;
    private final String cardCountry;

    public BusinessInvoicePayment(final String accountKey, final BigDecimal amount, final String extPaymentRefId,
                                  final String cardCountry, final String cardType, final DateTime createdDate,
                                  final Currency currency, final DateTime effectiveDate, final UUID invoiceId,
                                  final String paymentError, final UUID paymentId, final String paymentMethod,
                                  final String paymentType, final String pluginName, final String processingStatus,
                                  final BigDecimal requestedAmount, final DateTime updatedDate) {
        this.accountKey = accountKey;
        this.amount = amount;
        this.extPaymentRefId = extPaymentRefId;
        this.cardCountry = cardCountry;
        this.cardType = cardType;
        this.createdDate = createdDate;
        this.currency = currency;
        this.effectiveDate = effectiveDate;
        this.invoiceId = invoiceId;
        this.paymentError = paymentError;
        this.paymentId = paymentId;
        this.paymentMethod = paymentMethod;
        this.paymentType = paymentType;
        this.pluginName = pluginName;
        this.processingStatus = processingStatus;
        this.requestedAmount = requestedAmount;
        this.updatedDate = updatedDate;
    }

    public String getExtPaymentRefId() {
        return extPaymentRefId;
    }

    public DateTime getCreatedDate() {
        return createdDate;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public String getAccountKey() {
        return accountKey;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCardCountry() {
        return cardCountry;
    }

    public String getCardType() {
        return cardType;
    }

    public Currency getCurrency() {
        return currency;
    }

    public DateTime getEffectiveDate() {
        return effectiveDate;
    }

    public UUID getInvoiceId() {
        return invoiceId;
    }

    public String getPaymentError() {
        return paymentError;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getPluginName() {
        return pluginName;
    }

    public String getProcessingStatus() {
        return processingStatus;
    }

    public BigDecimal getRequestedAmount() {
        return requestedAmount;
    }

    public DateTime getUpdatedDate() {
        return updatedDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessInvoicePayment");
        sb.append("{accountKey='").append(accountKey).append('\'');
        sb.append(", paymentId=").append(paymentId);
        sb.append(", createdDate=").append(createdDate);
        sb.append(", extPaymentRefId=").append(extPaymentRefId);
        sb.append(", updatedDate=").append(updatedDate);
        sb.append(", invoiceId=").append(invoiceId);
        sb.append(", effectiveDate=").append(effectiveDate);
        sb.append(", amount=").append(amount);
        sb.append(", currency=").append(currency);
        sb.append(", paymentError='").append(paymentError).append('\'');
        sb.append(", processingStatus='").append(processingStatus).append('\'');
        sb.append(", requestedAmount=").append(requestedAmount);
        sb.append(", pluginName='").append(pluginName).append('\'');
        sb.append(", paymentType='").append(paymentType).append('\'');
        sb.append(", paymentMethod='").append(paymentMethod).append('\'');
        sb.append(", cardType='").append(cardType).append('\'');
        sb.append(", cardCountry='").append(cardCountry).append('\'');
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final BusinessInvoicePayment that = (BusinessInvoicePayment) o;

        if (accountKey != null ? !accountKey.equals(that.accountKey) : that.accountKey != null) {
            return false;
        }
        if (amount != null ? Rounder.round(amount) != Rounder.round(that.amount) : that.amount != null) {
            return false;
        }
        if (extPaymentRefId != null ? !extPaymentRefId.equals(that.extPaymentRefId) : that.extPaymentRefId != null) {
            return false;
        }
        if (cardCountry != null ? !cardCountry.equals(that.cardCountry) : that.cardCountry != null) {
            return false;
        }
        if (cardType != null ? !cardType.equals(that.cardType) : that.cardType != null) {
            return false;
        }
        if (createdDate != null ? !createdDate.equals(that.createdDate) : that.createdDate != null) {
            return false;
        }
        if (currency != that.currency) {
            return false;
        }
        if (effectiveDate != null ? !effectiveDate.equals(that.effectiveDate) : that.effectiveDate != null) {
            return false;
        }
        if (invoiceId != null ? !invoiceId.equals(that.invoiceId) : that.invoiceId != null) {
            return false;
        }
        if (paymentError != null ? !paymentError.equals(that.paymentError) : that.paymentError != null) {
            return false;
        }
        if (paymentId != null ? !paymentId.equals(that.paymentId) : that.paymentId != null) {
            return false;
        }
        if (paymentMethod != null ? !paymentMethod.equals(that.paymentMethod) : that.paymentMethod != null) {
            return false;
        }
        if (paymentType != null ? !paymentType.equals(that.paymentType) : that.paymentType != null) {
            return false;
        }
        if (pluginName != null ? !pluginName.equals(that.pluginName) : that.pluginName != null) {
            return false;
        }
        if (processingStatus != null ? !processingStatus.equals(that.processingStatus) : that.processingStatus != null) {
            return false;
        }
        if (requestedAmount != null ? Rounder.round(requestedAmount) != Rounder.round(that.requestedAmount) : that.requestedAmount != null) {
            return false;
        }
        if (updatedDate != null ? !updatedDate.equals(that.updatedDate) : that.updatedDate != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = paymentId != null ? paymentId.hashCode() : 0;
        result = 31 * result + (createdDate != null ? createdDate.hashCode() : 0);
        result = 31 * result + (extPaymentRefId != null ? extPaymentRefId.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        result = 31 * result + (accountKey != null ? accountKey.hashCode() : 0);
        result = 31 * result + (invoiceId != null ? invoiceId.hashCode() : 0);
        result = 31 * result + (effectiveDate != null ? effectiveDate.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        result = 31 * result + (currency != null ? currency.hashCode() : 0);
        result = 31 * result + (paymentError != null ? paymentError.hashCode() : 0);
        result = 31 * result + (processingStatus != null ? processingStatus.hashCode() : 0);
        result = 31 * result + (requestedAmount != null ? requestedAmount.hashCode() : 0);
        result = 31 * result + (pluginName != null ? pluginName.hashCode() : 0);
        result = 31 * result + (paymentType != null ? paymentType.hashCode() : 0);
        result = 31 * result + (paymentMethod != null ? paymentMethod.hashCode() : 0);
        result = 31 * result + (cardType != null ? cardType.hashCode() : 0);
        result = 31 * result + (cardCountry != null ? cardCountry.hashCode() : 0);
        return result;
    }
}
