package com.toscoin.core.network.interfaces;

import com.toscoin.core.network.AddressStatus;
import com.toscoin.core.network.BlockHeader;
import com.toscoin.core.network.ServerClient;
import org.bitcoinj.core.Transaction;

import java.util.List;

/**
 * @author John L. Jegutanis
 */
public interface TransactionEventListener {
    void onNewBlock(BlockHeader header);

    void onAddressStatusUpdate(AddressStatus status);

//    void onUnspentTransactionUpdate(AddressStatus status, List<ServerClient.UnspentTx> unspentTxes);

    void onTransactionHistory(AddressStatus status, List<ServerClient.HistoryTx> historyTxes);

    void onTransactionUpdate(Transaction tx);

    void onTransactionBroadcast(Transaction transaction);

    void onTransactionBroadcastError(Transaction tx);
}
