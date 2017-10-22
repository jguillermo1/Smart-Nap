package csuchico.smartnap;

import com.orm.SugarRecord;

import java.util.Vector;

/**
 *  Created by gerald on 10/13/2017.
 */

public class FlashCardSet extends SugarRecord<FlashCardSet> {
    int m_set_id;
    String m_FlashCardName;
    Vector<FlashCard>m_FlashCardSet;

    public FlashCardSet(int set_id, String FlashCardName, Vector<FlashCard> FlashCardSet){
        this.m_set_id = set_id;
        this.m_FlashCardName = FlashCardName;
        this.m_FlashCardSet = FlashCardSet;
    }
}
