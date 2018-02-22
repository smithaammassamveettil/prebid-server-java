package org.prebid.bidder.lifestreet;

import com.iab.openrtb.request.BidRequest;
import org.prebid.bidder.Bidder;
import org.prebid.bidder.model.BidderBid;
import org.prebid.bidder.model.HttpCall;
import org.prebid.bidder.model.HttpRequest;
import org.prebid.bidder.model.Result;

import java.util.List;

/**
 * Lifestreet {@link Bidder} implementation.
 * <p>
 * Maintainer email: <a href="mailto:mobile.tech@lifestreet.com">mobile.tech@lifestreet.com</a>
 */
public class LifestreetBidder implements Bidder {

    public LifestreetBidder() {
    }

    @Override
    public Result<List<HttpRequest>> makeHttpRequests(BidRequest bidRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Result<List<BidderBid>> makeBids(HttpCall httpCall, BidRequest bidRequest) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String name() {
        return "lifestreet";
    }

    @Override
    public String cookieFamilyName() {
        return "lifestreet";
    }
}