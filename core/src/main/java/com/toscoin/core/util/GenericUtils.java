package com.toscoin.core.util;


import com.toscoin.core.coins.CoinID;
import com.toscoin.core.coins.CoinType;
import com.toscoin.core.coins.Value;
import com.toscoin.core.coins.ValueType;
import com.google.common.collect.ImmutableList;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Monetary;
import org.bitcoinj.core.VersionedChecksummedBytes;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import javax.annotation.Nonnull;

/**
 * @author Andreas Schildbach
 * @author John L. Jegutanis
 */
public class GenericUtils {
    private static final Pattern charactersO0 = Pattern.compile("[0O]");
    private static final Pattern characterIl = Pattern.compile("[Il]");
    private static final Pattern notBase58 = Pattern.compile("[^123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz]");

    public static String fixAddress(final String input) {
        String fixed = charactersO0.matcher(input).replaceAll("o");
        fixed = characterIl.matcher(fixed).replaceAll("1");
        fixed = notBase58.matcher(fixed).replaceAll("");
        return fixed;
    }

    public static String addressSplitToGroupsMultiline(final String address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.substring(0, 4));
        sb.append(" ");
        sb.append(address.substring(4, 8));
        sb.append(" ");
        sb.append(address.substring(8, 12));
        sb.append(" ");
        sb.append(address.substring(12, 17));
        sb.append("\n");
        sb.append(address.substring(17, 21));
        sb.append(" ");
        sb.append(address.substring(21, 25));
        sb.append(" ");
        sb.append(address.substring(25, 29));
        sb.append(" ");
        sb.append(address.substring(29));

