package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Achievements {
    static Map<Long, Integer> groupMap = new HashMap<>();
    Random random = new Random();
    GroupMessageEvent event;
    storage st;
    Member member;
    long ID, groupID;
    Group group;
    String name;
    String s;

    Achievements(GroupMessageEvent event, storage st) {
        this.st = st;
        this.event = event;
        this.member = event.getSender();
        this.ID = member.getId();
        this.group = event.getGroup();
        this.groupID = group.getId();
        this.s = event.getMessage().contentToString();
        this.name = member.getNameCard().length() > 1 ? member.getNameCard() : member.getNick();
    }
    String hold(String detail, long val, long v1, long v2, long v3, String n1, String n2, String n3) {
        StringBuffer sb = new StringBuffer(detail + "(当前为" + val + ") ");
        if (val < v1) sb.append("暂无成就");
        else if (val < v2) sb.append("Lv.1 " + n1 + "，下一级需要" + v2);
        else if (val < v3) sb.append("Lv.2 " + n2 + "，下一级需要" + v3);
        else sb.append("Lv.MAX");
        return sb.toString();
    }
    void detect() {
//        if (ID == 1779894826) FirstModule.send(group, s);
        if (s.equals("成就")) {
            StringBuffer sb = new StringBuffer(name + "的成就：\n");
            sb.append(hold("金币自2.4日总数", st.getStat(ID, "consume"), 20000, 400000, 8000000, "小康阶层", "富甲一方", "富可敌国"));
            sb.append("\n" + hold("扔漂流瓶数", st.getStat(ID, "throw"), 20, 180, 666, "", "", ""));
            sb.append("\n" + hold("捡漂流瓶数", st.getStat(ID, "pick"), 20, 180, 666, "", "", ""));
            sb.append("\n" + hold("跳进海里数", st.getStat(ID, "jump"), 20, 180, 666, "深潜者", "海中生灵", "海洋霸主"));
            sb.append("\n" + hold("给bot送的礼物数", st.getStat(ID, "gift"), 40, 360, 1000, "bot之友I", "bot之友II", "bot之友III"));
            FirstModule.send(group, sb.toString());
        }
    }
}
