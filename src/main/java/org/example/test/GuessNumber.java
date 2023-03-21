package org.example.test;

import net.mamoe.mirai.contact.Group; import net.mamoe.mirai.contact.Member; import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap; import java.util.Map; import java.util.Random;

public class GuessNumber extends GameEngine {
    static Map<Long, Integer> groupMap = new HashMap<>();
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

    GuessNumber(GroupMessageEvent event, storage st) {
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
        long val = 1788 + 41L * st.getLevel(ID) * st.getLevel(ID) / 3;
        int v = random.nextInt(20) + 91;
        val = val * v / 100;
        StringBuffer stringBuffer = new StringBuffer(name + "花费了" + val / 29 + "金币！\n");
        stringBuffer.append("当前已经猜了" + (countMap.get(groupID) + 1) + "次\n");
        st.addCoin(ID, -(val / 29));
        if (!groupMap.containsKey(groupID)) {
            FirstModule.send(group, "猜数字已开始！发送猜数字XXX进行猜测。猜中后，数字会自动改变（从1到300）。在该游戏中，有概率发生特殊事件！");
            groupMap.put(groupID, random.nextInt(300) + 1);
        } else if (random.nextInt(100) < 23) {
            int b = change();
            if (b == 1) stringBuffer.append("数字变大了一点点(1到15)!\n");
            if (b == 2) stringBuffer.append("数字变大了亿点点(1到50)!\n");
            if (b == -1) stringBuffer.append("数字变小了一点点(1到15)!\n");
            if (b == -2) stringBuffer.append("数字变小了亿点点(1到50)!\n");
        }
        String stt = s.substring(3);
        while (stt.startsWith(" ")) stt = stt.substring(1);
        if (!FirstModule.isInteger(stt)) {
            stringBuffer.append(name + "猜错了！");
            FirstModule.send(group, stringBuffer.toString());
            return;
        }
        int vv = Integer.parseInt(stt);
        if (vv == groupMap.get(groupID)) {
            stringBuffer.append("猜对了！\n" + name + "获得了" + val + "金币和922经验！\n数字已经重新随机！");
            st.addCoin(ID, val);
            st.addEXP(member, 922, group);
            groupMap.put(groupID, random.nextInt(300) + 1);
            countMap.put(groupID, 0);
        } else {
            if (vv < groupMap.get(groupID)) {
                stringBuffer.append("太小了！");
            }
            else stringBuffer.append("太大了！");
            countMap.put(groupID, countMap.get(groupID) + 1);
        }

        FirstModule.send(group, stringBuffer.toString());

    }

    int modifyVal(int val) {
        if (val <= 0) return 1;
        if (val > MAX) return MAX;
        return val;
    }

    int change() {
        int ans = random.nextInt(10);
        int val = groupMap.get(groupID);
        if (ans < 4) {
            val += random.nextInt(15) + 1;
            groupMap.put(groupID, modifyVal(val));
            return 1;
        } else if (ans < 8) {
            val -= random.nextInt(15) + 1;
            groupMap.put(groupID, modifyVal(val));
            return -1;
        } else if (ans == 8) {
            val += random.nextInt(50) + 1;
            groupMap.put(groupID, modifyVal(val));
            return 2;
        } else {
            val -= random.nextInt(50) + 1;
            groupMap.put(groupID, modifyVal(val));
            return -2;
        }

    }

    @Override
    void detect(String s) {
        if (s.equals("猜数字")) {
            FirstModule.send(group, "请直接发送猜数字XXX哦！提示：范围是1到300，第一次可以尝试发送猜数字150！");
            return;
        }
        if (s.startsWith("猜数字")) start();

    }

    @Override
    void detectFriend(String s) {

    }
}