package com.oldsugar.pattern.dao.impl;

import com.oldsugar.pattern.dao.FlyBehavior;

public class RocketFlyImpl implements FlyBehavior{

	@Override
	public void fly() {
		System.out.println("fly with rocket......");
	}

}
