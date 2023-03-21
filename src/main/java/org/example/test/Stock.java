package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Stock extends GameEngine{
    static Map<Long, Integer> groupMap = new HashMap<>();
    Random random = new Random();
    GroupMessageEvent event;
    storage st;
    Member member;
    long ID, groupID;
    Group group;
    String name;
    String s;
    final static String[] coinNames = new String[]{"AAA", "BBB", "CCC", "DDD", "EEE"};
    Stock(GroupMessageEvent event, storage st) {
        super(event, st);
        this.st = st;
        this.event = event;
        this.member = event.getSender();
        this.ID = member.getId();
        this.group = event.getGroup();
        this.groupID = group.getId();
        this.s = event.getMessage().contentToString();
        this.name = member.getNameCard().length() > 1 ? member.getNameCard() : member.getNick();
    }

    @Override
    void start() {

    }
    void buy(String s) {
        if (!(s.startsWith("买") || s.startsWith("买名卡币") || s.startsWith("买pe币") || s.startsWith("买凯文币"))) {
            return;
        }
        String ss = new String("啊");
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '币') ss = s.substring(i + 1);
        }
        while (ss.startsWith(" ")) ss = ss.substring(1);
        if (!FirstModule.isInteger(ss)) return;
        String vv = "AAA";
        String coinName = "名卡币";
        if (s.startsWith("买p")) {
            vv = "BBB";coinName = "pe币";
        }
        else if (s.startsWith("买凯")) {
            vv = "CCC";coinName = "凯文币";
        }
        else if (s.startsWith("买jian"))  {
            vv = coinNames[3]; coinName = "jian币";
        }
        else if (s.startsWith("买中原")) {
            vv = coinNames[4]; coinName = "中原币";
        }
        long want = Long.parseLong(ss);
        long all = want * st.getStat(vv);
        long haveNow = st.getStat(ID, "AAA") * st.getStat("AAA") + st.getStat(ID, "BBB")* st.getStat("BBB") + st.getStat(ID, "CCC")* st.getStat("CCC");
        haveNow = 0;
        for (int i = 0; i < coinNames.length; i++) {
            haveNow += st.getStat(ID, coinNames[i]) * st.getStat(coinNames[i]);
        }
        if (want <= 0 || want >= 10000000 || haveNow + all + all > st.getCoin(ID)) {
            FirstModule.send(group, "你的现金不够！注意：禁止您的投资总额超过现金！");
        }
        else {
            long have1 = st.getStat(ID, vv);
            FirstModule.send(group, name + "购买了" + want +"个" + coinName + "，花费了金币" + all + "！");
            st.addStat(ID, vv, want);
            st.addCoin2(ID, -(all));
            long vvc = all / 111;
            st.addCoin(ID, -vvc);
            st.addCoin(ID, vvc);
            st.addEXP(ID, vvc);
        }
    }
    void change() {
        Random random = new Random();
        int mod = random.nextInt(1000);
        if (mod > 111) {
            int md = random.nextInt(17) - 8;
            st.addStat("CCC", md);
        }
        if (mod > 111) {
            int md = random.nextInt(13) - 6;
            st.addStat("BBB", md);
            st.addStat("EEE", 6 * md);
            int mdd=random.nextInt(23) - 11;
            st.addStat("EEE", mdd);
        }
        if (mod < 477) {
            int mdd = random.nextInt(13) - 6;
            long chastat = st.getStat("BBB") * mdd / 400;
            st.addStat("BBB", chastat);
            int mbb = random.nextInt(13) - 6;
            long chastat2 = st.getStat("DDD") * mdd / 400;
            st.addStat("DDD", chastat2);
        }
        if (mod > 599) {
            int md = random.nextInt(17) - 8;
            st.addStat("AAA", md);
            st.addStat("DDD", md / 3);
        }
        if (mod < 444) {
            int mdd = random.nextInt(7) - 3;
            long chastat = st.getStat("AAA") * mdd / 100;
            st.addStat("AAA", chastat);
            st.addStat("EEE", - chastat * 9);
        }
        if (mod == 377 || mod == 499 || mod == 655) {
            st.addStat("AAA", 1);
            st.addStat("BBB", 3);
            st.addStat("CCC", 5);
            if (mod == 377) st.addStat("DDD", 1);
            st.addStat("EEE", 6);
        }
        if (st.getStat("CCC") < 1000) st.addStat("CCC", 190);
        if (st.getStat("BBB") < 500) st.addStat("BBB", 160);
        if (st.getStat("AAA") < 100) st.addStat("AAA", 150);
        if (st.getStat("EEE") < 1500) st.addStat("EEE", 220);
        if (st.getStat("DDD") < 70) st.addStat("DDD", 120);
//        if (ID == 1779894826) FirstModule.send(group, st.getStat("AAA") + " " + st.getStat("BBB") + " " + st.getStat("CCC"));
    }
    void sell(String s) {
        if (!(s.startsWith("卖") || s.startsWith("卖pe币") || s.startsWith("卖凯文币"))) {
            return;
        }
        String ss = new String("啊");
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) == '币') ss = s.substring(i + 1);
        }
        while (ss.startsWith(" ")) ss = ss.substring(1);
        if (!FirstModule.isInteger(ss)) return;
        String vv = "AAA";
        String coinName = "g";
        if (s.startsWith("卖p")) {
            vv = "BBB";coinName = "pe币";
        }
        else if (s.startsWith("卖凯")) {
            vv = "CCC";coinName = "凯文币";
        }
        else if (s.startsWith("卖jian"))  {
            vv = coinNames[3]; coinName = "jian币";
        }
        else if (s.startsWith("卖中原")) {
            vv = coinNames[4]; coinName = "中原币";
        }
        long want = Long.parseLong(ss);
        long haveNow = st.getStat(ID, vv);
        if (want <= 0 || want >= 1000000000 || want > haveNow) {
            FirstModule.send(group, "出售不合规定！你的该币种数量不足！");
        }
        else {
            FirstModule.send(group, name + "出售了" + want +"个" + coinName + "，获得了金币" + want * st.getStat(vv) * 299 / 300 + "！");
            st.addStat(ID, vv, -want); st.addCoin(ID, want * st.getStat(vv) * 299 / 300);
        }
    }
    @Override
    void detect(String s) {
        change();
        if (s.equals("投资查询")) find();
        if (s.equals("投资帮助")) help();
        buy(s);
        sell(s);
    }
    public void help() {
        FirstModule.send(group, "输入买XX币进行购买（XX为你希望投资的金额），卖XX币进行卖出。交易所实时更新三种币的价格。" +
                "5种币的价格。注意：禁止投资金额超过现金总量。5种币分别为：名卡币，pe币，凯文币，jian币，中原币。\n输入投资查询可以查看实时走势。\n" +
                "投资建议：凯文币投资风险最小，名卡币和中原币投资风险最大，pe币和jian币投资风险适中。投资有风险，请量力而行！");
    }
    public void find(){
        long AAA = st.getStat(ID, "AAA");
        long BBB = st.getStat(ID, "BBB");
        long CCC = st.getStat(ID, "CCC");
        long DDD = st.getStat(ID, "DDD");
        long EEE = st.getStat(ID, "EEE");
        long haveNow = 0;
        for (int i = 0; i < coinNames.length; i++) {
            haveNow += st.getStat(ID, coinNames[i]) * st.getStat(coinNames[i]);
        }

        StringBuffer stringBuffer = new StringBuffer(name + "持有的名卡币数量为：" + AAA + "，pe币数量为：" + BBB + "，凯文币数量为：" + CCC + "，"
        + "持有的jian币数量为：" + DDD + "，持有的中原币数量为：" + EEE);
        stringBuffer.append("。\n你的投资总额为：" + haveNow + "\n");
        stringBuffer.append("你的现金为：" + st.getCoin(ID) + "\n");
        stringBuffer.append("当前名卡币价格为：" + st.getStat("AAA") + "，pe币价格为：" + st.getStat("BBB") + "，凯文币价格为：" + st.getStat("CCC") + "，"
        + "jian币价格为：" + st.getStat("DDD") + "，中原币价格为：" + st.getStat("EEE") + "。");
        FirstModule.send(group, stringBuffer.toString());
    }

    @Override
    void detectFriend(String s) {


    }
}