        return sb.toString();
    }

    public static String addressSplitToGroups(final Address address) {
        return addressSplitToGroups(address.toString());
    }

    public static String addressSplitToGroups(final String address) {
        StringBuilder sb = new StringBuilder();
        sb.append(address.substring(0, 5));
        sb.append(" ");
        sb.append(address.substring(5, 9));
        sb.append(" ");
        sb.append(address.substring(9, 13));
        sb.append(" ");
        sb.append(address.substring(13, 17));
        sb.append(" ");
        sb.append(address.substring(17, 21));
        sb.append(" ");
        sb.append(address.substring(21, 25));
        sb.append(" ");
        sb.append(address.substring(25, 29));
        sb.append(" ");
        sb.append(address.substring(29));

        return sb.toString();
    }

    public static String formatValue(@Nonnull final Value value) {
        return formatCoinValue(value.type, value.toCoin(), "", "-", 8, 0);
    }

    public static String formatCoinValue(@Nonnull final ValueType type, @Nonnull final Monetary value) {
        return formatCoinValue(type, value, "", "-", 8, 0);
    }

    public static String formatCoinValue(@Nonnull final ValueType type, @Nonnull final Monetary value,
                                         final int precision, final int shift) {
        return formatCoinValue(type, value, "", "-", precision, shift);
    }

    public static String formatCoinValue(@Nonnull final ValueType type, @Nonnull final Monetary value,
                                         @Nonnull final String plusSign, @Nonnull final String minusSign,
                                         final int precision, final int shift) {
        return formatValue(type.getUnitExponent(), value, plusSign, minusSign, precision, shift, false);
    }

    public static String formatCoinValue(@Nonnull final ValueType type, @Nonnull final Monetary value,
                                         boolean removeFinalZeroes) {
        return formatValue(type.getUnitExponent(), value, "", "-", 8, 0, removeFinalZeroes);
    }

    private static String formatValue(final long unitExponent, @Nonnull final Monetary value,
                                      @Nonnull final String plusSign, @Nonnull final String minusSign,
                                      final int precision, final int shift, boolean removeFinalZeroes) {
        long longValue = value.getValue();

        final String sign = value.signum() == -1 ? minusSign : plusSign;

        String formatedValue;

        if (shift == 0) {
            long units = Math.round(Math.pow(10, unitExponent));
            long precisionUnits = (long) (units / Math.pow(10, precision));
            long roundingPrecisionUnits = precisionUnits / 2;

            if (precision == 2 || precision == 4 || precision == 6 || precision == 8) {
                if (roundingPrecisionUnits > 0) {
                    longValue = longValue - longValue % precisionUnits + longValue % precisionUnits / roundingPrecisionUnits * precisionUnits;
                }
            } else {
                throw new IllegalArgumentException("cannot handle precision/shift: " + precision + "/" + shift);
            }

            final long absValue = Math.abs(longValue);
            final long coins = absValue / units;
            final int satoshis = (int) (absValue % units);

            if (isShiftPossible(units, satoshis, 100)) {
                formatedValue = String.format(Locale.US, "%d.%02d",
                        coins, getShiftedCents(units, satoshis, 100));

            } else if (isShiftPossible(units, satoshis, 10000)) {
                formatedValue = String.format(Locale.US, "%d.%04d",
                        coins, getShiftedCents(units, satoshis, 10000));

            } else if (isShiftPossible(units, satoshis, 1000000)) {
                formatedValue = String.format(Locale.US, "%d.%06d", coins,
                        getShiftedCents(units, satoshis, 1000000));

            } else {
                formatedValue = String.format(Locale.US, "%d.%08d", coins, satoshis);
            }

        } else {
            throw new IllegalArgumentException("cannot handle shift: " + shift);
        }

        // Relax precision if incorrectly shows value as 0.00 but is in reality not zero
        if (formatedValue.equals("0.00") && value.getValue() != 0) {
            return formatValue(unitExponent, value, plusSign, minusSign, precision + 2, shift, removeFinalZeroes);
        }

        // Remove final zeroes if requested
        while (removeFinalZeroes && formatedValue.length() > 0 &&
                formatedValue.contains(".") && formatedValue.endsWith("0")) {
            formatedValue = formatedValue.substring(0, formatedValue.length() - 1);
        }
        if (removeFinalZeroes && formatedValue.length() > 0 && formatedValue.endsWith(".")) {
            formatedValue = formatedValue.substring(0, formatedValue.length() - 1);
        }

        // Add the sign if needed
        formatedValue = String.format(Locale.US, "%s%s", sign, formatedValue);

        return formatedValue;
    }

    private static long getShiftedCents(long units, int satoshis, int centAmount) {
        return satoshis / (units / centAmount);
    }

    private static boolean isShiftPossible(long units, int satoshis, int centAmount) {
        return units / centAmount != 0 && satoshis % (units / centAmount) == 0;
    }

    public static String formatFiatValue(final Value fiat, final int precision, final int shift) {
        return formatValue(fiat.smallestUnitExponent(), fiat, "", "-", precision, shift, false);
    }

    public static String formatFiatValue(Value fiat) {
        return formatFiatValue(fiat, 2, 0);
    }

    /**
     * Parses the provided string and returns the possible supported coin types.
     * Throws an AddressFormatException if the string is not a valid address or not supported.
     */
    public static List<CoinType> getPossibleTypes(String addressStr) throws AddressFormatException {
        VersionedChecksummedBytes parsed = new VersionedChecksummedBytes(addressStr) { };
        ImmutableList.Builder<CoinType> builder = ImmutableList.builder();
        int version = parsed.getVersion();
        for (CoinType type : CoinID.getSupportedCoins()) {
            for (int addressCode : type.getAcceptableAddressCodes()) {
                if (addressCode == version) {
                    builder.add(type);
                    break;
                }
            }
        }
        List<CoinType> possibleTypes = builder.build();
        if (possibleTypes.size() == 0) {
            throw new AddressFormatException("Unsupported address: " + addressStr);
        }
        return builder.build();
    }

    public static List<CoinType> getPossibleTypes(Address address) throws AddressFormatException {
        return getPossibleTypes(address.toString());
    }

    public static boolean hasMultipleTypes(Address address) {
        return hasMultipleTypes(address.toString());
    }

    public static boolean hasMultipleTypes(String addressStr) {
        try {
            return getPossibleTypes(addressStr).size() > 1;
        } catch (AddressFormatException e) {
            return false;
        }
    }
}