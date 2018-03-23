package com.toscoin.core.coins;

import com.toscoin.core.coins.families.BitFamily;

/**
 * @author John L. Jegutanis
 */
public class TosCoinMain extends CoinType {
    private TosCoinMain() {
        id = "newcoin.main";

        addressHeader = 66;
        p2shHeader = 5;
        acceptableAddressCodes = new int[] { addressHeader, p2shHeader };
        spendableCoinbaseDepth = 60;

        family = BitFamily.get();
        name = "TosCoin";
        symbol = "TOS";
        uriScheme = "TosCoin";
        bip44Index = 99;
        unitExponent = 6;
        feePerKb = value(1000); // 0.0001 AUM
        minNonDust = value(1);
        softDustLimit = value(10000); // 0.01 AUM
        softDustPolicy = SoftDustPolicy.AT_LEAST_BASE_FEE_IF_SOFT_DUST_TXO_PRESENT;
        signedMessageHeader = toBytes("TosCoin Signed Message:\n");
    }

    private static TosCoinMain instance = new TosCoinMain();
    public static synchronized CoinType get() {
        return instance;
    }
}