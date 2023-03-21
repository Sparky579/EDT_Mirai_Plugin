package org.example.test;

import net.mamoe.mirai.contact.Group; import net.mamoe.mirai.contact.Member; import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap;

import java.util.Map;
import java.util.Random;

public class PlainGuess extends GameEngine {
    static Map<Long, Integer> groupMap = new HashMap<>();
    Random random = new Random();
    GroupMessageEvent event;
    storage st;
    Member member;
    long ID, groupID;
    Group group;
    String name;
    String s;
    final int MAX = 100;

    PlainGuess(GroupMessageEvent event, storage st) {
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
        long val = 327 + 7L * st.getLevel(ID) * st.getLevel(ID) / 3;
        int v = random.nextInt(20) + 91;
        val = val * v / 100;
        StringBuffer stringBuffer = new StringBuffer(name + "花费了" + val / 12 + "金币！\n");
        st.addCoin(ID, -(val / 12));
        if (!groupMap.containsKey(groupID)) {
            FirstModule.send(group, "猜数字已开始！发送猜数字XXX进行猜测。猜中后，数字会自动改变（从0到100）。");
            groupMap.put(groupID, random.nextInt(100) + 1);
        }
        String stt = s.substring(5);
        while (stt.startsWith(" ")) stt = stt.substring(1);
        if (!FirstModule.isInteger(stt)) {
            stringBuffer.append(name + "猜错了！");
            FirstModule.send(group, stringBuffer.toString());
            return;
        }
        int vv = Integer.parseInt(stt);
        if (vv == groupMap.get(groupID)) {
            stringBuffer.append("猜对了！\n" + name + "获得了" + val + "金币和133经验！\n数字已经重新随机！");
            st.addCoin(ID, val);
            st.addEXP(member, 133, group);
            groupMap.put(groupID, random.nextInt(100) + 1);
        } else if (vv < groupMap.get(groupID)) {
            stringBuffer.append("太小了！");
        } else stringBuffer.append("太大了！");
        FirstModule.send(group, stringBuffer.toString());

    }

    int modifyVal(int val) {
        if (val <= 0) return 1;
        if (val > MAX) return MAX;
        return val;
    }

    @Override
    void detect(String s) {
        if (s.equals("普通猜数字")) {
            FirstModule.send(group, "请直接发送普通猜数字XXX哦！提示：范围是0到100，第一次可以尝试发送猜数字50！");
            return;
        }
        if (s.startsWith("普通猜数字")) start();

    }

    @Override
    void detectFriend(String s) {

    }
}
