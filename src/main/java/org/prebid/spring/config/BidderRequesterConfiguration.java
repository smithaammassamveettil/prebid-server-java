package org.prebid.spring.config;

import io.vertx.core.http.HttpClient;
import org.prebid.adapter.HttpConnector;
import org.prebid.adapter.conversant.ConversantAdapter;
import org.prebid.adapter.facebook.FacebookAdapter;
import org.prebid.adapter.index.IndexAdapter;
import org.prebid.adapter.lifestreet.LifestreetAdapter;
import org.prebid.adapter.pubmatic.PubmaticAdapter;
import org.prebid.adapter.pulsepoint.PulsepointAdapter;
import org.prebid.auction.BidderRequesterCatalog;
import org.prebid.bidder.BidderRequester;
import org.prebid.bidder.HttpAdapterRequester;
import org.prebid.bidder.HttpBidderRequester;
import org.prebid.bidder.appnexus.AppnexusBidder;
import org.prebid.bidder.rubicon.RubiconBidder;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.List;

@Configuration
public class BidderRequesterConfiguration {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequesterCatalog bidderRequesterCatalog(List<BidderRequester> bidderRequesters) {
        return new BidderRequesterCatalog(bidderRequesters);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester rubiconHttpConnector(RubiconBidder rubiconBidder, HttpClient httpClient) {
        return new HttpBidderRequester(rubiconBidder, httpClient);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester appnexusHttpConnector(AppnexusBidder appnexusBidder, HttpClient httpClient) {
        return new HttpBidderRequester(appnexusBidder, httpClient);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester pulsepointHttpConnector(PulsepointAdapter pulsepointAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(pulsepointAdapter, httpConnector);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(name = {"adapters.facebook.usersync-url", "adapters.facebook.platformId"})
    BidderRequester facebookHttpConnector(FacebookAdapter facebookAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(facebookAdapter, httpConnector);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @ConditionalOnProperty(name = "adapters.indexexchange.endpoint")
    BidderRequester indexHttpConnector(IndexAdapter indexAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(indexAdapter, httpConnector);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester lifestreetHttpConnector(LifestreetAdapter lifestreetAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(lifestreetAdapter, httpConnector);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester pubmaticHttpConnector(PubmaticAdapter pubmaticAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(pubmaticAdapter, httpConnector);
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    BidderRequester conversantHttpConnector(ConversantAdapter conversantAdapter, HttpConnector httpConnector) {
        return new HttpAdapterRequester(conversantAdapter, httpConnector);
    }
}