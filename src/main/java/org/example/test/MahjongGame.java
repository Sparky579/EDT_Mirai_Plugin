package org.example.test;


import net.mamoe.mirai.contact.Group; import net.mamoe.mirai.contact.Member; import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap; import java.util.Map; import java.util.Random;

public class MahjongGame extends GameEngine {
    static Map<Long, String[]> groupMap = new HashMap<>();
    static Map<Long, Integer> countMap = new HashMap<>();
    Random random = new Random();
    GroupMessageEvent event;
    storage st;
    Member member;
    long ID, groupID;
    Group group;
    String name;
    String s;
    final int MAX = 300;

    MahjongGame(GroupMessageEvent event, storage st) {
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
        if (!countMap.containsKey(groupID)) {
            countMap.put(groupID, 0);
        }
        StringBuffer stringBuffer = new StringBuffer(name + "开始打麻将！\n");
        stringBuffer.append("当前已经抓了" + (countMap.get(groupID) + 1) + "张牌\n");
        if (!groupMap.containsKey(groupID)) {
            FirstModule.send(group, "麻将已开始！发送抓牌来抓取牌，发送弃牌X来弃掉X号手牌，发送查看手牌来查看整手手牌。每个玩家一开始有13张随机的牌。");
            groupMap.put(groupID, getRandomCards());
        }
        if (s.equals("抓牌")) {
            stringBuffer.append("抓到了" + getRandomCard() + "！");
            countMap.put(groupID, countMap.get(groupID) + 1);
        } else if (s.startsWith("弃牌")) {
            String stt = s.substring(3);
            while (stt.startsWith(" ")) stt = stt.substring(1);
            if (!FirstModule.isInteger(stt)) {
                stringBuffer.append(name + "输入有误！");
                FirstModule.send(group, stringBuffer.toString());
                return;
            }
            int vv = Integer.parseInt(stt);
            if (vv < 1 || vv > 13) {
                stringBuffer.append(name + "输入有误！");
                FirstModule.send(group, stringBuffer.toString());
                return;
            }
            String[] cards = groupMap.get(groupID);
            stringBuffer.append("弃掉了" + cards[vv - 1] + "！");
            cards[vv - 1] = "";
            groupMap.put(groupID, cards);
        } else if (s.equals("查看手牌")) {
            String[] cards = groupMap.get(groupID);
            stringBuffer.append("手牌：");
            for (int i = 0; i < 13; i++) {
                if (cards[i].length() > 0) {
                    stringBuffer.append(cards[i] + " ");
                }
            }
        }
        FirstModule.send(group, stringBuffer.toString());
    }

    int modifyVal(int val) {
        if (val <= 0) return 1;
        if (val > MAX) return MAX;
        return val;
    }

    String getRandomCard() {
        int type = random.nextInt(3);
        int num = random.nextInt(9) + 1;
        if (type == 0) return num + "万";
        if (type == 1) return num + "饼";
        if (type == 2) return num + "条";
        return "";
    }

    String[] getRandomCards() {
        String[] cards = new String[13];
        for (int i = 0; i < 13; i++) {
            cards[i] = getRandomCard();
        }
        return cards;
    }

    @Override
    void detect(String s) {
        if (s.equals("打麻将")) {
            FirstModule.send(group, "请直接发送抓牌来抓取牌，发送弃牌X来弃掉X号手牌，发送查看手牌来查看整手手牌。每个玩家一开始有13张随机的牌。");
            return;
        }
        start();
    }

    @Override
    void detectFriend(String s) {

    }
}