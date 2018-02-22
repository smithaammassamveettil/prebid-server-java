package org.prebid.cookie;

import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.prebid.model.UidWithExpiry;
import org.prebid.model.Uids;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.emptyMap;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

public class UidsCookieTest {

    private static final String RUBICON = "rubicon";
    private static final String ADNXS = "adnxs";

    @Rule
    public final MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void creationShouldFailOnNullArguments() {
        assertThatNullPointerException().isThrownBy(() -> new UidsCookie(null));
    }

    @Test
    public void uidFromShouldReturnUids() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when and then
        assertThat(uidsCookie.uidFrom(RUBICON)).isEqualTo("J5VLCWQP-26-CWFT");
    }

    @Test
    public void uidFromShouldTolerateNullUids() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).build());

        // when and then
        assertThat(uidsCookie.uidFrom(RUBICON)).isNull();
    }

    @Test
    public void allowsSyncShouldReturnFalseIfOptoutTrue() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).optout(true).build());

        // when and then
        assertThat(uidsCookie.allowsSync()).isFalse();
    }

    @Test
    public void allowsSyncShouldReturnTrueIfOptoutAbsent() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).build());

        // when and then
        assertThat(uidsCookie.allowsSync()).isTrue();
    }

    @Test
    public void hasLiveUidsShouldReturnFalse() {
        // given
        Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.expired("J5VLCWQP-26-CWFT"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // then
        assertThat(uidsCookie.hasLiveUids()).isFalse();
    }

    @Test
    public void hasLiveUidsShouldReturnTrue() {
        // given
        Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // then
        assertThat(uidsCookie.hasLiveUids()).isTrue();
    }

    @Test
    public void hasLiveUidFromFamilyNameShouldReturnExpectedValue() {
        // given
        Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        uids.put(ADNXS, UidWithExpiry.expired("12345"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // then
        assertThat(uidsCookie.hasLiveUidFrom(RUBICON)).isTrue();
        assertThat(uidsCookie.hasLiveUidFrom(ADNXS)).isFalse();
    }

    @Test
    public void isFacebookSentinelShouldFailOnNullArguments() {
        assertThatNullPointerException().isThrownBy(() -> UidsCookie.isFacebookSentinel(null, null));
        assertThatNullPointerException().isThrownBy(() -> UidsCookie.isFacebookSentinel("", null));
    }

    @Test
    public void isFacebookSentinelShouldReturnTrueForAudienceNetworkAndZero() {
        assertThat(UidsCookie.isFacebookSentinel("audienceNetwork", "0")).isTrue();
    }

    @Test
    public void isFacebookSentinelShouldReturnFalseForAudienceNetworkAndNonZero() {
        assertThat(UidsCookie.isFacebookSentinel("audienceNetwork", "id")).isFalse();
    }

    @Test
    public void isFacebookSentinelShouldReturnFalseForNonAudienceNetwork() {
        assertThat(UidsCookie.isFacebookSentinel("rubicon", "0")).isFalse();
    }

    @Test
    public void deleteUidShouldFailOnNullArguments() {
        assertThatNullPointerException().isThrownBy(() -> new UidsCookie(Uids.builder().build()).deleteUid(null));
    }

    @Test
    public void deleteUidShouldReturnUidsCookieWithUidRemoved() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        uids.put(ADNXS, UidWithExpiry.live("12345"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.deleteUid(RUBICON);

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isNull();
        assertThat(uidsCookieReturned.uidFrom(ADNXS)).isEqualTo("12345");
    }

    @Test
    public void deleteUidShouldIgnoreMissingUid() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(ADNXS, UidWithExpiry.live("12345"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.deleteUid(RUBICON);

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isNull();
        assertThat(uidsCookieReturned.uidFrom(ADNXS)).isEqualTo("12345");
    }

    @Test
    public void deleteUidShouldTolerateNullUids() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.deleteUid(RUBICON);

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isNull();
    }

    @Test
    public void updateUidShouldFailOnNullArguments() {
        assertThatNullPointerException().isThrownBy(
                () -> new UidsCookie(Uids.builder().build()).updateUid(null, null));
        assertThatNullPointerException().isThrownBy(
                () -> new UidsCookie(Uids.builder().build()).updateUid("", null));
    }

    @Test
    public void updateUidShouldReturnUidsCookieWithUidReplaced() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        uids.put(ADNXS, UidWithExpiry.live("12345"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.updateUid(RUBICON, "updatedUid");

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isEqualTo("updatedUid");
        assertThat(uidsCookieReturned.uidFrom(ADNXS)).isEqualTo("12345");
    }

    @Test
    public void updateUidShouldReturnUidsCookieWithUidAdded() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(ADNXS, UidWithExpiry.live("12345"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.updateUid(RUBICON, "createdUid");

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isEqualTo("createdUid");
        assertThat(uidsCookieReturned.uidFrom(ADNXS)).isEqualTo("12345");
    }

    @Test
    public void updateUidShouldTolerateNullUids() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.updateUid(RUBICON, "createdUid");

        // then
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isEqualTo("createdUid");
    }

    @Test
    public void updateOptoutShouldReturnUidsCookieWithOptoutFlagOff() {
        // given
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(emptyMap()).optout(true).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.updateOptout(false);

        // then
        assertThat(uidsCookieReturned.allowsSync()).isTrue();
    }

    @Test
    public void updateOptoutShouldReturnUidsCookieWithOptoutFlagOn() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, UidWithExpiry.live("J5VLCWQP-26-CWFT"));
        final UidsCookie uidsCookie = new UidsCookie(Uids.builder().uids(uids).build());

        // when
        final UidsCookie uidsCookieReturned = uidsCookie.updateOptout(true);

        // then
        assertThat(uidsCookieReturned.allowsSync()).isFalse();
        assertThat(uidsCookieReturned.uidFrom(RUBICON)).isNull();
    }

    @Test
    public void toJsonShouldReturnCookieInValidJsonFormat() {
        // given
        final Map<String, UidWithExpiry> uids = new HashMap<>();
        uids.put(RUBICON, new UidWithExpiry("J5VLCWQP-26-CWFT", ZonedDateTime.parse("2017-12-30T12:30:40Z[GMT]")));

        final UidsCookie uidsCookie = new UidsCookie(Uids.builder()
                .uids(uids)
                .bday(ZonedDateTime.parse("2017-08-15T19:47:59.523908376Z"))
                .build());

        // when and then
        assertThat(uidsCookie.toJson()).isEqualTo("{\"tempUIDs\":{\"rubicon\":{\"uid\":\"J5VLCWQP-26-CWFT\"," +
                "\"expires\":\"2017-12-30T12:30:40.000000000Z\"}},\"bday\":\"2017-08-15T19:47:59.523908376Z\"}");
    }
}