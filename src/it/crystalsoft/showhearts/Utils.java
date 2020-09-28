package it.crystalsoft.showhearts;

import java.util.Locale;
import java.util.Random;
import java.util.StringTokenizer;

public class Utils
{
	public static int randomNumber(int Min, int Max)
	{
		Random gen = new Random();

		return gen.nextInt(Max - Min) + Min;
	}

	public static Locale stringToLocale(String s)
	{
		StringTokenizer tempStringTokenizer = new StringTokenizer(s, ",");
		String l = null, c = null;

		if (tempStringTokenizer.hasMoreTokens())
		{
			l = (String)tempStringTokenizer.nextElement();
		}

		if (tempStringTokenizer.hasMoreTokens())
		{
			c = (String)tempStringTokenizer.nextElement();
		}

		if ((l != null) && (c != null))
		{
			return new Locale(l, c);
		}

		return null;
	}
}
