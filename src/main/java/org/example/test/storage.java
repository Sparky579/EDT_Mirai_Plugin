package org.example.test;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;

import java.io.*;
import java.util.Date;
import java.util.Properties;

public class storage {
    Properties properties;
    final static String[] numbs = new String[]{"一","二","三","四","五","六","七"};
    final static String[] stats = new String[]{"青铜","白银","黄金","铂金","钻石"};
    final String path = "spk/myconfig.xml";
    storage() {
        properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(path);
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File file = new File(path);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    static String getStat(int level) {
        if (level <= 7) return numbs[level - 1] + "星士兵";
        if (level <= 22) return stats[(level - 8) / 3] + "级" + numbs[(level - 2) % 3] + "星战士";
        if (level <= 34) return stats[(level - 20) / 3] + "级" + numbs[(level - 2) % 3] + "星统领";
        if (level <= 55) return stats[(level - 21) / 7] + "级" + numbs[(level) % 7] + "星将军";
        return "神界" + getStat(level - 55);
    }
    public long getStat(String stat) {
        if (properties.getProperty(stat) == null) return 0;
        return Long.parseLong(properties.getProperty(stat));
    }
    public void addStat(String stat, long add) {
        long stat0 = getStat(stat);
        properties.setProperty(stat, stat0 + add + "");
        write();
    }
    private long getGuard(long ID) {
        if (!properties.containsKey(ID+".guard")) return 1000000000000L;
        return Long.parseLong(properties.getProperty(""+ID+".guard"));
    }
    public long withGuard(long ID) {//-1 represents false
        if (!properties.containsKey(ID+".guard")) return -1;
        long val = (new Date()).getTime() / 1000;
        long last = getGuard(ID);
        if (last > 900000000000L) return -2;
        if (val - last > 3600 * 8) return -3;
        return 3600L * 8 - (val - last);
    }
    public void setGuard(long ID) {
        long val = (new Date()).getTime() / 1000;
        properties.setProperty(ID+".guard", ""+val);
    }
    public void setASS(long ID) {
        properties.setProperty(ID+".ASS", "5");
    }
    public long getASS(long ID) {
        if (!properties.containsKey(ID+".ASS")) return -1;
        return Long.parseLong(properties.getProperty(ID+".ASS"));
    }
    public void consumeASS(long ID) {
        if (getASS(ID) <= 0) return;
        properties.setProperty(ID+".ASS", getASS(ID) - 1 +"");
    }
    public void addCoin(long ID, long add) {
        long coin = Long.parseLong(properties.getProperty(ID + ".coin"));
        coin += add;
        properties.setProperty(ID + ".coin", ""+coin);
        if (add < 0) this.addStat(ID, "consume", -add);
        write();
    }
    public void addCoin2(long ID, long add) {
        long coin = Long.parseLong(properties.getProperty(ID + ".coin"));
        coin += add;
        properties.setProperty(ID + ".coin", ""+coin);
        write();
    }
    public long getCoin(long ID) {
        return Long.parseLong(properties.getProperty(ID + ".coin"));
    }
    public void addEXP(Member member, long add, Group group) {
        long ID = member.getId();
        long exp = getEXP(ID);
        int level = getLevel(ID);
        properties.setProperty(ID + ".exp", exp + add + "");
        int level2 = getLevel(ID);
        if (level2 > level) FirstModule.send(group, "恭喜！！！" + member.getNick()
        + "从" + level + "级升到了" + level2 + "级！");
        write();
    }
    public void addEXP(long ID, long add) {
        long exp = getEXP(ID);
        properties.setProperty(ID + ".exp", exp + add + "");
        write();
    }
    public long getPower(long ID) {
        if (getCoin(ID) <= 0) return 0;
        return (long)(2 * Math.pow(1.07, getLevel(ID)) * Math.pow(getCoin(ID), 0.37) * Math.pow(1.5, getStat(ID, "zhuan")));
    }
    public long getEXP(long ID) {
        if (properties.getProperty(ID + ".exp") == null) return 0;
        return Long.parseLong(properties.getProperty(ID + ".exp"));
    }
    public long getEXP(Member member) {
        return getEXP(member.getId());
    }
    public long getStat(long ID, String stat) {
        if (properties.getProperty(ID + "." + stat) == null) return 0;
        return Long.parseLong(properties.getProperty(ID + "." + stat));
    }
    public void addStat(long ID, String stat, long add) {
        long stat0 = getStat(ID, stat);
        properties.setProperty(ID + "." + stat, stat0 + add + "");
        write();
    }
    public static long requireEXP(int level) {
        if (level > 34) return (long) (2 * Math.pow(1.04, level) * Math.pow(level + 10, 3.6)) - 2500000;
        return (long) (2 * Math.pow(1.1, level) * Math.pow(level + 3, 3.1));
    }
    public int getLevel(long ID) {
        long exp = getEXP(ID);
        for (int i = 100; i >= 2; i--) {
            if (exp > requireEXP(i)) return i;
        }
        return 1;
    }
    public void write() {
        try {
            OutputStream out = new FileOutputStream(path);
            properties.store(out, "write config");
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sleep(int time) {
        try {
            Thread.currentThread().sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
