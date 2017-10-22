package csuchico.smartnap;

import com.orm.SugarRecord;

/**
 * Created by gerald on 10/13/17.
 */

public class FlashCard extends SugarRecord<FlashCard>{

    int FC_ID;
    String m_question;
    String m_answer;

    // Note: Please retain default constructor
    public FlashCard() {

    }

    public FlashCard (String question, String answer){
        this.m_question = question;
        this.m_answer = answer;
    }
}
