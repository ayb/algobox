package io.algobox.testing;

import com.google.common.collect.ImmutableMap;
import io.algobox.instrument.InstrumentInfoDetailed;
import io.algobox.instrument.InstrumentService;
import io.algobox.instrument.MarketHours;
import io.algobox.util.DateTimeUtils;
import io.algobox.util.MarketHoursUtils;
import io.algobox.util.MorePreconditions;

import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class TestingInstrumentService implements InstrumentService, Serializable {
  public static final String INSTRUMENT_DAX = "DAX";
  public static final String INSTRUMENT_EURUSD = "EURUSD";
  public static final String INSTRUMENT_GER30 = "GER30";

  private static final Map<String, InstrumentInfoDetailed> INSTRUMENTS_INFO =
      ImmutableMap.<String, InstrumentInfoDetailed>builder()
          .put(INSTRUMENT_EURUSD,
              new InstrumentInfoDetailed("EURUSD", 17, 0, 17, 0, true, 8, 4, "America/New_York"))
          .put(INSTRUMENT_DAX,
              new InstrumentInfoDetailed("DAX", 9, 0, 17, 45, false, 9, 0, "Europe/Berlin"))
          .put(INSTRUMENT_GER30,
              new InstrumentInfoDetailed("GER30", 9, 0, 17, 45, false, 9, 0, "Europe/Berlin"))
          .build();

  @Override
  public InstrumentInfoDetailed getInstrumentInfo(String instrumentId) {
    MorePreconditions.checkNotNullOrEmpty(instrumentId);
    InstrumentInfoDetailed instrumentInfo = INSTRUMENTS_INFO.get(instrumentId);
    if (instrumentInfo == null) {
      throw new IllegalArgumentException("Instrument not found.");
    }
    return instrumentInfo;
  }

  @Override
  public Optional<MarketHours> getMarketHours(String instrumentId, long timestampUtc) {
    InstrumentInfoDetailed info = checkNotNull(getInstrumentInfo(instrumentId));
    return MarketHoursUtils.getMarketHours(info, timestampUtc);
  }

  @Override
  public Optional<MarketHours> getMarketHoursYesterday(String instrumentId) {
    long yesterday = DateTimeUtils.getCurrentUtcTimestampMilliseconds() - (24 * 60 * 60 * 1000);
    return getMarketHours(instrumentId, yesterday);
  }
}
