package com.oldsugar.pattern.dao.impl;

import com.oldsugar.pattern.dao.QuackBehavior;

public class MuteQuackImpl implements QuackBehavior {

	@Override
	public void quack() {
		System.out.println("silence......");
	}

}
