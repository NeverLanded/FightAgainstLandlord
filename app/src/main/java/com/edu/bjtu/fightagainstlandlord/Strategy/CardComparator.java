package com.edu.bjtu.fightagainstlandlord.Strategy;

import com.edu.bjtu.fightagainstlandlord.Data.State.State;
import com.edu.bjtu.fightagainstlandlord.System.CardSystem;
import com.edu.bjtu.fightagainstlandlord.Data.CardData;
import com.edu.bjtu.fightagainstlandlord.Data.Iterator.CardDataIterator;

import java.util.Arrays;
import java.util.List;

/**
 * 扑克比较器
 */

public  class CardComparator {
    /**
     * 判断这些牌是不是一个大小
     * @param num  牌的数量
     * @param card 牌的集合
     * @return true，这些牌都是同一个大小；false，这些牌不同大小
     */
    private static boolean judgeSameCard(int num, int card[]) {
        for (int i = 1; i < num; i++)
            if (card[0] / 4 != card[i] / 4) return false;
        return true;
    }
    /**
     * 判断卡片是不是龙
     *
     * @param num  牌的数量
     * @param card 牌的集合
     * @return true，严格逐1递减；false，不符合要求
     */
    private static boolean judgeCardContinuity(int num, int card[]) {
        if (num < 5 || card[0] >= 48) return false;//牌的张数不能小于5，最大的牌不能是2或者以上；
        for (int i = 0; i < num - 1; i++) {
            if (card[i] / 4 - 1 != card[i + 1] / 4) return false;
        }
        return true;
    }
    /**
     * 判断卡片是不是连对
     * @param num  牌的数量
     * @param card 牌的集合
     * @return true，是连对；false，不是连对
     */
    private static boolean judgeCardPairs(int num, int card[]) {
        int i;
        if (num % 2 != 0 || num <= 4 || card[0] >= 48)
            return false;//将不是偶数张牌，0~4张牌，最大的牌不能是2或者以上的情况排除在外
        for (i = 0; i < num - 1; i += 2) {
            if (card[i] / 4 != card[i + 1] / 4) return false;//相邻牌是否相等
            if (i + 2 < num && card[i] / 4 - 1 != card[i + 2] / 4) return false;//连续偶数牌是否相差1
        }
        return true;
    }
    /**
     * 判断出牌的类型是什么
     * @param cardDataList 卡牌集合
     * @return -1:不允许这样出牌； 0:单牌；
     * 1:1对对子，2:3对对子，3:4对对子，4:5对对子，5:6对对子，6:7对对子，7:8对对子，8:9对对子，9:10对对子；
     * 10:3张， 11:3张带1个，12:3张带1对；
     * 13:两3张， 14:两3张带两个单牌， 15:两3张带两对
     * 16:三3张， 17:三3张带三个单牌， 18:三3张带三对
     * 19:四3张   20:四3张带四单牌， 21:四3张带四对
     * 22：五三张  23:五三张带五单牌
     * 30:4张，普通炸弹，31:4张炸弹带两张单牌， 32:4张炸弹带1对
     * 40:5张龙，41:6张龙 42：7张龙 43：8张龙 44:9张龙，45:10张龙，46：11张龙，47：12张龙
     * 50:王炸
     */
    public static int judgeCardType(List<CardData> cardDataList) {
        int card[] = new int[cardDataList.size()];
        CardDataIterator iterator = new CardDataIterator(cardDataList);
        int count = 0, num = cardDataList.size();
        while (!iterator.isLast()){
            card[count++] = iterator.getNextItem().getNum();
            iterator.next();
        }

        if (num == 1) return 0;//单牌
        if (num == 2) {
            if (judgeSameCard(num, card)) {
                if (card[0] == 52 || card[1] == 52)
                    return 50;//王炸
                return 1;//一对
            }
        }

        if (num == 4) {
            if (judgeSameCard(num, card)) return 30;//炸弹
            if (judgeSameCard(num - 1, Arrays.copyOfRange(card, 0, num - 1)) || judgeSameCard(num - 1, Arrays.copyOfRange(card, 1, num)))
                return 11;//三张带一个
        }
        if (judgeCardContinuity(num, card)) return (35 + num);//判断是不是龙

        if (judgeCardPairs(num, card)) return (num / 2 - 1);//判断是不是连对

        //判断炸弹带两张
        if (num == 6) {
            if (judgeSameCard(num - 2, Arrays.copyOfRange(card, 0, num - 2)) ||
                    judgeCardContinuity(num - 2, Arrays.copyOfRange(card, 1, num - 1)) ||
                    judgeCardContinuity(num - 2, Arrays.copyOfRange(card, 2, num))) {
                if (judgeSameCard(2, Arrays.copyOfRange(card, 0, 2)) ||
                        judgeSameCard(2, Arrays.copyOfRange(card, 2, 4)) ||
                        judgeSameCard(2, Arrays.copyOfRange(card, 4, 6)))
                    return 32;
                else return 31;
            }
        }

        if (num % 3 == 0) {
            boolean ifThree = true;
            for (int i = 0; i < num / 3 && ifThree; i++)
                if (!judgeSameCard(3, Arrays.copyOfRange(card, i * 3, i * 3 + 3))) ifThree = false;
            if (ifThree) return (7 + num);
        }



        //3张带一对
        if (num == 5) {
            if ((judgeSameCard(3, Arrays.copyOfRange(card, 0, 3)) && judgeSameCard(2, Arrays.copyOfRange(card, 3, 5)))
                    || (judgeSameCard(3, Arrays.copyOfRange(card, 2, 5)) && judgeSameCard(2, Arrays.copyOfRange(card, 0, 2))) )
                return 12;
        }
        //两个3带二
        if (num == 8){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,2,4))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,1, 4)) && judgeSameCard(3, Arrays.copyOfRange(card, 4, 7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,3,5))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,4,6))) )
                return 14;
        }

        //两个3带两对
        if (num == 10){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,6,8)) && judgeSameCard(2,Arrays.copyOfRange(card,8,10))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,8,10))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8))) && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) )
                return 15;
        }

        //三3张带3单牌
        if (num == 12){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,1, 4)) && judgeSameCard(3, Arrays.copyOfRange(card, 4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,3,5)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeSameCard(3, Arrays.copyOfRange(card, 8, 11)) && judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,7,9))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10))) )
                return 17;
        }

        /* 18:三3张带三对*/
        if (num == 15){
            //先三连，再三对
            if (judgeSameCard(3,Arrays.copyOfRange(card,0,3)) && judgeSameCard(3,Arrays.copyOfRange(card,3,6)) && judgeSameCard(3,Arrays.copyOfRange(card,6,9)) &&
                    judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) &&
                    judgeSameCard(2,Arrays.copyOfRange(card,9,11)) && judgeSameCard(2,Arrays.copyOfRange(card,11,13)) && judgeSameCard(2,Arrays.copyOfRange(card,13,15))){
                return 18;
            }
            //先一对，三连，两对
            if (judgeSameCard(3,Arrays.copyOfRange(card,2,5)) && judgeSameCard(3,Arrays.copyOfRange(card,5,8)) && judgeSameCard(3,Arrays.copyOfRange(card,8,11)) &&
                    judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,7,9)) &&
                    judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,11,13)) && judgeSameCard(2,Arrays.copyOfRange(card,13,15))){
                return 18;
            }
            // 先两对，三连，一对
            if (judgeSameCard(3,Arrays.copyOfRange(card,4,7)) && judgeSameCard(3,Arrays.copyOfRange(card,7,10)) && judgeSameCard(3,Arrays.copyOfRange(card,10,13)) &&
                    judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11)) &&
                    judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,13,15))){
                return 18;
            }
            // 先三对，三连
            if (judgeSameCard(3,Arrays.copyOfRange(card,6,9)) && judgeSameCard(3,Arrays.copyOfRange(card,9,12)) && judgeSameCard(3,Arrays.copyOfRange(card,12,15)) &&
                    judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,11,13)) &&
                    judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,4,6))){
                return 18;
            }
        }

        /*20:四3张带四单牌，*/
        if (num == 16){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,1, 4)) && judgeSameCard(3, Arrays.copyOfRange(card, 4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeSameCard(3, Arrays.copyOfRange(card, 10, 13)) && judgeCardContinuity(2,Arrays.copyOfRange(card,3,5)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeSameCard(3, Arrays.copyOfRange(card, 8, 11)) && judgeSameCard(3, Arrays.copyOfRange(card, 11, 14)) && judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,7,9)) && judgeCardContinuity(2,Arrays.copyOfRange(card,10,12))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeSameCard(3, Arrays.copyOfRange(card, 12, 15)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,11,13))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeSameCard(3, Arrays.copyOfRange(card, 10, 13)) && judgeSameCard(3, Arrays.copyOfRange(card, 13, 16)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11)) && judgeCardContinuity(2,Arrays.copyOfRange(card,12,14))) )
                return 20;
        }
        /*21:四3张带四对*/
        if (num == 20){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeSameCard(2,Arrays.copyOfRange(card,12,14)) && judgeSameCard(2,Arrays.copyOfRange(card,14,16)) && judgeSameCard(2,Arrays.copyOfRange(card,16,18)) && judgeSameCard(2,Arrays.copyOfRange(card,18,20))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeSameCard(3, Arrays.copyOfRange(card, 8, 11)) && judgeSameCard(3, Arrays.copyOfRange(card, 11, 14)) && judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,7,9)) && judgeCardContinuity(2,Arrays.copyOfRange(card,10,12))  && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,14,16)) && judgeSameCard(2,Arrays.copyOfRange(card,16,18)) && judgeSameCard(2,Arrays.copyOfRange(card,18,20))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeSameCard(3, Arrays.copyOfRange(card, 10, 13)) && judgeSameCard(3, Arrays.copyOfRange(card, 13, 16)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11)) && judgeCardContinuity(2,Arrays.copyOfRange(card,12,14)))  && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,16,18)) && judgeSameCard(2,Arrays.copyOfRange(card,18,20)) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeSameCard(3, Arrays.copyOfRange(card, 12, 15)) && judgeSameCard(3, Arrays.copyOfRange(card, 15, 18)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,11,13)) && judgeCardContinuity(2,Arrays.copyOfRange(card,14,16)))  && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,4,6)) && judgeSameCard(2,Arrays.copyOfRange(card,18,20)) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,8, 11)) && judgeSameCard(3, Arrays.copyOfRange(card, 11, 14)) && judgeSameCard(3, Arrays.copyOfRange(card, 14, 17)) && judgeSameCard(3, Arrays.copyOfRange(card, 17, 20)) && judgeCardContinuity(2,Arrays.copyOfRange(card,10,12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,13,15)) && judgeCardContinuity(2,Arrays.copyOfRange(card,16,18)))  && judgeSameCard(2,Arrays.copyOfRange(card,0,2)) && judgeSameCard(2,Arrays.copyOfRange(card,2,4)) && judgeSameCard(2,Arrays.copyOfRange(card,4,6)) && judgeSameCard(2,Arrays.copyOfRange(card,6,8)))
                return 21;
        }
        /*23:五三张带五单牌 */
        if (num == 20){
            if ( (judgeSameCard(3,Arrays.copyOfRange(card,0, 3)) && judgeSameCard(3, Arrays.copyOfRange(card, 3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeSameCard(3, Arrays.copyOfRange(card, 12, 15)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,2,4)) && judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,11,13))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,1, 4)) && judgeSameCard(3, Arrays.copyOfRange(card, 4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeSameCard(3, Arrays.copyOfRange(card, 10, 13)) && judgeSameCard(3, Arrays.copyOfRange(card, 13, 16)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,3,5)) && judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11)) && judgeCardContinuity(2,Arrays.copyOfRange(card,12,14))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,2, 5)) && judgeSameCard(3, Arrays.copyOfRange(card, 5, 8)) && judgeSameCard(3, Arrays.copyOfRange(card, 8, 11)) && judgeSameCard(3, Arrays.copyOfRange(card, 11, 14)) && judgeSameCard(3, Arrays.copyOfRange(card, 14, 17)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,4,6)) && judgeCardContinuity(2,Arrays.copyOfRange(card,7,9)) && judgeCardContinuity(2,Arrays.copyOfRange(card,10,12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,13,15))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,3, 6)) && judgeSameCard(3, Arrays.copyOfRange(card, 6, 9)) && judgeSameCard(3, Arrays.copyOfRange(card, 9, 12)) && judgeSameCard(3, Arrays.copyOfRange(card, 12, 15)) && judgeSameCard(3, Arrays.copyOfRange(card, 15, 18)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,5,7)) && judgeCardContinuity(2,Arrays.copyOfRange(card,8,10)) && judgeCardContinuity(2,Arrays.copyOfRange(card,11,13)) && judgeCardContinuity(2,Arrays.copyOfRange(card,14,16))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,4, 7)) && judgeSameCard(3, Arrays.copyOfRange(card, 7, 10)) && judgeSameCard(3, Arrays.copyOfRange(card, 10, 13)) && judgeSameCard(3, Arrays.copyOfRange(card, 13, 16)) && judgeSameCard(3, Arrays.copyOfRange(card, 16, 19)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,6,8)) && judgeCardContinuity(2,Arrays.copyOfRange(card,9,11)) && judgeCardContinuity(2,Arrays.copyOfRange(card,12,14)) && judgeCardContinuity(2,Arrays.copyOfRange(card,15,17))) ||
                    (judgeSameCard(3,Arrays.copyOfRange(card,5, 8)) && judgeSameCard(3, Arrays.copyOfRange(card, 8, 11)) && judgeSameCard(3, Arrays.copyOfRange(card, 11, 14)) && judgeSameCard(3, Arrays.copyOfRange(card, 14, 17)) && judgeSameCard(3, Arrays.copyOfRange(card, 17, 20)) &&judgeCardContinuity(2,Arrays.copyOfRange(card,7,9)) && judgeCardContinuity(2,Arrays.copyOfRange(card,10,12)) && judgeCardContinuity(2,Arrays.copyOfRange(card,13,15)) && judgeCardContinuity(2,Arrays.copyOfRange(card,16,18))) )
            return 23;
        }
        return -1;//不允许出牌
    }
    /**
     * 比较同类型牌的大小
     * @param cardDataList 将要出的牌的集合
     * @return true，比前一轮玩家出的牌要大，允许出牌；false，反正，不允许出牌。
     */
    private static boolean compareSameTypeSize(List<CardData> cardDataList){
        State state = CardSystem.getInstance().getState();
        int nowNum = cardDataList.size();
        int lastType = state.getLastType(state.getLastTurn());//获得前一次出牌的类型
        List<CardData> lastCardDataList = state.getLastCardDataList(state.getLastTurn());
        int lastCardNum = lastCardDataList.size();
        //生成preCard
        int preCard[] = new int[lastCardDataList.size()], nowCard[] = new int[cardDataList.size()], count = 0;
        CardDataIterator iterator = new CardDataIterator(lastCardDataList);
        while (!iterator.isLast()){
            preCard[count++] = iterator.getNextItem().getNum();
            iterator.next();
        }
        //生成nowCard;
        iterator = new CardDataIterator(cardDataList);
        count = 0;
        while (!iterator.isLast()){
            nowCard[count++] = iterator.getNextItem().getNum();
            iterator.next();
        }

        CardOperation cardOperation = new CardOperationAdapter();
        if (lastType == -1 || lastType == 50) return false;
        if (lastType == 0) return ((nowCard[0]/4) > (preCard[0]/4));
        if ((lastType > 0 && lastType <= 10) || (lastType >= 13 && lastType <= 22 && lastType % 3 == 1) || lastType == 30 || (lastType >= 40 && lastType <= 47)) return nowCard[0] > preCard[0];
        if (lastType == 11 || lastType == 12 || lastType == 14 || lastType == 15 || lastType == 17 ||
                lastType == 18 || lastType == 20 || lastType == 21 || lastType == 23){

            return nowCard[cardOperation.findSameCardLocation(nowNum, nowCard, 3)] >
                    preCard[cardOperation.findSameCardLocation(lastCardNum, preCard, 3)];
        }
        /*31:4张炸弹带两张单牌， 32:4张炸弹带1对 */
        return ((lastType == 31|| lastType == 32) &&
                (nowCard[cardOperation.findSameCardLocation(nowNum,nowCard,4)] >
                        preCard[cardOperation.findSameCardLocation(lastCardNum,preCard,4)]));
    }
    /**
     * 计算是否允许出牌人出牌
     * @param cardDataList 当前想要出的牌的集合
     * @return true，允许出牌；false，不允许出牌
     */
    public static boolean ifCardCanCommit(List<CardData> cardDataList){
        State state = CardSystem.getInstance().getState();
        int nowTurn = state.getTurn();
        int lastTurn = state.getLastTurn();
        int lastType = state.getLastType(lastTurn);
        if (cardDataList == null || cardDataList.size() == 0) return false;//没牌肯定不可以出
        if (nowTurn == lastTurn) return (judgeCardType(cardDataList) != -1);//如果上一轮是同一个人出，只需要判断出牌类型是否正确
        int nowType = judgeCardType(cardDataList);
        if (nowType == 50) return true;//王炸肯定可以出
        if (nowType == lastType) return compareSameTypeSize(cardDataList);
        else{
            if (lastType <=23 || (lastType >= 31 && lastType < 50 ))
                return nowType == 30;
        }
        return false;//否则不允许出牌
    }

}
