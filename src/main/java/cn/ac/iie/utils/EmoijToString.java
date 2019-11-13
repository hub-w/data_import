package cn.ac.iie.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoijToString {
	/**
	 * @Description 将字符串中的emoji表情转换成可以在utf-8字符集数据库中保存的格式（表情占4个字节，需要utf8mb4字符集）
	 * @param str
	 *            待转换字符串
	 * @return 转换后字符串
	 * @throws UnsupportedEncodingException
	 *             exception
	 */
	public static String emojiConvert(String str)  {
		String patternString = "([\\x{10000}-\\x{10ffff}\ud800-\udfff])";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(str);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb, "[[" + URLEncoder.encode(matcher.group(1), "UTF-8") + "]]");
			} catch (UnsupportedEncodingException e) {
				System.out.println("emojiConvert error" + e);
			}
		}
		matcher.appendTail(sb);
		System.out.println("emojiConvert " + str + " to " + sb.toString() + ", len：" + sb.length());
		return sb.toString();
	}

	/**
	 * @Description 还原utf8数据库中保存的含转换后emoji表情的字符串
	 * @param str
	 *            转换后的字符串
	 * @return 转换前的字符串
	 * @throws UnsupportedEncodingException
	 *             exception
	 */
	public static String emojiRecovery2(String str)  {
		String patternString = "\\[\\[(.*?)\\]\\]";

		Pattern pattern = Pattern.compile(patternString);
		Matcher matcher = pattern.matcher(str);

		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			try {
				matcher.appendReplacement(sb, URLDecoder.decode(matcher.group(1), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				System.out.println("emojiRecovery error"+ e);
			}
		}
		matcher.appendTail(sb);
		System.out.println("emojiRecovery " + str + " to " + sb.toString());
		return sb.toString();
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
//		emojiConvert("6季全🔥特high清🔥dsadsaddddd撒旦撒旦撒");
		emojiConvert("\uD83D\uDD25老板说你不用回来了在家长期休假吧");
		String content = "<p>不一样的，有些东西，不是每个人都可以玩，只有大国才玩得起的。\n" +
				"</p><p>两弹一星>就是典型案例。</p><p>它需要太多的国家整体实力作为支撑，\n" +
				"</p><p>如果没有这些，你会牺牲数代人口才能搞出来，</p><p>即使搞出来，周边大国全比你先进，\n" +
				"</p><p>仍然没用。</p><p>然后你>又愚公移山地连续N代搞，</p><p>每次都落后。。\n" +
				"</p><p>每次都饿死很多人。。</p><p>最后你倒台了。。。</p><p>即使如苏联这样的大国，也因为和美国搞军备竞赛，解体了，何况朝鲜。\n" +
				"</p><p>英国是世界上第3个独立核试验的国家（1952年），国家软硬实力很强，全世界除美国以外的有钱人，很多都往英国跑。但现在它们只有极少数的核武器存货，\n" +
				"其它都不玩了。</p><p>法国也是如此，\n" +
				"搞核电厂的兴趣比搞核武器浓厚多了。\n" +
				"</p><p>至于航空和航天，根本一个人玩不起，只有欧洲组团一起玩。\n" +
				"</p><p><br></p><p>国家的整体实力是最关键的因素，NASA的航天飞机虽然倒了，但民营>的SpaceX 2017年航天发射次数全球第一，超越了举国之力的俄罗斯和中国，\n" +
				"最要命的是，<b>它现在开价比中国还便宜！</b></p><p><br></p><p><b>中国是未来世界话事者之一，\n" +
				"也将在未来参与游>戏规则的制定</b>，<i>幅员广阔，人口众多，潜力雄厚</i>。</p><p>1980之前，经济上基本崩溃。\n" +
				"</p><p>托改革开放之福，以及中国人民独有的吃苦耐劳，聪明能干，积累了大量财富和技术，</p><p>要保护自己的全球利益，必须玩这些。\n" +
				"</p><p>而同样玩这些，现在比60年代轻松多了，不需要举国之力。\n" +
				"</p><p>当你国家GDP特别大时，随便切一小块来，就足够对付。</p><p><br></p><p>但>一些核心的技术仍然欠缺，\n" +
				"</p><p>例如今年连续几次发射失败，据说是改用了新的国产火箭发动机（以前是俄罗斯的？）</p><p>未来国家之间的竞争不是在几个破岛和很快干涸的石油，\n" +
				"</p><p>而>是在<b>AI和太空</b>，</p><p>中国航天人要加油啊！</p><p><br></p><p>关于中美火箭对比，\n" +
				"请看下面</p><p><a href=\"https://www.zhihu.com/question/21731308\">SpaceX 猎鹰火箭和中国的长\n" +
				"征系列火箭，各有什么优劣势？</a></p><p><br></p><p>对于朝鲜人民来说，最好的路线是学日本，\n" +
				"军事暂时不投资（<b>背后有中俄，绝对不会看着美国进入38线之北</b>），但经济发达，各种先>进技术均有储备，一旦国家需要，\n" +
				"分秒钟搞出两弹一星来（能否立刻实战不好说，但日本搞出来的肯定比朝鲜的强）。</p><p><br></p><p>但这一招对于xxxx是不行的，</p><p>（以下删除N字）</p>";
		System.out.println("dsadas   "+content.length());
	}
}
