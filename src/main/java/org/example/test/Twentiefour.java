package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.event.events.GroupMessageEvent;

import java.util.Random;
import java.util.Timer;

public class Twentiefour {
    static int[] arrays, numbers;
    protected GroupMessageEvent event;
    storage st;
    Group group;
    Member member;
    Twentiefour(GroupMessageEvent event, storage st) {
        this.event = event;
        this.st = st;
        group = event.getGroup();
        member = event.getSender();
    }
    static int haveChar(String s, char c) {
        int val = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) val++;
        }
        return val;
    }
    boolean isValid(String s) {
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.charAt(i) <= '9' && s.charAt(i) >= '0' && s.charAt(i+1) <= '9' && s.charAt(i+1) >= '0') {
                FirstModule.send(group, "不能把两个数字拼起来哦!");
                return false;
            }
        }
        for (int i = 0; i <= 9; i++) {
            if (haveChar(s, (char)('0' + i)) != arrays[i]) return false;
        }
        if (NifixExpression.evaluateExpression(s) != 24) return false;
        return true;
    }
    void detect(String s) {
        if (s.equals("24点")) {
            arrays = new int[14];
            numbers = new int[4];
            StringBuffer stringBuffer = new StringBuffer("24点开始!\n数字为：");
            Random random = new Random();
            for (int i = 0; i < 4; i++) {
                numbers[i] = random.nextInt(9) + 1;
                stringBuffer.append(numbers[i] + " ");
                arrays[numbers[i]]++;
            }
            FirstModule.send(group, stringBuffer.toString());
        }
        if (s.equals("查看")) {
            if (numbers == null) return;
            StringBuffer stringBuffer = new StringBuffer("24点数字为：");
            for (int i = 0; i < 4; i++) {
                stringBuffer.append(numbers[i] + " ");
            }
            FirstModule.send(group, stringBuffer.toString());
        }
        if (s.charAt(0) == '答') {
            if (numbers == null) return;
            boolean valid = isValid(s.substring(1));
            if (valid) {
                long val = 300 + 7L * st.getLevel(member.getId()) * st.getLevel(member.getId()) / 4;
                FirstModule.send(group, member.getNick() + "答题成功！奖励"+val+"金币，166经验！");
                st.addCoin(member.getId(), val);
                st.addEXP(member, 166, group);
                numbers = null;
            }
            else {
                FirstModule.send(group, member.getNick() + "答题失败！扣除50金币！");
                st.addCoin(member.getId(), -50);
            }
        }
    }
}
