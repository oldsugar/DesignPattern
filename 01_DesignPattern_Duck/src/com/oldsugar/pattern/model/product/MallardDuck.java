package com.oldsugar.pattern.model.product;

import com.oldsugar.pattern.dao.impl.FlyImpl;
import com.oldsugar.pattern.dao.impl.QuackImpl;
import com.oldsugar.pattern.model.Duck;

public class MallardDuck extends Duck{
	
	public MallardDuck() {
//		quackBehavior.quack();
//		flyBehavior.fly();
		quackBehavior = new QuackImpl();
		flyBehavior = new FlyImpl();
	}

	@Override
	public void display() {
		System.out.println("I m mallardFuck!!~~~~");
	}

}
