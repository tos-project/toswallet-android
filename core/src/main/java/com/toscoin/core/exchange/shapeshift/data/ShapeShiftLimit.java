package com.toscoin.core.exchange.shapeshift.data;

import com.toscoin.core.coins.CoinID;
import com.toscoin.core.coins.CoinType;
import com.toscoin.core.coins.Value;

import org.json.JSONObject;

import java.math.RoundingMode;

import static com.toscoin.core.Preconditions.checkState;

/**
 * @author John L. Jegutanis
 */
public class ShapeShiftLimit extends ShapeShiftPairBase {
    public final Value limit;
    public final Value minimum;

    public ShapeShiftLimit(JSONObject data) throws ShapeShiftException {
        super(data);
        if (!isError) {
            try {
                String[] pairs = pair.split("_");
                checkState(pairs.length == 2);
                CoinType typeFrom = CoinID.typeFromSymbol(pairs[0]);
                limit = parseValue(typeFrom, data.getString("limit"), RoundingMode.DOWN);
                minimum = parseValue(typeFrom, data.getString("min"), RoundingMode.UP);
            } catch (Exception e) {
                throw new ShapeShiftException("Could not parse object", e);
            }
        } else {
            limit = null;
            minimum = null;
        }
    }
}
